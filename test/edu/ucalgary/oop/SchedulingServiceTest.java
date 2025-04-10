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

public class SchedulingServiceTest {

    private TestDataManager testDataManager;
    private SchedulingService schedulingService;
    private RideRequest testRequest;

    @Before
    public void setUp() throws SQLException {
        // Create test components
        testDataManager = new TestDataManager();

        // Create scheduling service with test data manager
        schedulingService = new SchedulingService(testDataManager);

        // Set up test ride request
        testRequest = new RideRequest();
        testRequest.setRequestID(1);
        testRequest.setClientName("Test Client");
        testRequest.setPickUpLocation("Test Pickup");
        testRequest.setDropOffLocation("Test Dropoff");
        testRequest.setPassengerCount(2);
        testRequest.setSpecialRequirements("Wheelchair");
        testRequest.setRequestDate(LocalDate.now());
        testRequest.setPickupTime(LocalTime.of(10, 0));
        testRequest.setStatus("Pending");
    }

    @Test
    public void testScheduleRideRequest_Available() throws SQLException {
        // Set up available drivers and vehicles
        testDataManager.addTestDriver(true);  // Available driver
        testDataManager.addTestVehicle(true); // Wheelchair accessible vehicle

        // Test scheduling
        boolean result = schedulingService.scheduleRideRequest(testRequest);

        // Verify results
        assertTrue("Scheduling should succeed when resources are available", result);
        assertThat(testRequest.getStatus(), is("Scheduled"));
        assertThat(testDataManager.getScheduleCount(), is(1));
    }

    @Test
    public void testScheduleRideRequest_NoDriver() throws SQLException {
        // Set up with no drivers but with vehicles
        testDataManager.addTestVehicle(true);

        // Test scheduling
        boolean result = schedulingService.scheduleRideRequest(testRequest);

        // Verify results
        assertFalse("Scheduling should fail when no drivers are available", result);
        assertThat(testRequest.getStatus(), is("Pending"));
        assertThat(testDataManager.getScheduleCount(), is(0));
    }

    @Test
    public void testScheduleRideRequest_NoSuitableVehicle() throws SQLException {
        // Set up with drivers but no suitable vehicles
        testDataManager.addTestDriver(true);
        testDataManager.addTestVehicle(false); // Not wheelchair accessible

        // Test scheduling
        boolean result = schedulingService.scheduleRideRequest(testRequest);

        // Verify results
        assertFalse("Scheduling should fail when no suitable vehicles are available", result);
        assertThat(testRequest.getStatus(), is("Pending"));
        assertThat(testDataManager.getScheduleCount(), is(0));
    }

    @Test
    public void testRescheduleRidesForDriver() throws SQLException {
        // Set up with a driver and vehicle
        Driver testDriver = testDataManager.addTestDriver(true);
        testDataManager.addTestVehicle(true);

        // Add the test request to the test data manager's collection
        // This ensures getAllRideRequests() will return it
        testDataManager.addTestRequest(testRequest);

        // Schedule a ride - this will add a schedule entry
        schedulingService.scheduleRideRequest(testRequest);
        assertThat(testRequest.getStatus(), is("Scheduled"));

        // Make the driver unavailable and test rescheduling
        testDriver.setAvailable(false);
        schedulingService.rescheduleRidesForDriver(testDriver.getDriverID());

        // Since there are no other drivers, the ride should be set to Pending
        assertThat(testRequest.getStatus(), is("Pending"));
    }

    /**
     * Test implementation of DataAccessManager for testing SchedulingService
     */
    private class TestDataManager extends DataAccessManager {
        private int driverIdCounter = 1;
        private int vehicleIdCounter = 1;
        private int scheduleIdCounter = 1;

        private List<Driver> drivers = new ArrayList<>();
        private List<Vehicle> vehicles = new ArrayList<>();
        private List<Schedule> schedules = new ArrayList<>();
        private List<RideRequest> requests = new ArrayList<>();

        public void addTestRequest(RideRequest request) {
            requests.add(request);
        }

        public TestDataManager() throws SQLException {
            super(); // Call the parent constructor
            requests.add(testRequest);
        }

        @Override
        public void connect() throws SQLException {
            // Do nothing in test implementation
        }

        public Driver addTestDriver(boolean isAvailable) {
            Driver driver = new Driver();
            driver.setDriverID(driverIdCounter++);
            driver.setName("Test Driver " + driver.getDriverID());
            driver.setPhoneNumber("555-000" + driver.getDriverID());
            driver.setLicenseNumber("D-TEST-" + driver.getDriverID());
            driver.setAvailable(isAvailable);
            drivers.add(driver);
            return driver;
        }

        public Vehicle addTestVehicle(boolean isWheelchairAccessible) {
            Vehicle vehicle = new Vehicle();
            vehicle.setVehicleID(vehicleIdCounter++);
            vehicle.setLicensePlate("TEST-" + vehicle.getVehicleID());
            vehicle.setCapacity(4);
            vehicle.setWheelchairAccessible(isWheelchairAccessible);
            vehicle.setCurrentLocation("Test Location");
            vehicle.setMaintenanceDueDate(LocalDate.now().plusMonths(3));
            vehicles.add(vehicle);
            return vehicle;
        }

        public int getScheduleCount() {
            return schedules.size();
        }

        @Override
        public List<Driver> getAvailableDrivers(LocalDate date, LocalTime startTime, LocalTime endTime) throws SQLException {
            return drivers.stream()
                    .filter(driver -> driver.isAvailable())
                    .collect(java.util.stream.Collectors.toList());
        }

        @Override
        public List<Vehicle> getAvailableVehicles(LocalDate date, LocalTime startTime, LocalTime endTime, boolean needsWheelchair, int passengerCount) throws SQLException {
            return vehicles.stream()
                    .filter(vehicle -> !needsWheelchair || vehicle.isWheelchairAccessible())
                    .filter(vehicle -> vehicle.getCapacity() >= passengerCount)
                    .collect(java.util.stream.Collectors.toList());
        }

        @Override
        public boolean addSchedule(Schedule schedule) throws SQLException {
            schedule.setScheduleID(scheduleIdCounter++);
            schedules.add(schedule);
            return true;
        }

        @Override
        public boolean updateRideRequest(RideRequest request) throws SQLException {
            // For testing purposes, we just return true
            return true;
        }

        @Override
        public List<Schedule> getAllSchedules() throws SQLException {
            return new ArrayList<>(schedules);
        }

        @Override
        public List<RideRequest> getAllRideRequests() throws SQLException {
            return new ArrayList<>(requests);
        }
    }
}