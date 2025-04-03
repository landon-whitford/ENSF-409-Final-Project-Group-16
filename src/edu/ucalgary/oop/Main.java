package edu.ucalgary.oop;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialize application
            initializeApplication();
        } catch (SQLException e) {
            System.err.println("Error initializing application: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void initializeApplication() throws SQLException {
        // Initialize data access manager
        DataAccessManager dataManager = new DataAccessManager();

        // Initialize services
        SchedulingService schedulingService = new SchedulingService(dataManager);
        ReportGeneratorService reportService = new ReportGeneratorService(dataManager);

        // Initialize controller
        TransportationController controller = new TransportationController(
                dataManager, schedulingService, reportService);

        // Initialize UI
        UserInterface ui = new CommandLineUI(controller);

        // Start the application by displaying the main menu
        ui.displayMenu();

        // Clean up database connection when the application exits
        try {
            DatabaseConnector.closeConnection();
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}