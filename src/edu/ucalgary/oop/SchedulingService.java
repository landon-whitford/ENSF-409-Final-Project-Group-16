package edu.ucalgary.oop;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * The SchedulingService class is responsible for coordinating the scheduling
 * of ride requests with available drivers and vehicles. It implements the
 * core scheduling logic for the Accessible Transportation System, ensuring that
 * ride requests are properly assigned to appropriate resources when possible.
 * This service handles initial scheduling of ride requests, rescheduling when
 * driver availability changes, and manages constraints such as wheelchair
 * accessibility and passenger capacity.
 *
 * @author Group 16
 * @version 1.2
 * @since 1.0
 */

public class SchedulingService {
    private final DataAccessManager dataManager;

    /**
     * Constructs a new SchedulingService with the specified data manager.
     * @param dataManager the DataAccessManager to access and update scheduling data
     */

    public SchedulingService(DataAccessManager dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * Attempts to schedule a ride request with an available driver and vehicle.
     * The method checks for vehicles that meet the passenger capacity and wheelchair
     * accessibility requirements, and available drivers during the requested time.
     * If scheduling is successful, the ride request status is updated to "Scheduled"
     * and a new schedule entry is created in the database. If scheduling fails, the
     * ride request status is set to "Pending".
     *
     * @param request the RideRequest to be scheduled
     * @return true if the request was successfully scheduled, false otherwise
     */

    public boolean scheduleRideRequest(RideRequest request) {
        try {
            LocalDate date = request.getRequestDate();
            LocalTime time = request.getPickupTime();
            LocalTime endTime = time.plusMinutes(30);

            boolean needsWheelchair = request.getSpecialRequirements() != null &&
                    request.getSpecialRequirements().toLowerCase().contains("wheelchair");
            int passengerCount = request.getPassengerCount();

            List<Driver> availableDrivers = dataManager.getAvailableDrivers(date, time.minusMinutes(30), endTime.plusMinutes(30));
            List<Vehicle> availableVehicles = dataManager.getAvailableVehicles(date, time.minusMinutes(30), endTime.plusMinutes(30), needsWheelchair, passengerCount);

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

            request.setStatus("Pending");
            dataManager.updateRideRequest(request);
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Reschedules all rides that were previously assigned to a driver whose
     * availability has changed. This method is called when a driver's availability
     * is set to unavailable.
     * All scheduled rides for the specified driver are set to "Pending" status,
     * and then the system attempts to reschedule all pending rides using other
     * available drivers and vehicles.
     *
     * @param driverId the ID of the driver whose rides need to be rescheduled
     */

    public void rescheduleRidesForDriver(int driverId) {
        try {
            List<Schedule> allSchedules = dataManager.getAllSchedules();

            // Modify schedules for the specific driver
            for (Schedule schedule : allSchedules) {
                if (schedule.getDriver().getDriverID() == driverId &&
                        schedule.getRideRequest() != null &&
                        "Scheduled".equalsIgnoreCase(schedule.getRideRequest().getStatus())) {
                    RideRequest req = schedule.getRideRequest();
                    req.setStatus("Pending");
                    dataManager.updateRideRequest(req);
                }
            }

            // Attempt to reschedule all pending requests
            List<RideRequest> pendingRequests = dataManager.getAllRideRequests().stream()
                    .filter(r -> r != null && "Pending".equalsIgnoreCase(r.getStatus()))
                    .collect(java.util.stream.Collectors.toList());

            for (RideRequest r : pendingRequests) {
                if (r != null) {
                    scheduleRideRequest(r);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if there is a scheduling conflict for a specific driver, vehicle, and ride request.
     * This method verifies that both the driver and vehicle are available during the
     * time slot required for the ride request.
     * @param driver the Driver to check for availability
     * @param vehicle the Vehicle to check for availability
     * @param request the RideRequest with the time slot to check
     * @return true if there is a conflict (driver or vehicle unavailable), false otherwise
     * @throws SQLException if a database access error occurs
     */

    private boolean isConflict(Driver driver, Vehicle vehicle, RideRequest request) throws SQLException {
        LocalDate date = request.getRequestDate();
        LocalTime time = request.getPickupTime();
        LocalTime endTime = time.plusMinutes(30);

        // Check if the driver is available during the required time slot
        boolean driverAvailable = dataManager.getAvailableDrivers(date, time.minusMinutes(30), endTime.plusMinutes(30))
                .stream().anyMatch(d -> d.getDriverID() == driver.getDriverID());

        // Check if the vehicle is available during the required time slot
        boolean vehicleAvailable = dataManager.getAvailableVehicles(date, time.minusMinutes(30), endTime.plusMinutes(30),
                        vehicle.isWheelchairAccessible(), request.getPassengerCount())
                .stream().anyMatch(v -> v.getVehicleID() == vehicle.getVehicleID());

        // Return true if there is a conflict (either driver or vehicle is unavailable)
        return !driverAvailable || !vehicleAvailable;
    }
}