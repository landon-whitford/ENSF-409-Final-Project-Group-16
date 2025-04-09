package edu.ucalgary.oop;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class SchedulingService {
    private final DataAccessManager dataManager;

    public SchedulingService(DataAccessManager dataManager) {
        this.dataManager = dataManager;
    }

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

            for (RideRequest r : dataManager.getAllRideRequests()) {
                if (r.getStatus().equalsIgnoreCase("Pending")) {
                    scheduleRideRequest(r);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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