package edu.ucalgary.oop;

import java.util.Scanner;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.sql.SQLException;

/**
 * The CommandLineUI class implements the UserInterface interface to provide
 * a text-based command-line interface for the Calgary Access Network
 * Transportation System. This class handles all user interactions through
 * the console, including displaying menus, collecting input, and showing
 * output messages.
 *
 * @author Group 16
 * @version 1.7
 * @since 1.0
 */
public class CommandLineUI implements UserInterface {
    private Scanner scanner;
    private TransportationController controller;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Constructs a new CommandLineUI with the specified controller.
     * Initializes the scanner for reading user input.
     * @param controller the TransportationController that will process user requests
     */
    public CommandLineUI(TransportationController controller) {
        this.scanner = new Scanner(System.in);
        this.controller = controller;
    }

    /**
     * Displays the main menu and handles user interaction until the user chooses to exit.
     * This method is the main entry point for the UI.
     */
    @Override
    public void displayMenu() {
        boolean exit = false;

        while (!exit) {
            showMessage("\n===== Calgary Access Network Transportation System =====");
            showMessage("1. Ride Request Management");
            showMessage("2. Driver Management");
            showMessage("3. Vehicle Information");
            showMessage("4. Schedule Management");
            showMessage("5. Exit");
            showMessage("Enter your choice (1-5): ");

            String choice = getInput();

            try {
                switch (choice) {
                    case "1":
                        handleRideRequestMenu();
                        break;
                    case "2":
                        handleDriverMenu();
                        break;
                    case "3":
                        handleVehicleMenu();
                        break;
                    case "4":
                        handleScheduleMenu();
                        break;
                    case "5":
                        exit = true;
                        showMessage("Generating weekly report...");
                        controller.generateWeeklyReport();
                        showMessage("Thank you for using the CAN Transportation System!");
                        break;
                    default:
                        showError("Invalid choice. Please enter a number between 1 and 5.");
                }
            } catch (Exception e) {
                showError("An error occurred: " + e.getMessage());
            }
        }
    }

    /**
     * Displays the ride request management submenu and handles related user interactions.
     * @throws SQLException if a database error occurs
     */
    private void handleRideRequestMenu() throws SQLException {
        boolean back = false;

        while (!back) {
            showMessage("\n----- Ride Request Management -----");
            showMessage("1. View All Ride Requests");
            showMessage("2. Add New Ride Request");
            showMessage("3. Modify Ride Request");
            showMessage("4. Cancel Ride Request");
            showMessage("5. Complete Ride Request");
            showMessage("6. Back to Main Menu");
            showMessage("Enter your choice (1-6): ");

            String choice = getInput();

            switch (choice) {
                case "1":
                    viewAllRideRequests();
                    break;
                case "2":
                    addRideRequest();
                    break;
                case "3":
                    modifyRideRequest();
                    break;
                case "4":
                    cancelRideRequest();
                    break;
                case "5":
                    completeRideRequest();
                    break;
                case "6":
                    back = true;
                    break;
                default:
                    showError("Invalid choice. Please enter a number between 1 and 6.");
            }
        }
    }

    /**
     * Displays all ride requests in the system.
     * @throws SQLException if a database error occurs
     */
    private void viewAllRideRequests() throws SQLException {
        showMessage("\n--- All Ride Requests ---");
        List<RideRequest> requests = controller.viewAllRideRequests();

        if (requests.isEmpty()) {
            showMessage("No ride requests found.");
            return;
        }

        for (RideRequest request : requests) {
            showMessage("\nRequest ID: " + request.getRequestID());
            showMessage("Client: " + request.getClientName());
            showMessage("Pickup: " + request.getPickUpLocation());
            showMessage("Dropoff: " + request.getDropOffLocation());
            showMessage("Passengers: " + request.getPassengerCount());
            showMessage("Special Requirements: " +
                    (request.getSpecialRequirements() != null ? request.getSpecialRequirements() : "None"));
            showMessage("Date: " + request.getRequestDate().format(DATE_FORMATTER));
            showMessage("Time: " + request.getPickupTime().format(TIME_FORMATTER));
            showMessage("Status: " + request.getStatus());
            showMessage("------------------------");
        }
    }

