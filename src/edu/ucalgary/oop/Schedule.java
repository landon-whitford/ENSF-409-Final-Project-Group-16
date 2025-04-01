package edu.ucalgary.oop;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.sql.Time;

public class Schedule {
    int scheduleID;
    Driver driver;
    Vehicle vehicle;
    RideRequest rideRequest;
    LocalDate scheduledDate;
    LocalTime scheduledTime;

    public Schedule(int scheduleID, Driver driver, Vehicle vehicle, RideRequest rideRequest, LocalDate scheduledDate, LocalTime scheduledTime) {
        setScheduleID(scheduleID);
        setDriver(driver);
        setVehicle(vehicle);
        setRideRequest(rideRequest);
        setDate(scheduledDate);
        setTime(scheduledTime);
    }

    public Schedule(){}

    public void setScheduleID(int scheduleID) {
        this.scheduleID = scheduleID;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
    
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setRideRequest(RideRequest rideRequest) {
        this.rideRequest = rideRequest;
    }
        
    public void setDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }
        
    public void setTime(LocalTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public int getScheduleID() {
        return this.scheduleID;
    }

    public Driver getDriver() {
        return this.driver;
    }
    
    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public RideRequest getRideRequest() {
        return this.rideRequest;
    }
        
    public String getDate() {
        return this.scheduledDate.toString();
    }
        
    public String getTime() {
        return this.scheduledTime.toString();
    }
   
}