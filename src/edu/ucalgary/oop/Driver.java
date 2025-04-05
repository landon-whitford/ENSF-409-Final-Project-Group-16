package edu.ucalgary.oop;

/**
 * Represents a driver in the Accessible Transportation System.
 * This class stores driver information including personal details, credentials,
 * and availability status.
 *
 * @author Group 16
 * @version 1.2
 * @since 1.0
 */

public class Driver {

    private int driverID;
    private String driverName;
    private String phoneNumber;
    private String licenseNumber;
    private boolean isAvailable;

    public Driver() {}
        //getter methods

    /**
     * Gets the unique identifier for this driver.
     * @return The driver ID
     */

    public int getDriverID() {
            return driverID;
        }

    /**
     * Gets the name of this driver.
     * @return The driver's name
     */

    public String getName() {
            return driverName;
        }

    /**
     * Gets the phone number of this driver.
     *
     * @return The driver's phone number
     */

    public String getPhoneNumber() {
            return phoneNumber;
        }

    /**
     * Gets the license number of this driver.
     * @return The driver's license number
     */

    public String getLicenseNumber() {
            return licenseNumber;
        }

    /**
     * Checks if this driver is currently available for assignments.
     * @return true if the driver is available, false otherwise
     */

    public boolean isAvailable() {
            return isAvailable;
        }

        // setter methods

    /**
     * Sets the unique identifier for this driver.
     * @param driverID The driver ID to set
     */

    public void setDriverID(int driverID) {
            this.driverID = driverID;
        }

    /**
     * Sets the name of this driver.
     * @param name The driver's name to set
     */

    public void setName(String name) {
            this.driverName = name;
        }

    /**
     * Sets the phone number of this driver.
     *
     * @param phoneNumber The driver's phone number to set
     */

    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}

    /**
     * Sets the license number of this driver.
     *
     * @param licenseNumber The driver's license number to set
     */

    public void setLicenseNumber(String licenseNumber) {
            this.licenseNumber = licenseNumber;
        }

    /**
     * Sets the availability status of this driver.
     *
     * @param available true to set the driver as available, false otherwise
     */

    public void setAvailable(boolean available) {
            isAvailable = available;
        }
    }

