package edu.ucalgary.oop;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TransportationController {
    private final DataAccessManager dataManager;
    private final SchedulingService schedulingService;
    private final ReportGeneratorService reportService;

    public TransportationController() throws SQLException {
        this.dataManager = new DataAccessManager();
        this.schedulingService = new SchedulingService(dataManager);
        this.reportService = new ReportGeneratorService(dataManager);
    }

    public List<RideRequest> viewAllRideRequests() throws SQLException {
        return dataManager.getAllRideRequests();
    }

    public boolean addRideRequest(RideRequest request) throws SQLException {
        boolean added = dataManager.addRideRequest(request);
        if (added) {
            boolean scheduled = schedulingService.scheduleRideRequest(request);
            if (!scheduled) {
                System.out.println("Ride request added but not scheduled (status: Pending).");
            }
        }
        return added;
    }

    public boolean modifyRideRequest(RideRequest request) throws SQLException {
        boolean updated = dataManager.updateRideRequest(request);
        if (updated) {
            boolean rescheduled = schedulingService.scheduleRideRequest(request);
            if (!rescheduled) {
                System.out.println("Modification saved, but the request could not be rescheduled.");
            }
        }
        return updated;
    }

    public boolean cancelRideRequest(int id) throws SQLException {
        return dataManager.updateRideStatus(id, "Cancelled");
    }

    public boolean completeRideRequest(int id) throws SQLException {
        return dataManager.updateRideStatus(id, "Completed");
    }

    public List<Driver> viewAllDrivers() throws SQLException {
        return dataManager.getAllDrivers();
    }

    public boolean modifyDriverAvailability(int id, boolean isAvailable) throws SQLException {
        boolean updated = dataManager.updateDriverAvailability(id, isAvailable);
        if (updated && !isAvailable) {
            schedulingService.rescheduleRidesForDriver(id);
        }
        return updated;
    }

    public List<Vehicle> viewAllVehicles() throws SQLException {
        return dataManager.getAllVehicles();
    }

    public List<Schedule> viewAllSchedules() throws SQLException {
        return dataManager.getAllSchedules();
    }

    public void generateDailySchedule(LocalDate date) {
        reportService.createDailyScheduleFile(date);
    }

    public void generateWeeklyReport() {
        reportService.createWeeklyReportFile(LocalDate.now().with(java.time.DayOfWeek.MONDAY));
    }
}
