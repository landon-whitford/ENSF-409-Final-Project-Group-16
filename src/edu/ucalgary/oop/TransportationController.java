package edu.ucalgary.oop;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * The TransportationController class serves as the central coordinator for the
 * Accessible Transportation System. It acts as an intermediary between the user
 * interface and the various services that perform business logic and data access.
 * This controller handles operations related to ride requests, drivers, vehicles,
 * and schedules, delegating tasks to the appropriate services and coordinating
 * their interactions. It abstracts the underlying implementation details from
 * the user interface layer.
 *
 * @author Group 16
 * @version 1.7
 * @since 1.0
 */

public class TransportationController {
    private DataAccessManager dataManager;
    private SchedulingService schedulingService;
    private ReportGeneratorService reportService;

    /**
     * Constructs a new TransportationController with the specified services.
     * @param dataManager the DataAccessManager for database operations
     * @param schedulingService the SchedulingService for ride scheduling operations
     * @param reportService the ReportGeneratorService for report generation
     */

    public TransportationController(DataAccessManager dataManager,
                                    SchedulingService schedulingService,
                                    ReportGeneratorService reportService) {
        this.dataManager = dataManager;
        this.schedulingService = schedulingService;
        this.reportService = reportService;
    }

    //------------------------------------------------------------
    // RideRequest operations
    //------------------------------------------------------------

    /**
     * Retrieves all ride requests from the system.
     * @return a List of all RideRequest objects
     * @throws SQLException if a database access error occurs
     */

    public List<RideRequest> viewAllRideRequests() throws SQLException {
        return dataManager.getAllRideRequests();
    }

    /**
     * Retrieves a specific ride request by its ID.
     * @param id the ID of the ride request to retrieve
     * @return the RideRequest object if found, null otherwise
     * @throws SQLException if a database access error occurs
     */

    public RideRequest getRideRequestById(int id) throws SQLException {
        return dataManager.getRideRequestById(id);
    }

    /**
     * Adds a new ride request to the system and attempts to schedule it.
     * If scheduling is successful, the ride request status will be set to "Scheduled".
     * If scheduling fails, the ride request status will remain "Pending".
     * @param request the RideRequest object to add
     * @return true if the ride request was successfully added (regardless of scheduling result),
     *         false if there was an error adding the request
     * @throws SQLException if a database access error occurs
     */

    public boolean addRideRequest(RideRequest request) throws SQLException {
        // First add the request to the database
        boolean added = dataManager.addRideRequest(request);

        if (added) {
            // Then try to schedule it
            boolean scheduled = schedulingService.scheduleRideRequest(request);

            // If scheduling failed, it will remain in "Pending" status
            // which was set by the SchedulingService
            return true;
        }

        return false;
    }

    /**
     * Modifies an existing ride request and updates its scheduling if necessary.
     * If the request status is "Scheduled", the system will attempt to reschedule it
     * based on the updated information. If rescheduling fails, the status will be
     * changed to "Pending".
     * @param request the RideRequest object with updated information
     * @return true if the ride request was successfully updated (regardless of scheduling result),
     *         false if there was an error updating the request
     * @throws SQLException if a database access error occurs
     */

    public boolean modifyRideRequest(RideRequest request) throws SQLException {
        // First, update the request in the database
        boolean updated = dataManager.updateRideRequest(request);

        if (updated) {
            // If the status is "Scheduled", we need to check if the schedule is still valid
            if ("Scheduled".equals(request.getStatus())) {
                // Try to reschedule the request
                boolean scheduled = schedulingService.scheduleRideRequest(request);

                // If scheduling failed, it will be set to "Pending" by the SchedulingService
                return true;
            }
            return true;
        }

        return false;
    }

    /**
     * Cancels a ride request by changing its status to "Cancelled".
     * A request can only be cancelled if it is not already completed or cancelled.
     * @param id the ID of the ride request to cancel
     * @return true if the ride request was successfully cancelled,
     *         false if the request was not found or could not be cancelled
     * @throws SQLException if a database access error occurs
     */

