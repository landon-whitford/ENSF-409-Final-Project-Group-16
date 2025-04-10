package edu.ucalgary.oop;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a ride request in the Accessible Transportation System.
 * This class contains all the information related to a client's request for transportation,
 * including client details, locations, special requirements, and status.
 *
 * @author Group 16
 * @version 1.2
 * @since 1.0
 */

public class RideRequest {

    // Private data Members
    private int requestID;
    private String clientName;
    private String pickUpLocation;
    private String dropOffLocation;
    private int passengerCount;
    private String specialRequirements;
    private LocalDate requestDate;
    private LocalTime pickupTime;
    private String status;


    public RideRequest(){}

    // Getter methods

    /**
     * Gets the unique identifier for this ride request.
     * @return The request ID
     */

    public int getRequestID() { return this.requestID; }

    /**
     * Gets the name of the client who requested the ride.
     * @return The client's name
     */

    public String getClientName() {return this.clientName;}

    /**
     * Gets the pickup location for this ride request.
     * @return The pickup location address
     */

    public String getPickUpLocation() {return this.pickUpLocation;}

    /**
     * Gets the dropoff location for this ride request.
     * @return The dropoff location address
     */

    public String getDropOffLocation() {return this.dropOffLocation;}

    /**
     * Gets the number of passengers for this ride request.
     * @return The number of passengers
     */

    public int getPassengerCount() { return this.passengerCount;}

    /**
     * Gets any special requirements for this ride request (e.g., wheelchair accessibility).
     * @return The special requirements, or null if none
     */

    public String getSpecialRequirements() {return this.specialRequirements;}

    /**
     * Gets the requested date for this ride.
     * @return The requested date
     */

    public LocalDate getRequestDate() { return this.requestDate; } //  returns Date

    /**
     * Gets the requested pickup time for this ride.
     * @return The requested pickup time
     */

    public LocalTime getPickupTime() { return this.pickupTime; }

    /**
     * Gets the current status of this ride request (e.g., "Pending", "Scheduled", "Cancelled", "Completed").
     * @return The current status
     */

    public String getStatus() {return this.status;}

    // Setter Methods
    /**
     * Sets the unique identifier for this ride request.
     * @param requestID The request ID to set
     */

    public void setRequestID(int requestID) { this.requestID = requestID; }

    /**
     * Sets the name of the client who requested the ride.
     * @param clientName The client's name to set
     */

    public void setClientName(String clientName) {this.clientName = clientName;}

    /**
     * Sets the pickup location for this ride request.
     * @param pickUpLocation The pickup location address to set
     */

    public void setPickUpLocation(String pickUpLocation) {this.pickUpLocation = pickUpLocation;}

    /**
     * Sets the dropoff location for this ride request.
     *
     * @param dropOffLocation The dropoff location address to set
     */

    public void setDropOffLocation(String dropOffLocation) {this.dropOffLocation = dropOffLocation;}

    /**
     * Sets the number of passengers for this ride request.
     * @param passengerCount The number of passengers to set
     */

    public void setPassengerCount(int passengerCount) {this.passengerCount = passengerCount;}

    /**
     * Sets any special requirements for this ride request (e.g., wheelchair accessibility).
     * @param specialRequirements The special requirements to set, or null if none
     */

    public void setSpecialRequirements(String specialRequirements) {this.specialRequirements = specialRequirements;}

    /**
     * Sets the requested date for this ride.
     * @param requestDate The requested date to set
     */

    public void setRequestDate(LocalDate requestDate) {this.requestDate = requestDate;}

    /**
     * Sets the requested pickup time for this ride.
     * @param pickupTime The requested pickup time to set
     */

    public void setPickupTime(LocalTime pickupTime) {this.pickupTime = pickupTime;}

    /**
     * Sets the current status of this ride request.
     * Valid values are "Pending", "Scheduled", "Cancelled", and "Completed".
     * @param status The status to set
     */

    public void setStatus(String status) {this.status = status;}
}
