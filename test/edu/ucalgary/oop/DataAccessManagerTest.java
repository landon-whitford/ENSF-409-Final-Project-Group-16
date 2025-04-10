package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DataAccessManagerTest {
    private DataAccessManager dataManager;

    @Before
    public void setUp() throws SQLException {
        dataManager = new DataAccessManager();
    }

    @Test
    public void testGetAllRideRequests() throws SQLException {
        List<RideRequest> requests = dataManager.getAllRideRequests();
        assertNotNull("Ride requests list should not be null", requests);
    }

    @Test
    public void testAddAndGetRideRequest() throws SQLException {
        RideRequest request = new RideRequest();
        request.setClientName("Test Client");
        request.setPickUpLocation("123 Test St");
        request.setDropOffLocation("456 Test Ave");
        request.setPassengerCount(2);
        request.setRequestDate(LocalDate.of(2025, 2, 26));
        request.setPickupTime(LocalTime.parse("10:00")); // Use string parsing
        request.setStatus("Pending");

        boolean added = dataManager.addRideRequest(request);
        assertTrue("Ride request should be added", added);

        RideRequest retrievedRequest = dataManager.getRideRequestById(request.getRequestID());
        assertNotNull("Retrieved ride request should not be null", retrievedRequest);
        assertThat(retrievedRequest.getClientName(), is("Test Client"));
    }

    @Test
    public void testUpdateRideStatus() throws SQLException {
        RideRequest request = new RideRequest();
        request.setClientName("Status Test Client");
        request.setPickUpLocation("789 Test St");
        request.setDropOffLocation("012 Test Ave");
        request.setPassengerCount(1);
        request.setRequestDate(LocalDate.of(2025, 2, 26));
        request.setPickupTime(LocalTime.parse("14:30")); // Use string parsing
        request.setStatus("Pending");

        dataManager.addRideRequest(request);

        boolean updated = dataManager.updateRideStatus(request.getRequestID(), "Completed");
        assertTrue("Ride request status should be updated", updated);

        RideRequest updatedRequest = dataManager.getRideRequestById(request.getRequestID());
        assertThat(updatedRequest.getStatus(), is("Completed"));
    }

    @Test
    public void testViewAllDrivers() throws SQLException {
        List<Driver> drivers = dataManager.getAllDrivers();
        assertNotNull("Drivers list should not be null", drivers);
        assertFalse("Drivers list should not be empty", drivers.isEmpty());
    }

    @Test
    public void testViewAllVehicles() throws SQLException {
        List<Vehicle> vehicles = dataManager.getAllVehicles();
        assertNotNull("Vehicles list should not be null", vehicles);
        assertFalse("Vehicles list should not be empty", vehicles.isEmpty());
    }

    @Test
    public void testUpdateDriverAvailability() throws SQLException {
        List<Driver> drivers = dataManager.getAllDrivers();
        assertFalse("Drivers list should not be empty", drivers.isEmpty());

        Driver driver = drivers.get(0);
        boolean originalAvailability = driver.isAvailable();

        boolean updated = dataManager.updateDriverAvailability(driver.getDriverID(), !originalAvailability);
        assertTrue("Driver availability should be updated", updated);

        Driver updatedDriver = dataManager.getDriverById(driver.getDriverID());
        assertThat(updatedDriver.isAvailable(), is(!originalAvailability));
    }
}