    /**
     * Collects user input to create and add a new ride request.
     * @throws SQLException if a database error occurs
     */
    private void addRideRequest() throws SQLException {
        showMessage("\n--- Add New Ride Request ---");

        showMessage("Enter client name: ");
        String clientName = getInput();

        showMessage("Enter pickup location: ");
        String pickupLocation = getInput();

        showMessage("Enter dropoff location: ");
        String dropoffLocation = getInput();

        int passengerCount = getIntInput("Enter number of passengers: ");

        showMessage("Enter special requirements (or leave blank for none): ");
        String specialRequirements = getInput();
        if (specialRequirements.trim().isEmpty()) {
            specialRequirements = null;
        }

        LocalDate requestDate = getDateInput("Enter request date (YYYY-MM-DD): ");
        LocalTime pickupTime = getTimeInput("Enter pickup time (HH:MM): ");

        // Create the ride request object
        RideRequest request = new RideRequest();
        request.setClientName(clientName);
        request.setPickUpLocation(pickupLocation);
        request.setDropOffLocation(dropoffLocation);
        request.setPassengerCount(passengerCount);
        request.setSpecialRequirements(specialRequirements);
        request.setRequestDate(requestDate);
        request.setPickupTime(pickupTime);
        request.setStatus("Pending");

        // Add the request
        boolean success = controller.addRideRequest(request);

        if (success) {
            if ("Pending".equals(request.getStatus())) {
                showMessage("Ride request added successfully but could not be scheduled immediately. Status: Pending");
            } else {
                showMessage("Ride request added and scheduled successfully!");
            }
        } else {
            showError("Failed to add ride request.");
        }
    }

    /**
     * Allows the user to modify an existing ride request.
     * @throws SQLException if a database error occurs
     */
    private void modifyRideRequest() throws SQLException {
        showMessage("\n--- Modify Ride Request ---");

        int requestId = getIntInput("Enter Request ID to modify: ");

        // Get the ride request
        RideRequest request = controller.getRideRequestById(requestId);

        if (request == null) {
            showError("Ride request not found.");
            return;
        }

        if ("Cancelled".equals(request.getStatus()) || "Completed".equals(request.getStatus())) {
            showError("Cannot modify a cancelled or completed ride request.");
            return;
        }

        showMessage("\nCurrent Ride Request Details:");
        showMessage("Client: " + request.getClientName());
        showMessage("Pickup: " + request.getPickUpLocation());
        showMessage("Dropoff: " + request.getDropOffLocation());
        showMessage("Passengers: " + request.getPassengerCount());
        showMessage("Special Requirements: " +
                (request.getSpecialRequirements() != null ? request.getSpecialRequirements() : "None"));
        showMessage("Date: " + request.getRequestDate().format(DATE_FORMATTER));
        showMessage("Time: " + request.getPickupTime().format(TIME_FORMATTER));
        showMessage("Status: " + request.getStatus());

        // Modify dropoff, passenger count, or request time as per project requirements
        showMessage("\nWhich field would you like to modify?");
        showMessage("1. Dropoff Location");
        showMessage("2. Passenger Count");
        showMessage("3. Request Date/Time");
        showMessage("4. Cancel");
        showMessage("Enter your choice (1-4): ");

        String choice = getInput();

        switch (choice) {
            case "1":
                showMessage("Enter new dropoff location: ");
                String dropoffLocation = getInput();
                request.setDropOffLocation(dropoffLocation);
                break;
            case "2":
                int passengerCount = getIntInput("Enter new passenger count: ");
                request.setPassengerCount(passengerCount);
                break;
            case "3":
                LocalDate newDate = getDateInput("Enter new request date (YYYY-MM-DD): ");
                LocalTime newTime = getTimeInput("Enter new pickup time (HH:MM): ");
                request.setRequestDate(newDate);
                request.setPickupTime(newTime);
                break;
            case "4":
                showMessage("Modification cancelled.");
                return;
            default:
                showError("Invalid choice.");
                return;
        }

        // Update the request
        boolean success = controller.modifyRideRequest(request);

        if (success) {
            if ("Pending".equals(request.getStatus())) {
                showMessage("Ride request modified but could not be scheduled. Status: Pending");
            } else {
                showMessage("Ride request modified successfully!");
            }
        } else {
            showError("Failed to modify ride request.");
        }
    }

    /**
     * Allows the user to cancel a ride request.
     * @throws SQLException if a database error occurs
     */
    private void cancelRideRequest() throws SQLException {
        showMessage("\n--- Cancel Ride Request ---");

        int requestId = getIntInput("Enter Request ID to cancel: ");

        boolean success = controller.cancelRideRequest(requestId);

        if (success) {
            showMessage("Ride request cancelled successfully.");
        } else {
            showError("Failed to cancel ride request. It may not exist or is already completed/cancelled.");
        }
    }

