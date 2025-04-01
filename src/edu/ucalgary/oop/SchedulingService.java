package edu.ucalgary.oop;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

public class SchedulingService {
    private DataAccessManager dataManager;

    public SchedulingService(DataAccessManager dataManager) {
        this.dataManager = dataManager;
    }

    public boolean scheduleRideRequest(RideRequest request) {
        List<Driver> availableDrivers = dataManager.getAllDrivers();
        List<Vehicle> allVehicles = dataManager.getAllVehicles();
        List<Schedule> currentSchedules = dataManager.getAllSchedules();

        // Filter available drivers
        List<Driver> drivers = new ArrayList<>();
        for (Driver d : availableDrivers) {
            if (d.isAvailable()) {
                drivers.add(d);
            }
        }

        // Sort vehicles by capacity (smallest first)
        allVehicles.sort(Comparator.comparingInt(Vehicle::getCapacity));

        for (Vehicle vehicle : allVehicles) {
            if (vehicle.getCapacity() >= request.getPassengerCount()) {
                if (request.getSpecialRequirements().toLowerCase().contains("wheelchair") && !vehicle.isWheelchairAccessible()) {
                    continue;
                }

                for (Driver driver : drivers) {
                    if (!isConflict(driver, vehicle, request, currentSchedules)) {
                        Schedule newSchedule = new Schedule();
                        newSchedule.setDriver(driver);
                        newSchedule.setVehicle(vehicle);
                        newSchedule.setRideRequest(request);
                        newSchedule.setScheduledDate(convertToLocalDate(request.getRequestDate()));
                        newSchedule.setScheduledTime(convertToLocalTime(request.getPickupTime()));
                        dataManager.addSchedule(newSchedule);

                        request.setStatus("Scheduled");
                        dataManager.updateRideRequest(request);
                        return true;
                    }
                }
            }
        }

        request.setStatus("Pending");
        dataManager.updateRideRequest(request);
        return false;
    }

    public void rescheduleRidesForDriver(int driverId) {
        List<Schedule> schedules = dataManager.getAllSchedules();
        for (Schedule s : schedules) {
            if (s.getDriver().getDriverID() == driverId && s.getRideRequest().getStatus().equals("Scheduled")) {
                s.getRideRequest().setStatus("Pending");
                dataManager.updateRideRequest(s.getRideRequest());
            }
        }

        // Re-attempt scheduling for all pending requests
        for (RideRequest r : dataManager.getAllRideRequests()) {
            if (r.getStatus().equalsIgnoreCase("Pending")) {
                scheduleRideRequest(r);
            }
        }
    }

    private boolean isConflict(Driver driver, Vehicle vehicle, RideRequest request, List<Schedule> schedules) {
        LocalDate requestDate = convertToLocalDate(request.getRequestDate());
        LocalTime requestTime = convertToLocalTime(request.getPickupTime());

        for (Schedule s : schedules) {
            if (s.getScheduledDate().equals(requestDate)) {
                if (s.getDriver().getDriverID() == driver.getDriverID() || s.getVehicle().getVehicleID() == vehicle.getVehicleID()) {
                    LocalTime scheduled = s.getScheduledTime();
                    if (Math.abs(scheduled.until(requestTime, java.time.temporal.ChronoUnit.MINUTES)) < 90) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private LocalTime convertToLocalTime(java.sql.Time time) {
        return time.toLocalTime();
    }
}
