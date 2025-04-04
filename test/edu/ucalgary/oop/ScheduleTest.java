package edu.ucalgary.oop;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleTest {

    private Schedule schedule;
    private Driver driver;
    private Vehicle vehicle;
    private RideRequest rideRequest;

    @Before
    public void setUp() {
        // Create a driver
        driver = new Driver();
        driver.setDriverID(1);
        driver.setName("Aisha Khan");
        driver.setPhoneNumber("555-1234");
        driver.setLicenseNumber("D1234567");
        driver.setAvailable(true);

        // Create a vehicle
        vehicle = new Vehicle();
        vehicle.setVehicleID(1);
        vehicle.setLicensePlate("ABC123");
        vehicle.setCapacity(4);
        vehicle.setWheelchairAccessible(true);
        vehicle.setCurrentLocation("Downtown Garage");
        vehicle.setMaintenanceDueDate(LocalDate.of(2025, 6, 15));

        // Create a ride request
        rideRequest = new RideRequest();
        rideRequest.setRequestID(1);
        rideRequest.setClientName("Priya Patel");
        rideRequest.setPickUpLocation("123 Main St");
        rideRequest.setDropOffLocation("456 Elm St");
        rideRequest.setPassengerCount(1);
        rideRequest.setSpecialRequirements("Wheelchair");
        rideRequest.setRequestDate(LocalDate.of(2025, 2, 26));
        rideRequest.setPickupTime(LocalTime.of(9, 0));
        rideRequest.setStatus("Scheduled");

        // Create a schedule
        schedule = new Schedule();
        schedule.setScheduleID(1);
        schedule.setDriver(driver);
        schedule.setVehicle(vehicle);
        schedule.setRideRequest(rideRequest);
        schedule.setDate(LocalDate.of(2025, 2, 26));
        schedule.setTime(LocalTime.of(9, 0));
    }

    @Test
    public void testGetScheduleID() {
        assertThat(schedule.getScheduleID(), is(1));
    }

    @Test
    public void testSetScheduleID() {
        schedule.setScheduleID(2);
        assertThat(schedule.getScheduleID(), is(2));
    }

    @Test
    public void testGetDriver() {
        assertThat(schedule.getDriver(), is(driver));
    }

    @Test
    public void testSetDriver() {
        Driver newDriver = new Driver();
        newDriver.setDriverID(2);
        newDriver.setName("Carlos Fernandez");

        schedule.setDriver(newDriver);
        assertThat(schedule.getDriver(), is(newDriver));
    }

    @Test
    public void testGetVehicle() {
        assertThat(schedule.getVehicle(), is(vehicle));
    }

    @Test
    public void testSetVehicle() {
        Vehicle newVehicle = new Vehicle();
        newVehicle.setVehicleID(2);
        newVehicle.setLicensePlate("XYZ456");

        schedule.setVehicle(newVehicle);
        assertThat(schedule.getVehicle(), is(newVehicle));
    }

    @Test
    public void testGetRideRequest() {
        assertThat(schedule.getRideRequest(), is(rideRequest));
    }

    @Test
    public void testSetRideRequest() {
        RideRequest newRequest = new RideRequest();
        newRequest.setRequestID(2);
        newRequest.setClientName("Neveah Miller");

        schedule.setRideRequest(newRequest);
        assertThat(schedule.getRideRequest(), is(newRequest));
    }

    @Test
    public void testGetScheduledDate() {
        assertThat(schedule.getDate(), is(LocalDate.of(2025, 2, 26)));
    }

    @Test
    public void testSetScheduledDate() {
        LocalDate newDate = LocalDate.of(2025, 2, 27);
        schedule.setDate(newDate);
        assertThat(schedule.getDate(), is(newDate));
    }

    @Test
    public void testGetScheduledTime() {
        assertThat(schedule.getTime(), is(LocalTime.of(9, 0)));
    }

    @Test
    public void testSetScheduledTime() {
        LocalTime newTime = LocalTime.of(10, 30);
        schedule.setTime(newTime);
        assertThat(schedule.getTime(), is(newTime));
    }

    @Test
    public void testDefaultConstructor() {
        Schedule newSchedule = new Schedule();
        assertNull(newSchedule.getDriver());
        assertNull(newSchedule.getVehicle());
        assertNull(newSchedule.getRideRequest());
        assertNull(newSchedule.getDate());
        assertNull(newSchedule.getTime());
    }

    @Test
    public void testFullConstructor() {
        // Create a new schedule using the constructor
        Schedule newSchedule = new Schedule(2, driver, vehicle, rideRequest,
                LocalDate.of(2025, 3, 1), LocalTime.of(14, 30));

        // Verify all properties were set correctly
        assertThat(newSchedule.getScheduleID(), is(2));
        assertThat(newSchedule.getDriver(), is(driver));
        assertThat(newSchedule.getVehicle(), is(vehicle));
        assertThat(newSchedule.getRideRequest(), is(rideRequest));
        assertThat(newSchedule.getDate(), is(LocalDate.of(2025, 3, 1)));
        assertThat(newSchedule.getTime(), is(LocalTime.of(14, 30)));
    }
}