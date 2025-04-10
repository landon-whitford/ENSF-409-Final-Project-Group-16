package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for CommandLineUI
 * This class tests the functionality of the CommandLineUI class
 * using mock objects and simulated user input
 */
public class CommandLineUITest {

    // Test objects
    private CommandLineUI ui;
    private MockTransportationController mockController;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @Before
    public void setUp() {
        // Create mock controller
        mockController = new MockTransportationController();

        // Set up output capture
        originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Create UI with mock controller
        ui = new CommandLineUI(mockController);
    }

    @After
    public void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
    }

    @Test
    public void testShowMessage() {
        // Test that showMessage outputs the correct text
        ui.showMessage("Test message");
        assertTrue(outputStream.toString().contains("Test message"));
    }

    @Test
    public void testShowError() {
        // Redirect err output for testing
        PrintStream originalErr = System.err;
        ByteArrayOutputStream errOutputStream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errOutputStream));

        // Test that showError outputs the correct text
        ui.showError("Test error");
        assertTrue(errOutputStream.toString().contains("ERROR: Test error"));

        // Restore original err
        System.setErr(originalErr);
    }

    @Test
    public void testGetInput() {
        // Simulate user input
        String input = "Test input\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Create new UI to use this input
        ui = new CommandLineUI(mockController);

        // Test that getInput returns the correct text
        assertEquals("Test input", ui.getInput());
    }

    @Test
    public void testViewAllRideRequests() throws SQLException {
        // Set up mock data
        List<RideRequest> mockRequests = createMockRideRequests();
        mockController.setMockRideRequests(mockRequests);

        // Set up input to select option to view all ride requests then return to main menu
        String input = "1\n1\n6\n5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Create new UI with this input
        ui = new CommandLineUI(mockController);

        // Run the menu
        ui.displayMenu();

        // Check output contains expected ride request info
        String output = outputStream.toString();
        assertTrue(output.contains("Client: Test Client 1"));
        assertTrue(output.contains("Client: Test Client 2"));
        assertTrue(output.contains("Pickup: Test Pickup 1"));
        assertTrue(output.contains("Dropoff: Test Dropoff 2"));
    }

    @Test
    public void testViewAllDrivers() throws SQLException {
        // Set up mock data
        List<Driver> mockDrivers = createMockDrivers();
        mockController.setMockDrivers(mockDrivers);

        // Set up input to select option to view all drivers then return to main menu
        String input = "2\n1\n3\n5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Create new UI with this input
        ui = new CommandLineUI(mockController);

        // Run the menu
        ui.displayMenu();

        // Check output contains expected driver info
        String output = outputStream.toString();
        assertTrue(output.contains("Name: Test Driver 1"));
        assertTrue(output.contains("Name: Test Driver 2"));
        assertTrue(output.contains("License: D1234"));
        assertTrue(output.contains("Available: Yes"));
    }

    @Test
    public void testViewAllVehicles() throws SQLException {
        // Set up mock data
        List<Vehicle> mockVehicles = createMockVehicles();
        mockController.setMockVehicles(mockVehicles);

        // Set up input to select option to view all vehicles then return to main menu
        String input = "3\n5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Create new UI with this input
        ui = new CommandLineUI(mockController);

        // Run the menu
        ui.displayMenu();

        // Check output contains expected vehicle info
        String output = outputStream.toString();
        assertTrue(output.contains("License Plate: ABC123"));
        assertTrue(output.contains("License Plate: XYZ456"));
        assertTrue(output.contains("Capacity: 4 passengers"));
        assertTrue(output.contains("Wheelchair Accessible: Yes"));
    }

    @Test
    public void testViewAllSchedules() throws SQLException {
        // Set up mock data
        List<Schedule> mockSchedules = createMockSchedules();
        mockController.setMockSchedules(mockSchedules);

        // Set up input to select option to view all schedules then return to main menu
        String input = "4\n1\n3\n5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Create new UI with this input
        ui = new CommandLineUI(mockController);

        // Run the menu
        ui.displayMenu();

        // Check output contains expected schedule info
        String output = outputStream.toString();
        assertTrue(output.contains("Driver: Test Driver 1"));
        assertTrue(output.contains("Vehicle: ABC123"));
        assertTrue(output.contains("Client: Test Client 1"));
    }

    // Helper methods to create mock data
    private List<RideRequest> createMockRideRequests() {
        List<RideRequest> requests = new ArrayList<>();

        RideRequest request1 = new RideRequest();
        request1.setRequestID(1);
        request1.setClientName("Test Client 1");
        request1.setPickUpLocation("Test Pickup 1");
        request1.setDropOffLocation("Test Dropoff 1");
        request1.setPassengerCount(2);
        request1.setSpecialRequirements("Wheelchair");
        request1.setRequestDate(LocalDate.now());
        request1.setPickupTime(java.time.LocalTime.of(9, 0));
        request1.setStatus("Scheduled");

        RideRequest request2 = new RideRequest();
        request2.setRequestID(2);
        request2.setClientName("Test Client 2");
        request2.setPickUpLocation("Test Pickup 2");
        request2.setDropOffLocation("Test Dropoff 2");
        request2.setPassengerCount(1);
        request2.setSpecialRequirements(null);
        request2.setRequestDate(LocalDate.now().plusDays(1));
        request2.setPickupTime(java.time.LocalTime.of(10, 30));
        request2.setStatus("Pending");

        requests.add(request1);
        requests.add(request2);

        return requests;
    }

    private List<Driver> createMockDrivers() {
        List<Driver> drivers = new ArrayList<>();

        Driver driver1 = new Driver();
        driver1.setDriverID(1);
        driver1.setName("Test Driver 1");
        driver1.setPhoneNumber("555-1234");
        driver1.setLicenseNumber("D1234");
        driver1.setAvailable(true);

        Driver driver2 = new Driver();
        driver2.setDriverID(2);
        driver2.setName("Test Driver 2");
        driver2.setPhoneNumber("555-5678");
        driver2.setLicenseNumber("D5678");
        driver2.setAvailable(false);

        drivers.add(driver1);
        drivers.add(driver2);

        return drivers;
    }

    private List<Vehicle> createMockVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();

        Vehicle vehicle1 = new Vehicle();
        vehicle1.setVehicleID(1);
        vehicle1.setLicensePlate("ABC123");
        vehicle1.setCapacity(4);
        vehicle1.setWheelchairAccessible(true);
        vehicle1.setCurrentLocation("Downtown Garage");
        vehicle1.setMaintenanceDueDate(LocalDate.now().plusMonths(3));

        Vehicle vehicle2 = new Vehicle();
        vehicle2.setVehicleID(2);
        vehicle2.setLicensePlate("XYZ456");
        vehicle2.setCapacity(6);
        vehicle2.setWheelchairAccessible(false);
        vehicle2.setCurrentLocation("North Depot");
        vehicle2.setMaintenanceDueDate(LocalDate.now().plusMonths(2));

        vehicles.add(vehicle1);
        vehicles.add(vehicle2);

        return vehicles;
    }

    private List<Schedule> createMockSchedules() {
        List<Schedule> schedules = new ArrayList<>();

        // Create mock objects for association
        Driver driver = createMockDrivers().get(0);
        Vehicle vehicle = createMockVehicles().get(0);
        RideRequest request = createMockRideRequests().get(0);

        Schedule schedule = new Schedule();
        schedule.setScheduleID(1);
        schedule.setDriver(driver);
        schedule.setVehicle(vehicle);
        schedule.setRideRequest(request);
        schedule.setDate(LocalDate.now());
        schedule.setTime(java.time.LocalTime.of(9, 0));

        schedules.add(schedule);

        return schedules;
    }

    /**
     * Mock implementation of TransportationController for testing
     */
    private class MockTransportationController extends TransportationController {
        private List<RideRequest> mockRideRequests = new ArrayList<>();
        private List<Driver> mockDrivers = new ArrayList<>();
        private List<Vehicle> mockVehicles = new ArrayList<>();
        private List<Schedule> mockSchedules = new ArrayList<>();

        public MockTransportationController() {
            super(null, null, null);
        }

        public void setMockRideRequests(List<RideRequest> requests) {
            this.mockRideRequests = requests;
        }

        public void setMockDrivers(List<Driver> drivers) {
            this.mockDrivers = drivers;
        }

        public void setMockVehicles(List<Vehicle> vehicles) {
            this.mockVehicles = vehicles;
        }

        public void setMockSchedules(List<Schedule> schedules) {
            this.mockSchedules = schedules;
        }

        @Override
        public List<RideRequest> viewAllRideRequests() {
            return mockRideRequests;
        }

        @Override
        public RideRequest getRideRequestById(int id) {
            for (RideRequest request : mockRideRequests) {
                if (request.getRequestID() == id) {
                    return request;
                }
            }
            return null;
        }

        @Override
        public boolean addRideRequest(RideRequest request) {
            request.setRequestID(mockRideRequests.size() + 1);
            mockRideRequests.add(request);
            return true;
        }

        @Override
        public boolean modifyRideRequest(RideRequest request) {
            for (int i = 0; i < mockRideRequests.size(); i++) {
                if (mockRideRequests.get(i).getRequestID() == request.getRequestID()) {
                    mockRideRequests.set(i, request);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean cancelRideRequest(int id) {
            for (RideRequest request : mockRideRequests) {
                if (request.getRequestID() == id) {
                    request.setStatus("Cancelled");
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean completeRideRequest(int id) {
            for (RideRequest request : mockRideRequests) {
                if (request.getRequestID() == id && "Scheduled".equals(request.getStatus())) {
                    request.setStatus("Completed");
                    return true;
                }
            }
            return false;
        }

        @Override
        public List<Driver> viewAllDrivers() {
            return mockDrivers;
        }

        @Override
        public Driver getDriverById(int id) {
            for (Driver driver : mockDrivers) {
                if (driver.getDriverID() == id) {
                    return driver;
                }
            }
            return null;
        }

        @Override
        public boolean modifyDriverAvailability(int id, boolean isAvailable) {
            for (Driver driver : mockDrivers) {
                if (driver.getDriverID() == id) {
                    driver.setAvailable(isAvailable);
                    return true;
                }
            }
            return false;
        }

        @Override
        public List<Vehicle> viewAllVehicles() {
            return mockVehicles;
        }

        @Override
        public List<Schedule> viewAllSchedules() {
            return mockSchedules;
        }

        @Override
        public boolean generateDailySchedule(LocalDate date) {
            return true;
        }

        @Override
        public boolean generateWeeklyReport() {
            return true;
        }
    }
}