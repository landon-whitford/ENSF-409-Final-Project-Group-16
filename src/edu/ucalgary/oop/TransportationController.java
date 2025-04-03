package edu.ucalgary.oop;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TransportationController {
    private DataAccessManager dataManager;
    private SchedulingService schedulingService;
    private ReportGeneratorService reportService;

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

    public List<RideRequest> viewAllRideRequests() throws SQLException {
        return dataManager.getAllRideRequests();
    }

    public RideRequest getRideRequestById(int id) throws SQLException {
        return dataManager.getRideRequestById(id);
    }

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

    public List<Driver> viewAllDrivers() throws SQLException {
        return dataManager.getAllDrivers();
    }

    public Driver getDriverById(int id) throws SQLException {
        return dataManager.getDriverById(id);
    }

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

    public List<Vehicle> viewAllVehicles() throws SQLException {
        return dataManager.getAllVehicles();
    }

    //------------------------------------------------------------
    // Schedule operations
    //------------------------------------------------------------

    public List<Schedule> viewAllSchedules() throws SQLException {
        return dataManager.getAllSchedules();
    }

    public boolean generateDailySchedule(LocalDate date) throws SQLException {
        try {
            return reportService.createDailyScheduleFile(date);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean generateWeeklyReport() throws SQLException {
        try {
            return reportService.createWeeklyReportFile(LocalDate.now());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}