    /**
     * Allows the user to mark a ride request as completed.
     * @throws SQLException if a database error occurs
     */
    private void completeRideRequest() throws SQLException {
        showMessage("\n--- Complete Ride Request ---");

        int requestId = getIntInput("Enter Request ID to mark as completed: ");

        boolean success = controller.completeRideRequest(requestId);

        if (success) {
            showMessage("Ride request marked as completed successfully.");
        } else {
            showError("Failed to complete ride request. It may not exist, is not scheduled, or is already completed/cancelled.");
        }
    }

    /**
     * Displays the driver management submenu and handles related user interactions.
     * @throws SQLException if a database error occurs
     */
    private void handleDriverMenu() throws SQLException {
        boolean back = false;

        while (!back) {
            showMessage("\n----- Driver Management -----");
            showMessage("1. View All Drivers");
            showMessage("2. Update Driver Availability");
            showMessage("3. Back to Main Menu");
            showMessage("Enter your choice (1-3): ");

            String choice = getInput();

            switch (choice) {
                case "1":
                    viewAllDrivers();
                    break;
                case "2":
                    updateDriverAvailability();
                    break;
                case "3":
                    back = true;
                    break;
                default:
                    showError("Invalid choice. Please enter a number between 1 and 3.");
            }
        }
    }

    /**
     * Displays all drivers in the system.
     * @throws SQLException if a database error occurs
     */
    private void viewAllDrivers() throws SQLException {
        showMessage("\n--- All Drivers ---");
        List<Driver> drivers = controller.viewAllDrivers();

        if (drivers.isEmpty()) {
            showMessage("No drivers found.");
            return;
        }

        for (Driver driver : drivers) {
            showMessage("\nDriver ID: " + driver.getDriverID());
            showMessage("Name: " + driver.getName());
            showMessage("Phone: " + driver.getPhoneNumber());
            showMessage("License: " + driver.getLicenseNumber());
            showMessage("Available: " + (driver.isAvailable() ? "Yes" : "No"));
            showMessage("------------------------");
        }
    }

    /**
     * Allows the user to update a driver's availability status.
     * @throws SQLException if a database error occurs
     */
    private void updateDriverAvailability() throws SQLException {
        showMessage("\n--- Update Driver Availability ---");

        int driverId = getIntInput("Enter Driver ID: ");

        // Get driver info
        Driver driver = controller.getDriverById(driverId);
        if (driver == null) {
            showError("Driver not found.");
            return;
        }

        showMessage("Current availability: " + (driver.isAvailable() ? "Available" : "Unavailable"));

        showMessage("Change to " + (!driver.isAvailable() ? "Available" : "Unavailable") + "? (y/n): ");
        String confirm = getInput().toLowerCase();

        if (confirm.equals("y") || confirm.equals("yes")) {
            boolean newAvailability = !driver.isAvailable();
            boolean success = controller.modifyDriverAvailability(driverId, newAvailability);

            if (success) {
                showMessage("Driver availability updated successfully to: " +
                        (newAvailability ? "Available" : "Unavailable"));
            } else {
                showError("Failed to update driver availability.");
            }
        } else {
            showMessage("Update cancelled.");
        }
    }

    /**
     * Displays the vehicle information menu and shows all vehicles.
     * @throws SQLException if a database error occurs
     */
    private void handleVehicleMenu() throws SQLException {
        showMessage("\n----- Vehicle Information -----");
        viewAllVehicles();
    }

    /**
     * Displays all vehicles in the system.
     * @throws SQLException if a database error occurs
     */
    private void viewAllVehicles() throws SQLException {
        showMessage("\n--- All Vehicles ---");
        List<Vehicle> vehicles = controller.viewAllVehicles();

        if (vehicles.isEmpty()) {
            showMessage("No vehicles found.");
            return;
        }

        for (Vehicle vehicle : vehicles) {
            showMessage("\nVehicle ID: " + vehicle.getVehicleID());
            showMessage("License Plate: " + vehicle.getLicensePlate());
            showMessage("Capacity: " + vehicle.getCapacity() + " passengers");
            showMessage("Wheelchair Accessible: " + (vehicle.isWheelchairAccessible() ? "Yes" : "No"));
            showMessage("Current Location: " + vehicle.getCurrentLocation());
            showMessage("Maintenance Due: " + vehicle.getMaintenanceDueDate().format(DATE_FORMATTER));
            showMessage("------------------------");
        }
    }

