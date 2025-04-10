package edu.ucalgary.oop;
import java.time.LocalDate;

/**
 * Represents a vehicle in the Accessible Transportation System.
 * This class stores vehicle information including identification, capacity,
 * accessibility features, and maintenance details.
 *
 * @author Group 16
 * @version 1.1
 * @since 1.0
 */


public class Vehicle {
    private int vehicleID;
    private String licensePlate;
    private int capacity;
    private boolean isWheelchairAccessible;
    private String currentLocation;
    private LocalDate maintenanceDueDate;


    public Vehicle(){}

    // Getters

    /**
     * Gets the unique identifier for this vehicle.
     * @return The vehicle ID
     */

    public int getVehicleID() {
        return vehicleID;
    }

    /**
     * Gets the license plate of this vehicle.
     * @return The vehicle's license plate
     */

    public String getLicensePlate() {
        return licensePlate;
    }

    /**
     * Gets the passenger capacity of this vehicle.
     * @return The vehicle's passenger capacity
     */

    public int getCapacity() {
        return capacity;
    }

    /**
     * Checks if this vehicle is wheelchair accessible.
     * @return true if the vehicle is wheelchair accessible, false otherwise
     */

    public boolean isWheelchairAccessible() {
        return isWheelchairAccessible;
    }

    /**
     * Gets the current location of this vehicle.
     * @return The vehicle's current location
     */

    public String getCurrentLocation() {
        return currentLocation;
    }

    /**
     * Gets the maintenance due date for this vehicle.
     * @return The date when maintenance is due
     */

    public LocalDate getMaintenanceDueDate() {
        return maintenanceDueDate;
    }

    // Setters

    /**
     * Sets the unique identifier for this vehicle.
     * @param vehicleID The vehicle ID to set
     */

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    /**
     * Sets the license plate of this vehicle.
     * @param licensePlate The vehicle's license plate to set
     */

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    /**
     * Sets the passenger capacity of this vehicle.
     * @param capacity The vehicle's passenger capacity to set
     */

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Sets whether this vehicle is wheelchair accessible.
     * @param wheelchairAccessible true to set the vehicle as wheelchair accessible, false otherwise
     */

    public void setWheelchairAccessible(boolean wheelchairAccessible) {
        isWheelchairAccessible = wheelchairAccessible;
    }

    /**
     * Sets the current location of this vehicle.
     * @param currentLocation The vehicle's current location to set
     */

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    /**
     * Sets the maintenance due date for this vehicle.
     * @param maintenanceDueDate The date when maintenance is due to set
     */

    public void setMaintenanceDueDate(LocalDate maintenanceDueDate) {
        this.maintenanceDueDate = maintenanceDueDate;
    }
}
