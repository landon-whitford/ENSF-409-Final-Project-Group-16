package edu.ucalgary.oop;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * The SchedulingService class is responsible for assigning ride requests
 * to available drivers and vehicles based on schedule constraints, capacity,
 * and accessibility requirements.
 * <p>
 * It also handles rescheduling when a driver becomes unavailable.
 */

public class SchedulingService {
    private final DataAccessManager dataManager;
    /**
     * Constructs a SchedulingService using the specified DataAccessManager.
     *
     * @param dataManager the data manager used for database operations
     */

    public SchedulingService(DataAccessManager dataManager) {

        this.dataManager = dataManager;
    }

    /**
     * Attempts to schedule a given RideRequest. Assigns the request to the most suitable
     * available driver and vehicle. Prioritizes smaller capacity vehicles when possible.
     *
     * @param request the ride request to be scheduled
     * @return true if the ride was successfully scheduled, false if set to "Pending"
     */

    public boolean scheduleRideRequest(RideRequest request) {
        try {
            LocalDate date = request.getRequestDate();
            LocalTime time = request.getPickupTime();
            LocalTime endTime = time.plusMinutes(30);

            String specialReqs = request.getSpecialRequirements();
            boolean needsWheelchair = specialReqs != null && specialReqs.toLowerCase().contains("wheelchair");

            int passengerCount = request.getPassengerCount();

            List<Driver> availableDrivers = dataManager.getAvailableDrivers(date, time.minusMinutes(30), endTime.plusMinutes(30));
            List<Vehicle> availableVehicles = dataManager.getAvailableVehicles(date, time.minusMinutes(30), endTime.plusMinutes(30), needsWheelchair, passengerCount);

            // Prefer vehicles with the smallest sufficient capacity
            availableVehicles.sort(Comparator.comparingInt(Vehicle::getCapacity));

            for (Vehicle vehicle : availableVehicles) {
                for (Driver driver : availableDrivers) {
                    if (!isConflict(driver, vehicle, request)) {
                        Schedule newSchedule = new Schedule(0, driver, vehicle, request,
                                request.getRequestDate(), request.getPickupTime());

                        dataManager.addSchedule(newSchedule);
                        request.setStatus("Scheduled");
                        dataManager.updateRideRequest(request);
                        return true;
                    }
                }
            }

            // No match found — mark as pending
            request.setStatus("Pending");
            dataManager.updateRideRequest(request);
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Reschedules all rides previously assigned to a given driver.
     * Any ride scheduled with that driver is reverted to "Pending"
     * and the system attempts to reassign it automatically.
     *
     * @param driverId the ID of the driver who is no longer available
     */
    public void rescheduleRidesForDriver(int driverId) {
        try {
            List<Schedule> allSchedules = dataManager.getAllSchedules();

            for (Schedule schedule : allSchedules) {
                if (schedule.getDriver().getDriverID() == driverId &&
                        schedule.getRideRequest().getStatus().equalsIgnoreCase("Scheduled")) {
                    RideRequest req = schedule.getRideRequest();
                    req.setStatus("Pending");
                    dataManager.updateRideRequest(req);
                }
            }

            // Attempt to reschedule all pending requests
            for (RideRequest r : dataManager.getAllRideRequests()) {
                if (r.getStatus().equalsIgnoreCase("Pending")) {
                    scheduleRideRequest(r);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if assigning a given driver and vehicle to a ride request would
     * result in a scheduling conflict.
     *
     * @param driver  the driver to check
     * @param vehicle the vehicle to check
     * @param request the ride request being considered
     * @return true if there is a conflict, false otherwise
     * @throws SQLException if a database error occurs
     */

    private boolean isConflict(Driver driver, Vehicle vehicle, RideRequest request) throws SQLException {
        LocalDate date = request.getRequestDate();
        LocalTime time = request.getPickupTime();
        LocalTime endTime = time.plusMinutes(30);

        return !dataManager.getAvailableDrivers(date, time.minusMinutes(30), endTime.plusMinutes(30))
                .stream().anyMatch(d -> d.getDriverID() == driver.getDriverID())
                || !dataManager.getAvailableVehicles(date, time.minusMinutes(30), endTime.plusMinutes(30),
                        vehicle.isWheelchairAccessible(), request.getPassengerCount())
                .stream().anyMatch(v -> v.getVehicleID() == vehicle.getVehicleID());
    }


}