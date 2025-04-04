package edu.ucalgary.oop;

public class Driver {
    private int driverID;
    private String driverName;
    private String phoneNumber;
    private String licenseNumber;
    private boolean isAvailable;


        // Default constructor
        public Driver() {}


        //getter methods
        public int getDriverID() {
            return driverID;
        }
        public String getName() {
            return driverName;
        }
        public String getPhoneNumber() {
            return phoneNumber;
        }
        public String getLicenseNumber() {
            return licenseNumber;
        }
        public boolean isAvailable() {
            return isAvailable;
        }

        // setter methods
        public void setDriverID(int driverID) {
            this.driverID = driverID;
        }
        public void setName(String name) {
            this.driverName = name;
        }
        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
        public void setLicenseNumber(String licenseNumber) {
            this.licenseNumber = licenseNumber;
        }
        public void setAvailable(boolean available) {
            isAvailable = available;
        }
    }

