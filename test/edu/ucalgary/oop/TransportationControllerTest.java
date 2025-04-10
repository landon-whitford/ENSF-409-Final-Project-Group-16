package edu.ucalgary.oop;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TransportationControllerTest {
    // Stub implementations to simulate dependencies
    private static class StubDataAccessManager extends DataAccessManager {
        private List<RideRequest> rideRequests = new ArrayList<>();
        private List<Driver> drivers = new ArrayList<>();
        private List<Vehicle> vehicles = new ArrayList<>();

        public StubDataAccessManager() throws SQLException {
            super(); // Call parent constructor
        }

        @Override
        public List<RideRequest> getAllRideRequests() throws SQLException {
            return new ArrayList<>(rideRequests);
        }

        @Override
        public RideRequest getRideRequestById(int id) throws SQLException {
            return rideRequests.stream()
                    .filter(r -> r.getRequestID() == id)
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public boolean updateRideStatus(int id, String status) throws SQLException {
            RideRequest request = getRideRequestById(id);
            if (request != null) {
                request.setStatus(status);
            }
            return true;
        }

        @Override
        public boolean updateRideRequest(RideRequest request) throws SQLException {
            // Find and update the existing request or add a new one
            rideRequests.removeIf(r -> r.getRequestID() == request.getRequestID());
            if (request.getRequestID() == 0) {
                request.setRequestID(rideRequests.size() + 1);
            }
            rideRequests.add(request);
            return true;
        }

        @Override
        public boolean addRideRequest(RideRequest request) throws SQLException {
            // Simulate adding a request by setting an ID and adding to list
            if (request.getRequestID() == 0) {
                request.setRequestID(rideRequests.size() + 1);
            }
            request.setStatus("Pending"); // Default status when added
            rideRequests.add(request);
            return true;
        }

        @Override
        public List<Driver> getAllDrivers() throws SQLException {
            return new ArrayList<>(drivers);
        }

        @Override
        public List<Vehicle> getAllVehicles() throws SQLException {
            return new ArrayList<>(vehicles);
        }

        @Override
        public boolean updateDriverAvailability(int id, boolean isAvailable) throws SQLException {
            for (Driver driver : drivers) {
                if (driver.getDriverID() == id) {
                    driver.setAvailable(isAvailable);
                    break;
                }
            }
            return true;
        }

        // Helper methods for test setup
        public void addDriver(Driver driver) {
            drivers.add(driver);
        }

        public void addVehicle(Vehicle vehicle) {
            vehicles.add(vehicle);
        }
    }

    private static class StubSchedulingService extends SchedulingService {
        private boolean scheduleRideRequestResult = true;

        public StubSchedulingService(DataAccessManager dataManager) {
            super(dataManager);
        }

        @Override
        public boolean scheduleRideRequest(RideRequest request) {
            if (scheduleRideRequestResult) {
                request.setStatus("Scheduled");
            } else {
                request.setStatus("Pending");
            }
            return scheduleRideRequestResult;
        }

        public void setScheduleRideRequestResult(boolean result) {
            scheduleRideRequestResult = result;
        }
    }

    private static class StubReportGeneratorService extends ReportGeneratorService {
        private boolean dailyScheduleGenerated = false;
        private boolean weeklyReportGenerated = false;

        public StubReportGeneratorService(DataAccessManager dataManager) {
            super(dataManager);
        }

        @Override
        public boolean createDailyScheduleFile(LocalDate date) throws SQLException {
            dailyScheduleGenerated = true;
            return true;
        }

        @Override
        public boolean createWeeklyReportFile(LocalDate date) throws SQLException {
            weeklyReportGenerated = true;
            return true;
        }

        public boolean wasDailyScheduleGenerated() {
            return dailyScheduleGenerated;
        }

        public boolean wasWeeklyReportGenerated() {
            return weeklyReportGenerated;
        }
    }

    private StubDataAccessManager stubDataManager;
    private StubSchedulingService stubSchedulingService;
    private StubReportGeneratorService stubReportService;
    private TransportationController controller;

    @Before
    public void setUp() throws SQLException {
        // Initialize stub dependencies
        stubDataManager = new StubDataAccessManager();
        stubSchedulingService = new StubSchedulingService(stubDataManager);
        stubReportService = new StubReportGeneratorService(stubDataManager);

        // Create controller with stub dependencies
        controller = new TransportationController(
                stubDataManager,
                stubSchedulingService,
                stubReportService
        );
    }

    @Test
    public void testViewAllRideRequests() throws SQLException {
        // Prepare test data
        RideRequest request = new RideRequest();
        request.setRequestID(1);
        request.setClientName("Test Client");
        stubDataManager.addRideRequest(request);

        // Execute
        List<RideRequest> requests = controller.viewAllRideRequests();

        // Verify
        assertThat(requests.size(), is(1));
        assertThat(requests.get(0).getClientName(), is("Test Client"));
    }

    @Test
    public void testAddRideRequest_Scheduled() throws SQLException {
        // Prepare test data
        RideRequest request = new RideRequest();
        request.setClientName("Test Client");
        request.setPickUpLocation("123 Test St");
        request.setDropOffLocation("456 Test Ave");
        request.setPassengerCount(2);
        request.setRequestDate(LocalDate.now());
        request.setPickupTime(LocalTime.now());

        // Ensure scheduling service will return true
        stubSchedulingService.setScheduleRideRequestResult(true);

        // Execute
        boolean result = controller.addRideRequest(request);

        // Verify
        assertTrue("Ride request should be added successfully", result);
        assertThat(request.getStatus(), is("Scheduled"));
    }

    @Test
    public void testAddRideRequest_Pending() throws SQLException {
        // Prepare test data
        RideRequest request = new RideRequest();
        request.setClientName("Test Client");
        request.setPickUpLocation("123 Test St");
        request.setDropOffLocation("456 Test Ave");
        request.setPassengerCount(2);
        request.setRequestDate(LocalDate.now());
        request.setPickupTime(LocalTime.now());

        // Ensure scheduling service will return false
        stubSchedulingService.setScheduleRideRequestResult(false);

        // Execute
        boolean result = controller.addRideRequest(request);

        // Verify
        assertTrue("Ride request should be added", result);
        assertThat(request.getStatus(), is("Pending"));
    }

    @Test
    public void testCancelRideRequest() throws SQLException {
        // Prepare test data
        RideRequest request = new RideRequest();
        request.setRequestID(1);
        request.setStatus("Scheduled");
        stubDataManager.addRideRequest(request);

        // Execute
        boolean result = controller.cancelRideRequest(1);

        // Verify
        assertTrue("Ride request should be cancelled successfully", result);
        assertThat(request.getStatus(), is("Cancelled"));
    }

    @Test
    public void testModifyDriverAvailability() throws SQLException {
        // Prepare test data
        Driver driver = new Driver();
        driver.setDriverID(1);
        driver.setAvailable(true);
        stubDataManager.addDriver(driver);

        // Execute
        boolean result = controller.modifyDriverAvailability(1, false);

        // Verify
        assertTrue("Driver availability should be modified", result);
        assertFalse("Driver should no longer be available", driver.isAvailable());
    }

    @Test
    public void testGenerateDailySchedule() throws SQLException {
        // Prepare
        LocalDate testDate = LocalDate.now();

        // Execute
        boolean result = controller.generateDailySchedule(testDate);

        // Verify
        assertTrue("Daily schedule should be generated", result);
        assertTrue("Daily schedule file should be created",
                stubReportService.wasDailyScheduleGenerated());
    }

    @Test
    public void testGenerateWeeklyReport() throws SQLException {
        // Execute
        controller.generateWeeklyReport();

        // Verify
        assertTrue("Weekly report should be generated",
                stubReportService.wasWeeklyReportGenerated());
    }
}