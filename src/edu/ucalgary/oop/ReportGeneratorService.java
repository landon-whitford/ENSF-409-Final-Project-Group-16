package edu.ucalgary.oop;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportGeneratorService {

    private DataAccessManager dataManager;

    public ReportGeneratorService(DataAccessManager dataManager) {
        this.dataManager = dataManager;
    }

    /**
     * Creates a daily schedule file for the specified date
     *
     * @param date The date for which to generate the schedule
     * @return true if the file was created successfully, false otherwise
     */
    public boolean createDailyScheduleFile(LocalDate date) throws SQLException, IOException {
        List<Schedule> schedules = dataManager.getSchedulesByDate(date);

        // Create the file with the required name format
        String filename = "daily_schedule_" + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Format header exactly like the example
            writer.println("Accessible Transportation Daily Schedule - " + date.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
            writer.println();

            // Organize schedules by driver
            Map<Integer, List<Schedule>> schedulesByDriver = new HashMap<>();

            // Get all drivers
            List<Driver> allDrivers = dataManager.getAllDrivers();
            for (Driver driver : allDrivers) {
                schedulesByDriver.put(driver.getDriverID(), new java.util.ArrayList<>());
            }

            // Add schedules to their respective drivers
            for (Schedule schedule : schedules) {
                int driverId = schedule.getDriver().getDriverID();
                schedulesByDriver.get(driverId).add(schedule);
            }

            // Process each driver
            for (Driver driver : allDrivers) {
                List<Schedule> driverSchedules = schedulesByDriver.get(driver.getDriverID());

                writer.println("Driver: " + driver.getName() + " (" + driver.getLicenseNumber() + ")");

                if (driverSchedules.isEmpty()) {
                    if (!driver.isAvailable()) {
                        writer.println("Vehicle: Not Assigned");
                        writer.println("--------------------------------------------------");
                        writer.println("Status: Currently unavailable.");
                        writer.println("--------------------------------------------------");
                        writer.println();
                        continue;
                    } else {
                        // Driver is available but has no rides
                        writer.println("Vehicle: Not Assigned");
                        writer.println("--------------------------------------------------");
                        writer.println("No rides assigned for this driver today.");
                        writer.println("--------------------------------------------------");
                        writer.println();
                        continue;
                    }
                }

                // Driver has schedules - get vehicle from first schedule
                Vehicle vehicle = driverSchedules.get(0).getVehicle();
                writer.println("Vehicle: " + vehicle.getLicensePlate() +
                        (vehicle.isWheelchairAccessible() ? " (Wheelchair Accessible)" : ""));
                writer.println("--------------------------------------------------");

                for (Schedule schedule : driverSchedules) {
                    // Format time as in the example (09:00 AM)
                    writer.println(schedule.getTime().format(DateTimeFormatter.ofPattern("hh:mm a")).toUpperCase());
                    writer.println("    Client: " + schedule.getRideRequest().getClientName());
                    writer.println("    Pickup: " + schedule.getRideRequest().getPickUpLocation());
                    writer.println("    Dropoff: " + schedule.getRideRequest().getDropOffLocation());
                    writer.println("    Special Requirements: " +
                            (schedule.getRideRequest().getSpecialRequirements() != null &&
                                    !schedule.getRideRequest().getSpecialRequirements().isEmpty() ?
                                    schedule.getRideRequest().getSpecialRequirements() : "None"));
                    writer.println("--------------------------------------------------");
                }

                writer.println();
            }
        }

        return true;
    }

    /**
     * Creates a weekly report file for the week containing the specified date
     *
     * @param date Any date within the week for which to generate the report
     * @return true if the file was created successfully, false otherwise
     */
    public boolean createWeeklyReportFile(LocalDate date) throws SQLException, IOException {
        // Find the Monday of the current week
        LocalDate monday = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate sunday = monday.plusDays(6);

        // Get all schedules for the week
        List<Schedule> weeklySchedules = dataManager.getSchedulesByDateRange(monday, sunday);

        // Create the file with the required name format
        String filename = "weekly_report_" + monday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Format header exactly like the example
            writer.println("Accessible Transportation Weekly Summary - " +
                    monday.format(DateTimeFormatter.ofPattern("MMMM d")) + " to " +
                    sunday.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")));
            writer.println();

            // Total ride count
            writer.println("Total Rides Scheduled: " + weeklySchedules.size());
            writer.println();

            // Vehicle usage stats
            writer.println("Vehicle Usage:");
            Map<Integer, Integer> vehicleRideCounts = new HashMap<>();

            // Initialize counts for all vehicles
            List<Vehicle> allVehicles = dataManager.getAllVehicles();
            for (Vehicle vehicle : allVehicles) {
                vehicleRideCounts.put(vehicle.getVehicleID(), 0);
            }

            // Count rides per vehicle
            for (Schedule schedule : weeklySchedules) {
                int vehicleId = schedule.getVehicle().getVehicleID();
                vehicleRideCounts.put(vehicleId, vehicleRideCounts.get(vehicleId) + 1);
            }

            // Output vehicle usage
            for (Vehicle vehicle : allVehicles) {
                int rideCount = vehicleRideCounts.get(vehicle.getVehicleID());
                writer.println("    - " + vehicle.getLicensePlate() + ": " + rideCount + " rides, " +
                        "Maintenance Due by " + vehicle.getMaintenanceDueDate().format(DateTimeFormatter.ofPattern("MMMM d, yyyy")));
            }
            writer.println();

            // Driver activity stats
            writer.println("Driver Activity:");
            Map<Integer, Integer> driverRideCounts = new HashMap<>();

            // Initialize counts for all drivers
            List<Driver> allDrivers = dataManager.getAllDrivers();
            for (Driver driver : allDrivers) {
                driverRideCounts.put(driver.getDriverID(), 0);
            }

            // Count rides per driver
            for (Schedule schedule : weeklySchedules) {
                int driverId = schedule.getDriver().getDriverID();
                driverRideCounts.put(driverId, driverRideCounts.get(driverId) + 1);
            }

            // Output driver activity
            for (Driver driver : allDrivers) {
                if (driver.isAvailable()) {
                    writer.println("    - " + driver.getName() + ": " + driverRideCounts.get(driver.getDriverID()) + " rides");
                } else {
                    writer.println("    - " + driver.getName() + ": Unavailable");
                }
            }
            writer.println();

            // Special requirements summary
            writer.println("Special Requirements Overview:");
            int wheelchairCount = 0;
            int standardCount = 0;

            for (Schedule schedule : weeklySchedules) {
                String specialReqs = schedule.getRideRequest().getSpecialRequirements();
                if (specialReqs != null && specialReqs.contains("Wheelchair")) {
                    wheelchairCount++;
                } else {
                    standardCount++;
                }
            }

            writer.println("    - Wheelchair Accessible Rides: " + wheelchairCount);
            writer.println("    - Standard Rides: " + standardCount);
        }

        return true;
    }
}