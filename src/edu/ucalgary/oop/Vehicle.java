package edu.ucalgary.oop;
import java.time.LocalDate;

public class Vehicle {
    private int vehicleID;
    private String licensePlate;
    private int capacity;
    private boolean isWheelchairAccessible;
    private String currentLocation;
    private LocalDate maintenanceDueDate;

    // Constructor
    public Vehicle(int vehicleID, String licensePlate, int capacity, boolean isWheelchairAccessible, String currentLocation, LocalDate maintenanceDueDate) {
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

    public boolean isWheelchairAccessible() {
        return isWheelchairAccessible;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public LocalDate getMaintenanceDueDate() {
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

    public void setWheelchairAccessible(boolean wheelchairAccessible) {
        isWheelchairAccessible = wheelchairAccessible;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void setMaintenanceDueDate(LocalDate maintenanceDueDate) {
        this.maintenanceDueDate = maintenanceDueDate;
    }
}
