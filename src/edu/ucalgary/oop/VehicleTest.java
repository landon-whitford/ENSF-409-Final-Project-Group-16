package edu.ucalgary.oop;

import java.sql.*;

import org.junit.*;
import static org.junit.Assert.*;

public class VehicleTest {
    private int vehicleID = 1;
    private String licensePlate = "XYZ-1234";
    private int capacity = 2;
    private boolean isWheelchairAccessible = true;
    private String currentLocation = "Calgary";
    private Date maintenanceDueDate = new Date(100);

    @Before
    public void setUp() {
        Vehicle testCar = new Vehicle(vehicleID, licensePlate, capacity, isWheelchairAccessible, currentLocation, maintenanceDueDate);
    }

    @Test
    public void testConstructor() {
        assertArrayEquals("Constructor assigns values properly. ",
                licensePlate, testCar.getLicensePlate());
    }
}
