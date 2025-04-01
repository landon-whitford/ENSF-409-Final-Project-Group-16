package edu.ucalgary.oop;

import org.junit.*;
import static org.junit.Assert.*;
import java.sql.*;

public class VehicleTest {
    Vehicle testCar;
    private int vehicleID = 1;
    private String licensePlate = "XYZ-1234";
    private int capacity = 2;
    private boolean isWheelchairAccessible = true;
    private String currentLocation = "Calgary";
    private Date maintenanceDueDate = new Date(100);

    @Before
    public void setUp() {
        testCar = new Vehicle(vehicleID, licensePlate, capacity, isWheelchairAccessible, currentLocation, maintenanceDueDate);
    }

    @Test
    public void testConstructor() {
        assertEquals("Constructor wrongly assigns vehicleID. ",
                vehicleID, testCar.getVehicleID());
        assertEquals("Constructor wrongly assigns licensePlate. ",
                licensePlate, testCar.getLicensePlate());
        assertEquals("Constructor wrongly assigns capacity. ",
                capacity, testCar.getCapacity());
        assertEquals("Constructor wrongly assigns isWheelChairAccessible. ",
                isWheelchairAccessible, testCar.getIsWheelchairAccessible());
        assertEquals("Constructor wrongly assigns currentLocation. ",
                currentLocation, testCar.getCurrentLocation());
        assertEquals("Constructor wrongly assigns maintenanceDueDate. ",
                maintenanceDueDate, testCar.getMaintenanceDueDate());
    }

    @Test
    public void testDefaultConstructor() {
        Vehicle testCar2 = new Vehicle();
        vehicleID = 2;
        testCar2.setVehicleID(vehicleID);

        assertEquals("Default Constructor wrongly assigns vehicleID. ",
                vehicleID, testCar2.getVehicleID());
    }

    @Test
    public void testSetters() {

        Vehicle testCar3 = new Vehicle();

        vehicleID = 3;
        licensePlate = "ABC-4567";
        capacity = 4;
        isWheelchairAccessible = false;
        currentLocation = "Edmonton";
        maintenanceDueDate = new Date(System.currentTimeMillis());

        testCar3.setVehicleID(vehicleID);
        testCar3.setLicensePlate(licensePlate);
        testCar3.setCapacity(capacity);
        testCar3.setIsWheelchairAccessible(isWheelchairAccessible);
        testCar3.setCurrentLocation(currentLocation);
        testCar3.setMaintenanceDueDate(maintenanceDueDate);

        assertEquals("setVehicleID wrongly assigns vehicleID. ",
                vehicleID, testCar3.getVehicleID());
        assertEquals("setLicensePlate wrongly assigns licensePlate. ",
                licensePlate, testCar3.getLicensePlate());
        assertEquals("setCapacity wrongly assigns capacity. ",
                capacity, testCar3.getCapacity());
        assertEquals("setIsWheelchairAccessible wrongly assigns isWheelChairAccessible. ",
                isWheelchairAccessible, testCar3.getIsWheelchairAccessible());
        assertEquals("setCurrentLocation wrongly assigns currentLocation. ",
                currentLocation, testCar3.getCurrentLocation());
        assertEquals("setMaintenanceDueDate wrongly assigns maintenanceDueDate. ",
                maintenanceDueDate, testCar3.getMaintenanceDueDate());

    }

}
