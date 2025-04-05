package edu.ucalgary.oop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * The DataAccessManager class handles all database operations for the Accessible Transportation Scheduler.
 * It provides methods to create, read, update, and delete data for ride requests, vehicles, drivers, and schedules.
 *
 * @author Group 16
 * @version 1.6
 * @since 1.0
 */
public class DataAccessManager {

    private Connection dbConnection;

    /**
     * Constructs a new DataAccessManager and establishes a database connection.
     * @throws SQLException if a database access error occurs
     */
    public DataAccessManager() throws SQLException {
        connect();
    }

    /**
     * Establishes a connection to the database using the DatabaseConnector.
     * @throws SQLException if a database access error occurs
     */
    public void connect() throws SQLException {
        this.dbConnection = DatabaseConnector.getConnection();
    }

    /**
     * Disconnects from the database by setting the connection to null.
     * Note: This doesn't actually close the connection, as that's handled by the DatabaseConnector.
     * @throws SQLException if a database access error occurs
     */
    public void disconnect() throws SQLException {
        this.dbConnection = null;
    }

    //------------------------------------------------------------
    // RideRequest methods
    //------------------------------------------------------------

    /**
     * Retrieves all ride requests from the database.
     * @return a List of RideRequest objects
     * @throws SQLException if a database access error occurs
     */
    public List<RideRequest> getAllRideRequests() throws SQLException {
        List<RideRequest> requests = new ArrayList<>();
        String query = "SELECT * FROM RideRequests";

        try (Statement stmt = dbConnection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                RideRequest request = mapResultSetToRideRequest(rs);
                requests.add(request);
            }
        }

