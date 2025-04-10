package edu.ucalgary.oop;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Before;
import org.junit.Test;

public class DriverTest {

    private Driver driver;

    @Before
    public void setUp() {
        driver = new Driver();
        driver.setDriverID(1);
        driver.setName("Aisha Khan");
        driver.setPhoneNumber("555-1234");
        driver.setLicenseNumber("D1234567");
        driver.setAvailable(true);
    }

    @Test
    public void testGetDriverID() {
        assertThat(driver.getDriverID(), is(1));
    }

    @Test
    public void testSetDriverID() {
        driver.setDriverID(2);
        assertThat(driver.getDriverID(), is(2));
    }

    @Test
    public void testGetName() {
        assertThat(driver.getName(), is("Aisha Khan"));
    }

    @Test
    public void testSetName() {
        driver.setName("Carlos Fernandez");
        assertThat(driver.getName(), is("Carlos Fernandez"));
    }

    @Test
    public void testGetPhoneNumber() {
        assertThat(driver.getPhoneNumber(), is("555-1234"));
    }

    @Test
    public void testSetPhoneNumber() {
        driver.setPhoneNumber("555-5678");
        assertThat(driver.getPhoneNumber(), is("555-5678"));
    }

    @Test
    public void testGetLicenseNumber() {
        assertThat(driver.getLicenseNumber(), is("D1234567"));
    }

    @Test
    public void testSetLicenseNumber() {
        driver.setLicenseNumber("D9876543");
        assertThat(driver.getLicenseNumber(), is("D9876543"));
    }

    @Test
    public void testIsAvailable() {
        assertThat(driver.isAvailable(), is(true));
    }

    @Test
    public void testSetAvailable() {
        driver.setAvailable(false);
        assertThat(driver.isAvailable(), is(false));
    }
}