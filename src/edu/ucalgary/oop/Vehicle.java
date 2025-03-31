package edu.ucalgary.oop;
import java.sql.*;

public class Vehicle {
    private int vehicleID;
    private String licensePlate;
    private int capacity;
    private boolean isWheelchairAccessible;
    private String currentLocation;
    private Date maintenanceDueDate;

    // Constructor
    public Vehicle(int vehicleID, String licensePlate, int capacity, boolean isWheelchairAccessible, String currentLocation, Date maintenanceDueDate) {
        this.vehicleID = vehicleID;
        this.licensePlate = licensePlate;
        this.capacity = capacity;
        this.isWheelchairAccessible = isWheelchairAccessible;
        this.currentLocation = currentLocation;
        this.maintenanceDueDate = maintenanceDueDate;
    }

    public Vehicle(){}

    // Getters
    public int getVehicleID() {
        return vehicleID;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean getIsWheelchairAccessible() {
        return isWheelchairAccessible;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public Date getMaintenanceDueDate() {
        return maintenanceDueDate;
    }

    // Setters
    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setIsWheelchairAccessible(boolean wheelchairAccessible) {
        isWheelchairAccessible = wheelchairAccessible;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void setMaintenanceDueDate(Date maintenanceDueDate) {
        this.maintenanceDueDate = maintenanceDueDate;
    }
}