        return requests;
    }

    /**
     * Retrieves a specific ride request by its ID.
     * @param id the ID of the ride request to retrieve
     * @return the RideRequest object, or null if not found
     * @throws SQLException if a database access error occurs
     */
    public RideRequest getRideRequestById(int id) throws SQLException {
        String query = "SELECT * FROM RideRequests WHERE RequestID = ?";

        try (PreparedStatement pstmt = dbConnection.prepareStatement(query)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRideRequest(rs);
                }
            }
        }

        return null;
    }

    /**
     * Adds a new ride request to the database.
     * @param request the RideRequest object to add
     * @return true if successfully added, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean addRideRequest(RideRequest request) throws SQLException {
        String query = "INSERT INTO RideRequests (ClientName, PickupLocation, DropoffLocation, " +
                "PassengerCount, SpecialRequirements, RequestDate, PickupTime, Status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING RequestID";

        try (PreparedStatement pstmt = dbConnection.prepareStatement(query)) {
            pstmt.setString(1, request.getClientName());
            pstmt.setString(2, request.getPickUpLocation());
            pstmt.setString(3, request.getDropOffLocation());
            pstmt.setInt(4, request.getPassengerCount());
            pstmt.setString(5, request.getSpecialRequirements());
            pstmt.setDate(6, java.sql.Date.valueOf(request.getRequestDate()));

            // Explicitly set time to hours and minutes
            pstmt.setTime(7, java.sql.Time.valueOf(
                    LocalTime.of(
                            request.getPickupTime().getHour(),
                            request.getPickupTime().getMinute()
                    )
            ));

            pstmt.setString(8, request.getStatus());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    request.setRequestID(rs.getInt(1));
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Updates an existing ride request in the database.
     * @param request the RideRequest object with updated information
     * @return true if successfully updated, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean updateRideRequest(RideRequest request) throws SQLException {
        String query = "UPDATE RideRequests SET ClientName = ?, PickupLocation = ?, " +
                "DropoffLocation = ?, PassengerCount = ?, SpecialRequirements = ?, " +
                "RequestDate = ?, PickupTime = ?, Status = ? " +
                "WHERE RequestID = ?";

        try (PreparedStatement pstmt = dbConnection.prepareStatement(query)) {
            pstmt.setString(1, request.getClientName());
            pstmt.setString(2, request.getPickUpLocation());
            pstmt.setString(3, request.getDropOffLocation());
            pstmt.setInt(4, request.getPassengerCount());
            pstmt.setString(5, request.getSpecialRequirements());
            pstmt.setDate(6, java.sql.Date.valueOf(request.getRequestDate()));

            // Explicitly set time to hours and minutes
            pstmt.setTime(7, java.sql.Time.valueOf(
                    LocalTime.of(
                            request.getPickupTime().getHour(),
                            request.getPickupTime().getMinute()
                    )
            ));

            pstmt.setString(8, request.getStatus());
            pstmt.setInt(9, request.getRequestID());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Updates the status of a ride request.
     * @param id the ID of the ride request to update
     * @param status the new status value
     * @return true if successfully updated, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean updateRideStatus(int id, String status) throws SQLException {
        String query = "UPDATE RideRequests SET Status = ? WHERE RequestID = ?";

        try (PreparedStatement pstmt = dbConnection.prepareStatement(query)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, id);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Maps a ResultSet row to a RideRequest object.
     * @param rs the ResultSet containing ride request data
     * @return a populated RideRequest object
     * @throws SQLException if a database access error occurs
     */
    private RideRequest mapResultSetToRideRequest(ResultSet rs) throws SQLException {
        RideRequest request = new RideRequest();

        request.setRequestID(rs.getInt("RequestID"));
        request.setClientName(rs.getString("ClientName"));
        request.setPickUpLocation(rs.getString("PickupLocation"));
        request.setDropOffLocation(rs.getString("DropoffLocation"));
        request.setPassengerCount(rs.getInt("PassengerCount"));
        request.setSpecialRequirements(rs.getString("SpecialRequirements"));

        java.sql.Date sqlDate = rs.getDate("RequestDate");
        request.setRequestDate(sqlDate.toLocalDate());

        java.sql.Time sqlTime = rs.getTime("PickupTime");
        request.setPickupTime(sqlTime.toLocalTime());

        request.setStatus(rs.getString("Status"));

        return request;
    }

    //------------------------------------------------------------
    // Vehicle methods
    //------------------------------------------------------------

    /**
     * Retrieves all vehicles from the database.
     * @return a List of Vehicle objects
     * @throws SQLException if a database access error occurs
     */
    public List<Vehicle> getAllVehicles() throws SQLException {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM Vehicles";

        try (Statement stmt = dbConnection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Vehicle vehicle = mapResultSetToVehicle(rs);
                vehicles.add(vehicle);
            }
        }

        return vehicles;
    }

    /**
     * Retrieves a specific vehicle by its ID.
     * @param id the ID of the vehicle to retrieve
     * @return the Vehicle object, or null if not found
     * @throws SQLException if a database access error occurs
     */
    public Vehicle getVehicleById(int id) throws SQLException {
        String query = "SELECT * FROM Vehicles WHERE VehicleID = ?";

        try (PreparedStatement pstmt = dbConnection.prepareStatement(query)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVehicle(rs);
                }
            }
        }

        return null;
    }

    /**
     * Retrieves available vehicles that meet specific requirements during a given time period.
     * @param date the date for which to check availability
     * @param startTime the start time of the period
     * @param endTime the end time of the period
     * @param needsWheelchair whether wheelchair accessibility is required
     * @param passengerCount the minimum required passenger capacity
     * @return a List of available Vehicle objects meeting the criteria
     * @throws SQLException if a database access error occurs
     */
    public List<Vehicle> getAvailableVehicles(LocalDate date, LocalTime startTime,
                                              LocalTime endTime, boolean needsWheelchair,
                                              int passengerCount) throws SQLException {
        List<Vehicle> availableVehicles = new ArrayList<>();

        // First get all vehicles that meet the basic requirements
        String query = "SELECT * FROM Vehicles WHERE " +
                "Capacity >= ? AND IsWheelchairAccessible >= ?";

        try (PreparedStatement pstmt = dbConnection.prepareStatement(query)) {
            pstmt.setInt(1, passengerCount);
            pstmt.setBoolean(2, needsWheelchair);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Vehicle vehicle = mapResultSetToVehicle(rs);

                    // Check if this vehicle is already scheduled during the requested time
                    if (!isVehicleScheduled(vehicle.getVehicleID(), date, startTime, endTime)) {
                        availableVehicles.add(vehicle);
                    }
                }
            }
        }

        return availableVehicles;
    }

    /**
     * Checks if a vehicle is already scheduled during a specific time period.
     * @param vehicleId the ID of the vehicle to check
     * @param date the date to check
     * @param startTime the start time of the period
     * @param endTime the end time of the period
     * @return true if the vehicle is already scheduled, false otherwise
     * @throws SQLException if a database access error occurs
     */
    private boolean isVehicleScheduled(int vehicleId, LocalDate date,
                                       LocalTime startTime, LocalTime endTime) throws SQLException {
        String query = "SELECT COUNT(*) FROM Schedules s " +
                "JOIN RideRequests r ON s.RequestID = r.RequestID " +
                "WHERE s.VehicleID = ? AND s.ScheduledDate = ? " +
                "AND r.Status = 'Scheduled' " +
                "AND ((s.ScheduledTime <= ? AND CAST(s.ScheduledTime AS TIME) + INTERVAL '30 minutes' >= ?) " +
                "OR (s.ScheduledTime <= ? AND CAST(s.ScheduledTime AS TIME) + INTERVAL '30 minutes' >= ?))";

        try (PreparedStatement pstmt = dbConnection.prepareStatement(query)) {
            pstmt.setInt(1, vehicleId);
            pstmt.setDate(2, java.sql.Date.valueOf(date));
            pstmt.setTime(3, java.sql.Time.valueOf(endTime));
            pstmt.setTime(4, java.sql.Time.valueOf(startTime));
            pstmt.setTime(5, java.sql.Time.valueOf(startTime));
            pstmt.setTime(6, java.sql.Time.valueOf(endTime));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    /**
     * Maps a ResultSet row to a Vehicle object.
     * @param rs the ResultSet containing vehicle data
     * @return a populated Vehicle object
     * @throws SQLException if a database access error occurs
     */
    private Vehicle mapResultSetToVehicle(ResultSet rs) throws SQLException {
        Vehicle vehicle = new Vehicle();

        vehicle.setVehicleID(rs.getInt("VehicleID"));
        vehicle.setLicensePlate(rs.getString("LicensePlate"));
        vehicle.setCapacity(rs.getInt("Capacity"));
        vehicle.setWheelchairAccessible(rs.getBoolean("IsWheelchairAccessible"));
        vehicle.setCurrentLocation(rs.getString("CurrentLocation"));

        java.sql.Date sqlDate = rs.getDate("MaintenanceDueDate");
        vehicle.setMaintenanceDueDate(sqlDate.toLocalDate());

        return vehicle;
    }

    //------------------------------------------------------------
    // Driver methods
    //------------------------------------------------------------

    /**
     * Retrieves all drivers from the database.
     * @return a List of Driver objects
     * @throws SQLException if a database access error occurs
     */
    public List<Driver> getAllDrivers() throws SQLException {
        List<Driver> drivers = new ArrayList<>();
        String query = "SELECT * FROM Drivers";

        try (Statement stmt = dbConnection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Driver driver = mapResultSetToDriver(rs);
                drivers.add(driver);
            }
        }

        return drivers;
    }

    /**
     * Retrieves a specific driver by ID.
     * @param id the ID of the driver to retrieve
     * @return the Driver object, or null if not found
     * @throws SQLException if a database access error occurs
     */
    public Driver getDriverById(int id) throws SQLException {
        String query = "SELECT * FROM Drivers WHERE DriverID = ?";

        try (PreparedStatement pstmt = dbConnection.prepareStatement(query)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDriver(rs);
                }
            }
        }

        return null;
    }

    /**
     * Retrieves available drivers during a specific time period.
     * @param date the date for which to check availability
     * @param startTime the start time of the period
     * @param endTime the end time of the period
     * @return a List of available Driver objects
     * @throws SQLException if a database access error occurs
     */
    public List<Driver> getAvailableDrivers(LocalDate date, LocalTime startTime,
                                            LocalTime endTime) throws SQLException {
        List<Driver> availableDrivers = new ArrayList<>();

        // First get all drivers that are marked as available
        String query = "SELECT * FROM Drivers WHERE IsAvailable = TRUE";

        try (PreparedStatement pstmt = dbConnection.prepareStatement(query)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Driver driver = mapResultSetToDriver(rs);

                    // Check if this driver is already scheduled during the requested time
                    if (!isDriverScheduled(driver.getDriverID(), date, startTime, endTime)) {
                        availableDrivers.add(driver);
                    }
                }
            }
        }

        return availableDrivers;
    }

    /**
     * Checks if a driver is already scheduled during a specific time period.
     * @param driverId the ID of the driver to check
     * @param date the date to check
     * @param startTime the start time of the period
     * @param endTime the end time of the period
     * @return true if the driver is already scheduled, false otherwise
     * @throws SQLException if a database access error occurs
     */
    private boolean isDriverScheduled(int driverId, LocalDate date,
                                      LocalTime startTime, LocalTime endTime) throws SQLException {
        String query = "SELECT COUNT(*) FROM Schedules s " +
                "JOIN RideRequests r ON s.RequestID = r.RequestID " +
                "WHERE s.DriverID = ? AND s.ScheduledDate = ? " +
                "AND r.Status = 'Scheduled' " +
                "AND ((s.ScheduledTime <= ? AND CAST(s.ScheduledTime AS TIME) + INTERVAL '30 minutes' >= ?) " +
                "OR (s.ScheduledTime <= ? AND CAST(s.ScheduledTime AS TIME) + INTERVAL '30 minutes' >= ?))";

        try (PreparedStatement pstmt = dbConnection.prepareStatement(query)) {
            pstmt.setInt(1, driverId);
            pstmt.setDate(2, java.sql.Date.valueOf(date));
            pstmt.setTime(3, java.sql.Time.valueOf(endTime));
            pstmt.setTime(4, java.sql.Time.valueOf(startTime));
            pstmt.setTime(5, java.sql.Time.valueOf(startTime));
            pstmt.setTime(6, java.sql.Time.valueOf(endTime));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    /**
     * Updates a driver's availability status.
     * @param id the ID of the driver to update
     * @param isAvailable the new availability status
     * @return true if successfully updated, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean updateDriverAvailability(int id, boolean isAvailable) throws SQLException {
        String query = "UPDATE Drivers SET IsAvailable = ? WHERE DriverID = ?";

        try (PreparedStatement pstmt = dbConnection.prepareStatement(query)) {
            pstmt.setBoolean(1, isAvailable);
            pstmt.setInt(2, id);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Maps a ResultSet row to a Driver object.
     * @param rs the ResultSet containing driver data
     * @return a populated Driver object
     * @throws SQLException if a database access error occurs
     */
    private Driver mapResultSetToDriver(ResultSet rs) throws SQLException {
        Driver driver = new Driver();

        driver.setDriverID(rs.getInt("DriverID"));
        driver.setName(rs.getString("Name"));
        driver.setPhoneNumber(rs.getString("PhoneNumber"));
        driver.setLicenseNumber(rs.getString("LicenseNumber"));
        driver.setAvailable(rs.getBoolean("IsAvailable"));

        return driver;
    }

    //------------------------------------------------------------
    // Schedule methods
    //------------------------------------------------------------

    /**
     * Retrieves all schedules from the database.
     * @return a List of Schedule objects
     * @throws SQLException if a database access error occurs
     */
    public List<Schedule> getAllSchedules() throws SQLException {
        List<Schedule> schedules = new ArrayList<>();
        String query = "SELECT * FROM Schedules";

        try (Statement stmt = dbConnection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Schedule schedule = mapResultSetToSchedule(rs);
                schedules.add(schedule);
            }
        }

        return schedules;
    }

    /**
     * Adds a new schedule to the database.
     * @param schedule the Schedule object to add
     * @return true if successfully added, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean addSchedule(Schedule schedule) throws SQLException {
        String query = "INSERT INTO Schedules (DriverID, VehicleID, RequestID, " +
                "ScheduledDate, ScheduledTime) VALUES (?, ?, ?, ?, ?) RETURNING ScheduleID";

        try (PreparedStatement pstmt = dbConnection.prepareStatement(query)) {
            pstmt.setInt(1, schedule.getDriver().getDriverID());
            pstmt.setInt(2, schedule.getVehicle().getVehicleID());
            pstmt.setInt(3, schedule.getRideRequest().getRequestID());
            pstmt.setDate(4, java.sql.Date.valueOf(schedule.getDate().toString()));
            pstmt.setTime(5, java.sql.Time.valueOf(schedule.getTime().toString()));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    schedule.setScheduleID(rs.getInt(1));
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Updates an existing schedule in the database.
     * @param schedule the Schedule object with updated information
     * @return true if successfully updated, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean updateSchedule(Schedule schedule) throws SQLException {
        String query = "UPDATE Schedules SET DriverID = ?, VehicleID = ?, RequestID = ?, " +
                "ScheduledDate = ?, ScheduledTime = ? WHERE ScheduleID = ?";

        try (PreparedStatement pstmt = dbConnection.prepareStatement(query)) {
            pstmt.setInt(1, schedule.getDriver().getDriverID());
            pstmt.setInt(2, schedule.getVehicle().getVehicleID());
            pstmt.setInt(3, schedule.getRideRequest().getRequestID());
            pstmt.setDate(4, java.sql.Date.valueOf(schedule.getDate().toString()));
            pstmt.setTime(5, java.sql.Time.valueOf(schedule.getTime().toString()));
            pstmt.setInt(6, schedule.getScheduleID());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Deletes all schedules for a specific driver.
     * @param driverId the ID of the driver whose schedules should be deleted
     * @return true if any schedules were deleted, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean deleteSchedulesByDriverId(int driverId) throws SQLException {
        String query = "DELETE FROM Schedules WHERE DriverID = ?";

        try (PreparedStatement pstmt = dbConnection.prepareStatement(query)) {
            pstmt.setInt(1, driverId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Retrieves all schedules for a specific date.
     * @param date the date for which to retrieve schedules
     * @return a List of Schedule objects
     * @throws SQLException if a database access error occurs
     */
    public List<Schedule> getSchedulesByDate(LocalDate date) throws SQLException {
        List<Schedule> schedules = new ArrayList<>();
        String query = "SELECT * FROM Schedules WHERE ScheduledDate = ?";

        try (PreparedStatement pstmt = dbConnection.prepareStatement(query)) {
            pstmt.setDate(1, java.sql.Date.valueOf(date));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Schedule schedule = mapResultSetToSchedule(rs);
                    schedules.add(schedule);
                }
            }
        }

        return schedules;
    }

    /**
     * Retrieves all schedules for a specific driver.
     * @param driverId the ID of the driver for whom to retrieve schedules
     * @return a List of Schedule objects
     * @throws SQLException if a database access error occurs
     */
    public List<Schedule> getSchedulesByDriverId(int driverId) throws SQLException {
        List<Schedule> schedules = new ArrayList<>();
        String query = "SELECT * FROM Schedules WHERE DriverID = ?";

        try (PreparedStatement pstmt = dbConnection.prepareStatement(query)) {
            pstmt.setInt(1, driverId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Schedule schedule = mapResultSetToSchedule(rs);
                    schedules.add(schedule);
                }
            }
        }

        return schedules;
    }

    /**
     * Retrieves all schedules within a date range.
     * @param startDate the beginning of the date range (inclusive)
     * @param endDate the end of the date range (inclusive)
     * @return a List of Schedule objects
     * @throws SQLException if a database access error occurs
     */
    public List<Schedule> getSchedulesByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<Schedule> schedules = new ArrayList<>();
        String query = "SELECT * FROM Schedules WHERE ScheduledDate BETWEEN ? AND ?";

        try (PreparedStatement pstmt = dbConnection.prepareStatement(query)) {
            pstmt.setDate(1, java.sql.Date.valueOf(startDate));
            pstmt.setDate(2, java.sql.Date.valueOf(endDate));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Schedule schedule = mapResultSetToSchedule(rs);
                    schedules.add(schedule);
                }
            }
        }

        return schedules;
    }

    /**
     * Maps a ResultSet row to a Schedule object.
     * @param rs the ResultSet containing schedule data
     * @return a populated Schedule object
     * @throws SQLException if a database access error occurs
     */
    private Schedule mapResultSetToSchedule(ResultSet rs) throws SQLException {
        Schedule schedule = new Schedule();

        schedule.setScheduleID(rs.getInt("ScheduleID"));

        Driver driver = getDriverById(rs.getInt("DriverID"));
        Vehicle vehicle = getVehicleById(rs.getInt("VehicleID"));
        RideRequest request = getRideRequestById(rs.getInt("RequestID"));

        schedule.setDriver(driver);
        schedule.setVehicle(vehicle);
        schedule.setRideRequest(request);

        java.sql.Date sqlDate = rs.getDate("ScheduledDate");
        schedule.setDate(sqlDate.toLocalDate());

        java.sql.Time sqlTime = rs.getTime("ScheduledTime");
        schedule.setTime(sqlTime.toLocalTime());

        return schedule;
    }
}