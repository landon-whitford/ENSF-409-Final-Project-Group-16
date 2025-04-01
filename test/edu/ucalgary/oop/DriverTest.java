package edu.ucalgary.oop;

import org.junit.*;
import static org.junit.Assert.*;
import java.sql.*;

public class DriverTest {

    private Driver testDriver;
    private int driverID = 1;
    private String driverName = "Andrew";
    private  String phoneNumber = "123-456-7890";
    private String licenseNumber = "D9876543";
    private boolean isAvailable = true;

    @Before
    public void setUp() {
        testDriver = new Driver(driverName, phoneNumber, licenseNumber);
    }

    @Test
    public void testEssentialConstructor() {
        assertEquals("Constructor wrongly assigns driverName. ",
                driverName, testDriver.getName());
        assertEquals("Constructor wrongly assigns phoneNumber. ",
                phoneNumber, testDriver.getPhoneNumber());
        assertEquals("Constructor wrongly assigns licenseNumber. ",
                licenseNumber, testDriver.getLicenseNumber());
        assertEquals("Constructor isn't available by default. ",
                isAvailable, testDriver.isAvailable());
    }

    @Test
    public void testDefaultConstructor() {

        Driver testDriver2 = new Driver();

        assertEquals("Constructor isn't available by default. ",
                isAvailable, testDriver2.isAvailable());
    }

    @Test
    public void testConstructorWithoutID() {

        driverName = "Landon";
        phoneNumber = "587-888-5335";
        licenseNumber = "D1234567";
        isAvailable = false;
        Driver testDriver3 = new Driver(driverName, phoneNumber, licenseNumber, isAvailable);

        assertEquals("Constructor wrongly assigns driverName. ",
                driverName, testDriver3.getName());
        assertEquals("Constructor wrongly assigns phoneNumber. ",
                phoneNumber, testDriver3.getPhoneNumber());
        assertEquals("Constructor wrongly assigns licenseNumber. ",
                licenseNumber, testDriver3.getLicenseNumber());
        assertEquals("Constructor isn't available by default. ",
                isAvailable, testDriver3.isAvailable());
    }

    @Test
    public void testConstructorWithID() {

        driverID = 4;
        driverName = "Carl";
        phoneNumber = "456-123-7890";
        licenseNumber = "D5678901";
        isAvailable = true;
        Driver testDriver4 = new Driver(driverID, driverName, phoneNumber, licenseNumber, isAvailable);

        assertEquals("Constructor wrongly assigns driverName. ",
                driverID, testDriver4.getDriverID());
        assertEquals("Constructor wrongly assigns driverName. ",
                driverName, testDriver4.getName());
        assertEquals("Constructor wrongly assigns phoneNumber. ",
                phoneNumber, testDriver4.getPhoneNumber());
        assertEquals("Constructor wrongly assigns licenseNumber. ",
                licenseNumber, testDriver4.getLicenseNumber());
        assertEquals("Constructor isn't available by default. ",
                isAvailable, testDriver4.isAvailable());
    }

    @Test
    public void testSetters(){

        driverID = 5;
        driverName = "Aitor";
        phoneNumber = "111-222-3333";
        licenseNumber = "D999999";
        isAvailable = false;

        Driver testDriver5 = new Driver();

        testDriver5.setDriverID(driverID);
        testDriver5.setName(driverName);
        testDriver5.setPhoneNumber(phoneNumber);
        testDriver5.setLicenseNumber(licenseNumber);
        testDriver5.setAvailable(isAvailable);

        assertEquals("setDriverID wrongly assigns driverName. ",
                driverID, testDriver5.getDriverID());
        assertEquals("setName wrongly assigns driverName. ",
                driverName, testDriver5.getName());
        assertEquals("setPhoneNumber wrongly assigns phoneNumber. ",
                phoneNumber, testDriver5.getPhoneNumber());
        assertEquals("setLicenseNumber wrongly assigns licenseNumber. ",
                licenseNumber, testDriver5.getLicenseNumber());
        assertEquals("setAvailable() isn't available by default. ",
                isAvailable, testDriver5.isAvailable());

    }

}