    public boolean cancelRideRequest(int id) throws SQLException {
        RideRequest request = dataManager.getRideRequestById(id);

        if (request == null) {
            return false;
        }

        if ("Completed".equals(request.getStatus()) || "Cancelled".equals(request.getStatus())) {
            return false;
        }

        request.setStatus("Cancelled");
        return dataManager.updateRideRequest(request);
    }

    /**
     * Marks a ride request as completed by changing its status to "Completed".
     * A request can only be completed if it is currently scheduled.
     * @param id the ID of the ride request to complete
     * @return true if the ride request was successfully completed,
     *         false if the request was not found or could not be completed
     * @throws SQLException if a database access error occurs
     */

    public boolean completeRideRequest(int id) throws SQLException {
        RideRequest request = dataManager.getRideRequestById(id);

        if (request == null) {
            return false;
        }

        if (!"Scheduled".equals(request.getStatus())) {
            return false;
        }

        request.setStatus("Completed");
        return dataManager.updateRideRequest(request);
    }

    //------------------------------------------------------------
    // Driver operations
    //------------------------------------------------------------

    /**
     * Retrieves all drivers from the system.
     * @return a List of all Driver objects
     * @throws SQLException if a database access error occurs
     */

    public List<Driver> viewAllDrivers() throws SQLException {
        return dataManager.getAllDrivers();
    }

    /**
     * Retrieves a specific driver by ID.
     * @param id the ID of the driver to retrieve
     * @return the Driver object if found, null otherwise
     * @throws SQLException if a database access error occurs
     */

    public Driver getDriverById(int id) throws SQLException {
        return dataManager.getDriverById(id);
    }

    /**
     * Updates a driver's availability status. If a driver is set to unavailable,
     * all their scheduled rides will be reassigned to other drivers if possible,
     * or set to "Pending" if reassignment is not possible.
     * @param id the ID of the driver whose availability is being updated
     * @param isAvailable the new availability status
     * @return true if the driver's availability was successfully updated,
     *         false if there was an error updating the status
     * @throws SQLException if a database access error occurs
     */

    public boolean modifyDriverAvailability(int id, boolean isAvailable) throws SQLException {
        boolean updated = dataManager.updateDriverAvailability(id, isAvailable);

        if (updated) {
            // If driver was set to unavailable, we need to reschedule their rides
            if (!isAvailable) {
                schedulingService.rescheduleRidesForDriver(id);
            }
            return true;
        }

        return false;
    }

    //------------------------------------------------------------
    // Vehicle operations
    //------------------------------------------------------------

    /**
     * Retrieves all vehicles from the system.
     * @return a List of all Vehicle objects
     * @throws SQLException if a database access error occurs
     */

    public List<Vehicle> viewAllVehicles() throws SQLException {
        return dataManager.getAllVehicles();
    }

    //------------------------------------------------------------
    // Schedule operations
    //------------------------------------------------------------

    /**
     * Retrieves all schedule entries from the system.
     * @return a List of all Schedule objects
     * @throws SQLException if a database access error occurs
     */

    public List<Schedule> viewAllSchedules() throws SQLException {
        return dataManager.getAllSchedules();
    }

    /**
     * Generates a daily schedule file for the specified date.
     * The file includes details of all scheduled rides for that date,
     * organized by driver and vehicle.
     * @param date the date for which to generate the schedule
     * @return true if the daily schedule was successfully generated,
     *         false if there was an error or no rides were scheduled for the date
     * @throws SQLException if a database access error occurs
     */

    public boolean generateDailySchedule(LocalDate date) throws SQLException {
        try {
            return reportService.createDailyScheduleFile(date);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Generates a weekly report file for the current week.
     * The report includes statistics on total rides, vehicle usage,
     * driver activity, and special requirements.
     * @return true if the weekly report was successfully generated,
     *         false if there was an error
     * @throws SQLException if a database access error occurs
     */

    public boolean generateWeeklyReport() throws SQLException {
        try {
            return reportService.createWeeklyReportFile(LocalDate.now());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}