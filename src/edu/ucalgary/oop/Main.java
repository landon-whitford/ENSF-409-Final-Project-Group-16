package edu.ucalgary.oop;

import java.sql.SQLException;

/**
 * Main entry point for the Calgary Access Network Transportation System.
 * This class initializes all necessary components and starts the application.
 * The application allows management of transportation services for individuals
 * with disabilities or mobility challenges, including ride requests, vehicle
 * management, driver scheduling, and report generation.
 *
 * @author Group 16
 * @version 1.1
 * @since 1.0
 */
public class Main {

    /**
     * The main method that serves as the entry point for the application.
     * Initializes the database connection, services, controller, and user interface.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            // Initialize application components
            DataAccessManager dataManager = new DataAccessManager();
            SchedulingService schedulingService = new SchedulingService(dataManager);
            ReportGeneratorService reportService = new ReportGeneratorService(dataManager);

            // Initialize controller with all required services
            TransportationController controller = new TransportationController(
                    dataManager, schedulingService, reportService);

            // Create and start the user interface
            UserInterface ui = new CommandLineUI(controller);

            // Display the main menu to begin user interaction
            ui.displayMenu();

            // Close database connection when the application exits
            DatabaseConnector.closeConnection();

        } catch (SQLException e) {
            System.err.println("Failed to initialize application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}