package edu.ucalgary.oop;

public class RideRequest {

    // Private data Members
    private int requestID;
    private String clientName;
    private String pickUpLocation;
    private String dropOffLocation;
    private int passengerCount;
    private String specialRequirements;
    private Date requestDate;
    private Time pickupTime;
    private String status;

    // Getter methods
    public int getRequestID() { return this.requestID; }

    public String getClientName() {return this.clientName;}

    public String getPickUpLocation() {return this.pickUpLocation;}

    public String getDropOffLocation() {return this.dropOffLocation;}

    public int getPassengerCount() { return this.passengerCount;}

    public String getSpecialRequirements() {return this.specialRequirements;}

    public Date getRequestDate() { return this.requestDate;}

    public Time getPickupTime() {return this.pickupTime;}

    public String getStatus() {return this.status};

    // Setter Methods
    public void setRequestID(int requestID) { this.requestID = requestID; }

    public void setClientName(String clientName) {this.clientName = clientName;}

    public void setPickUpLocation(String pickUpLocation) {this.pickUpLocation = pickUpLocation;}

    public void setDropOffLocation(String dropOffLocation) {this.dropOffLocation = dropOffLocation;}

    public void setPassengerCount(int passengerCount) {this.passengerCount = passengerCount;}

    public void setSpecialRequirements(String specialRequirements) {this.specialRequirements = specialRequirements;}

    public void setRequestDate(Date requestDate) {this.requestDate = requestDate;}

    public void setPickupTime(Time pickupTime) {this.pickupTime = pickupTime;}

    public void setStatus(String status) {this.status = status;}
}
