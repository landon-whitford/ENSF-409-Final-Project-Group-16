package edu.ucalgary.oop;

import java.util.Date;
import java.sql.Time;

public class Schedule {
    int scheduleID;
    Driver driver;
    Vehicle vehicle;
    RideRequest rideRequest;
    Date scheduledDate;
    Time scheduledTime;

    public Schedule(int scheduleID, Driver driver, Vehicle vehicle, RideRequest rideRequest, Date scheduledDate, Time scheduledTime) {
        setScheduleID(scheduleID);
        setDriver(driver);
        setVehicle(vehicle);
        setRideRequest(rideRequest);
        setDate(scheduledDate);
        setTime(scheduledTime);
    }

    public Schedule() {

    }

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
        
    public void setDate(Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }
        
    public void setTime(Time scheduledTime) {
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
        
    public Date getDate() {
        return this.scheduledDate;
    }
        
    public Time getTime() {
        return this.scheduledTime;
    }
   
}