    /**
     * Displays the schedule management submenu and handles related user interactions.
     * @throws SQLException if a database error occurs
     */
    private void handleScheduleMenu() throws SQLException {
        boolean back = false;

        while (!back) {
            showMessage("\n----- Schedule Management -----");
            showMessage("1. View All Schedules");
            showMessage("2. Generate Daily Schedule");
            showMessage("3. Back to Main Menu");
            showMessage("Enter your choice (1-3): ");

            String choice = getInput();

            switch (choice) {
                case "1":
                    viewAllSchedules();
                    break;
                case "2":
                    generateDailySchedule();
                    break;
                case "3":
                    back = true;
                    break;
                default:
                    showError("Invalid choice. Please enter a number between 1 and 3.");
            }
        }
    }

    /**
     * Displays all schedules in the system.
     * @throws SQLException if a database error occurs
     */
    private void viewAllSchedules() throws SQLException {
        showMessage("\n--- All Schedules ---");
        List<Schedule> schedules = controller.viewAllSchedules();

        if (schedules.isEmpty()) {
            showMessage("No schedules found.");
            return;
        }

        for (Schedule schedule : schedules) {
            showMessage("\nSchedule ID: " + schedule.getScheduleID());
            showMessage("Date: " + schedule.getDate().format(DATE_FORMATTER));
            showMessage("Time: " + schedule.getTime().format(TIME_FORMATTER));
            showMessage("Driver: " + schedule.getDriver().getName() + " (ID: " + schedule.getDriver().getDriverID() + ")");
            showMessage("Vehicle: " + schedule.getVehicle().getLicensePlate() + " (ID: " + schedule.getVehicle().getVehicleID() + ")");
            showMessage("Client: " + schedule.getRideRequest().getClientName());
            showMessage("Pickup: " + schedule.getRideRequest().getPickUpLocation());
            showMessage("Dropoff: " + schedule.getRideRequest().getDropOffLocation());
            showMessage("------------------------");
        }
    }

    /**
     * Allows the user to generate a daily schedule file for a specific date.
     * @throws SQLException if a database error occurs
     */
    private void generateDailySchedule() throws SQLException {
        showMessage("\n--- Generate Daily Schedule ---");

        // Default to today, but allow user to specify a date
        showMessage("Enter date for daily schedule (YYYY-MM-DD) or press Enter for today: ");
        String dateStr = getInput();

        LocalDate date;
        if (dateStr.trim().isEmpty()) {
            date = LocalDate.now();
        } else {
            try {
                date = LocalDate.parse(dateStr, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                showError("Invalid date format. Please use YYYY-MM-DD.");
                return;
            }
        }

        boolean success = controller.generateDailySchedule(date);

        if (success) {
            showMessage("Daily schedule generated successfully for " + date.format(DATE_FORMATTER) + ".");
        } else {
            showError("Failed to generate daily schedule or no rides scheduled for the selected date.");
        }
    }

    /**
     * Gets input from the user via the console.
     * @return the user's input as a String
     */
    @Override
    public String getInput() {
        return scanner.nextLine();
    }

    /**
     * Displays a message to the user.
     * @param message the message to display
     */
    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays an error message to the user.
     * @param error the error message to display
     */
    @Override
    public void showError(String error) {
        System.err.println("ERROR: " + error);
    }

    /**
     * Helper method to get integer input from the user with validation.
     * Repeatedly prompts until valid input is received.
     * @param prompt the message to display to the user
     * @return the validated integer input
     */
    private int getIntInput(String prompt) {
        while (true) {
            showMessage(prompt);
            try {
                return Integer.parseInt(getInput());
            } catch (NumberFormatException e) {
                showError("Please enter a valid number.");
            }
        }
    }

    /**
     * Helper method to get date input from the user with validation.
     * Repeatedly prompts until valid input is received.
     * @param prompt the message to display to the user
     * @return the validated LocalDate input
     */
    private LocalDate getDateInput(String prompt) {
        while (true) {
            showMessage(prompt);
            try {
                return LocalDate.parse(getInput(), DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                showError("Invalid date format. Please use YYYY-MM-DD.");
            }
        }
    }

    /**
     * Helper method to get time input from the user with validation.
     * Repeatedly prompts until valid input is received.
     * @param prompt the message to display to the user
     * @return the validated LocalTime input
     */
    private LocalTime getTimeInput(String prompt) {
        while (true) {
            showMessage(prompt);
            try {
                return LocalTime.parse(getInput(), TIME_FORMATTER);
            } catch (DateTimeParseException e) {
                showError("Invalid time format. Please use HH:MM (24-hour format).");
            }
        }
    }
}