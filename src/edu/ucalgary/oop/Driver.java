package edu.ucalgary.oop;

public class Driver {
    private int driverID;
    private String driverName;
    private String phoneNumber;
    private String licenseNumber;
    private boolean isAvailable;


        // Default constructor
        public Driver() {
            this.isAvailable = true; // Default to available
        }

        // Essential fields constructor
        public Driver(String name, String phoneNumber, String licenseNumber) {
            this.driverName = name;
            this.phoneNumber = phoneNumber;
            this.licenseNumber = licenseNumber;
            this.isAvailable = true; // Default to available
        }

        // Full constructor without ID (for new entries)
        public Driver(String name, String phoneNumber, String licenseNumber, boolean isAvailable) {
            this.driverName = name;
            this.phoneNumber = phoneNumber;
            this.licenseNumber = licenseNumber;
            this.isAvailable = isAvailable;
        }

        // Full constructor with ID (for database retrieval)
        public Driver(int driverID, String name, String phoneNumber, String licenseNumber, boolean isAvailable) {
            this.driverID = driverID;
            this.driverName = name;
            this.phoneNumber = phoneNumber;
            this.licenseNumber = licenseNumber;
            this.isAvailable = isAvailable;
        }

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

