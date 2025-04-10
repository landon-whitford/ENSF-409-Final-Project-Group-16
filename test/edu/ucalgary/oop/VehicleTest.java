package edu.ucalgary.oop;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

public class VehicleTest {

    private Vehicle vehicle;

    @Before
    public void setUp() {
        vehicle = new Vehicle();
        vehicle.setVehicleID(1);
        vehicle.setLicensePlate("ABC123");
        vehicle.setCapacity(4);
        vehicle.setWheelchairAccessible(true);
        vehicle.setCurrentLocation("Downtown Garage");
        vehicle.setMaintenanceDueDate(LocalDate.of(2025, 6, 15));
    }

    @Test
    public void testGetVehicleID() {
        assertThat(vehicle.getVehicleID(), is(1));
    }

    @Test
    public void testSetVehicleID() {
        vehicle.setVehicleID(2);
        assertThat(vehicle.getVehicleID(), is(2));
    }

    @Test
    public void testGetLicensePlate() {
        assertThat(vehicle.getLicensePlate(), is("ABC123"));
    }

    @Test
    public void testSetLicensePlate() {
        vehicle.setLicensePlate("XYZ789");
        assertThat(vehicle.getLicensePlate(), is("XYZ789"));
    }

    @Test
    public void testGetCapacity() {
        assertThat(vehicle.getCapacity(), is(4));
    }

    @Test
    public void testSetCapacity() {
        vehicle.setCapacity(6);
        assertThat(vehicle.getCapacity(), is(6));
    }

    @Test
    public void testIsWheelchairAccessible() {
        assertThat(vehicle.isWheelchairAccessible(), is(true));
    }

    @Test
    public void testSetWheelchairAccessible() {
        vehicle.setWheelchairAccessible(false);
        assertThat(vehicle.isWheelchairAccessible(), is(false));
    }

    @Test
    public void testGetCurrentLocation() {
        assertThat(vehicle.getCurrentLocation(), is("Downtown Garage"));
    }

    @Test
    public void testSetCurrentLocation() {
        vehicle.setCurrentLocation("Northside Depot");
        assertThat(vehicle.getCurrentLocation(), is("Northside Depot"));
    }

    @Test
    public void testGetMaintenanceDueDate() {
        assertThat(vehicle.getMaintenanceDueDate(), is(LocalDate.of(2025, 6, 15)));
    }

    @Test
    public void testSetMaintenanceDueDate() {
        LocalDate newDate = LocalDate.of(2025, 7, 30);
        vehicle.setMaintenanceDueDate(newDate);
        assertThat(vehicle.getMaintenanceDueDate(), is(newDate));
    }
}