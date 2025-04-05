package edu.ucalgary.oop;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a scheduled ride in the Accessible Transportation System.
 * This class links a ride request with an assigned driver, vehicle, date, and time.
 *
 * @author Group 16
 * @version 1.1
 * @since 1.0
 */

public class Schedule {
    int scheduleID;
    Driver driver;
    Vehicle vehicle;
    RideRequest rideRequest;
    LocalDate scheduledDate;
    LocalTime scheduledTime;

    /**
     * Constructs a new Schedule with the specified details.
     *
     * @param scheduleID The unique identifier for this schedule
     * @param driver The driver assigned to this schedule
     * @param vehicle The vehicle assigned to this schedule
     * @param rideRequest The ride request associated with this schedule
     * @param scheduledDate The date of the scheduled ride
     * @param scheduledTime The time of the scheduled ride
     */

    public Schedule(int scheduleID, Driver driver, Vehicle vehicle, RideRequest rideRequest, LocalDate scheduledDate, LocalTime scheduledTime) {
        setScheduleID(scheduleID);
        setDriver(driver);
        setVehicle(vehicle);
        setRideRequest(rideRequest);
        setDate(scheduledDate);
        setTime(scheduledTime);
    }

    public Schedule() {}

    /**
     * Sets the unique identifier for this schedule.
     *
     * @param scheduleID The schedule ID to set
     */

    public void setScheduleID(int scheduleID) {
        this.scheduleID = scheduleID;
    }

    /**
     * Sets the driver assigned to this schedule.
     *
     * @param driver The driver to assign
     */

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    /**
     * Sets the vehicle assigned to this schedule.
     *
     * @param vehicle The vehicle to assign
     */

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    /**
     * Sets the ride request associated with this schedule.
     *
     * @param rideRequest The ride request to associate
     */

    public void setRideRequest(RideRequest rideRequest) {
        this.rideRequest = rideRequest;
    }

    /**
     * Sets the date of the scheduled ride.
     *
     * @param scheduledDate The scheduled date to set
     */

    public void setDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    /**
     * Sets the time of the scheduled ride.
     *
     * @param scheduledTime The scheduled time to set
     */

    public void setTime(LocalTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    /**
     * Gets the unique identifier for this schedule.
     *
     * @return The schedule ID
     */

    public int getScheduleID() {
        return this.scheduleID;
    }

    /**
     * Gets the driver assigned to this schedule.
     *
     * @return The assigned driver
     */

    public Driver getDriver() {
        return this.driver;
    }

    /**
     * Gets the vehicle assigned to this schedule.
     *
     * @return The assigned vehicle
     */

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    /**
     * Gets the ride request associated with this schedule.
     *
     * @return The associated ride request
     */

    public RideRequest getRideRequest() {
        return this.rideRequest;
    }

    /**
     * Gets the date of the scheduled ride.
     *
     * @return The scheduled date
     */

    public LocalDate getDate() {
        return this.scheduledDate;
    }

    /**
     * Gets the time of the scheduled ride.
     *
     * @return The scheduled time
     */

    public LocalTime getTime() {
        return this.scheduledTime;
    }
   
}