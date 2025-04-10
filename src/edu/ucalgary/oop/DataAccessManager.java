package edu.ucalgary.oop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DataAccessManager provides a centralized way to interact with the PostgreSQL
 * database for the Accessible Transportation system.
 * It supports operations for RideRequests, Vehicles, Drivers, and Schedules.
 * @author Group 16
 * @version 1.0
 * @since 1.0
 */
public class DataAccessManager {

    private Connection dbConnection;

    /**
     * Constructs a DataAccessManager and establishes a connection to the database.
     *
     * @throws SQLException if a database access error occurs
     */
    public DataAccessManager() throws SQLException {
        connect();
    }

    /**
     * Connects to the database using DatabaseConnector.
     *
     * @throws SQLException if a database access error occurs
     */
    public void connect() throws SQLException {
        this.dbConnection = DatabaseConnector.getConnection();
    }

    /**
     * Disconnects from the database.
     *
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
     *
     * @return a list of RideRequest objects
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
     * Retrieves a ride request by its ID.
     *
     * @param id the request ID
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
     *
     * @param request the RideRequest object to add
     * @return true if the request was added successfully, false otherwise
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

            if (request.getPickupTime() != null) {
                pstmt.setTime(7, java.sql.Time.valueOf(request.getPickupTime()));
            } else {
                pstmt.setNull(7, java.sql.Types.TIME);
            }

            if (request.getRequestDate() != null) {
                pstmt.setDate(6, java.sql.Date.valueOf(request.getRequestDate()));
            } else {
                pstmt.setNull(6, java.sql.Types.DATE);
            }

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
     *
     * @param request the RideRequest object to update
     * @return true if the update was successful, false otherwise
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

            if (request.getRequestDate() != null) {
                pstmt.setDate(6, java.sql.Date.valueOf(request.getRequestDate()));
            } else {
                pstmt.setNull(6, java.sql.Types.DATE);
            }

            if (request.getPickupTime() != null) {
                pstmt.setTime(7, java.sql.Time.valueOf(request.getPickupTime()));
            } else {
                pstmt.setNull(7, java.sql.Types.TIME);
            }

            pstmt.setString(8, request.getStatus());
            pstmt.setInt(9, request.getRequestID());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Updates the status of a ride request.
     *
     * @param id the ID of the ride request
     * @param status the new status
     * @return true if the update was successful, false otherwise
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
     * Converts the current row of a ResultSet into a RideRequest object.
     *
     * @param rs the ResultSet positioned at a row containing ride request data
     * @return a fully populated RideRequest object
     * @throws SQLException if accessing the result set fails
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
     *
     * @return a list of Vehicle objects
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
     * Retrieves a vehicle by its ID.
     *
     * @param id the vehicle ID
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
     * Gets a list of available vehicles that meet the criteria for a ride.
     *
     * @param date the date of the ride
     * @param startTime the start time window
     * @param endTime the end time window
     * @param needsWheelchair whether a wheelchair accessible vehicle is needed
     * @param passengerCount number of passengers
     * @return a list of available Vehicle objects
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
     * Checks whether a specific vehicle is already scheduled for a ride
     * that overlaps with the given time window on a specific date.
     *
     * @param vehicleId the ID of the vehicle to check
     * @param date the date to check for scheduling conflicts
     * @param startTime the start of the time window
     * @param endTime the end of the time window
     * @return true if the vehicle is already scheduled during the time window; false otherwise
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
     * Converts a SQL ResultSet row into a Vehicle object.
     *
     * @param rs the ResultSet pointing to the current row of vehicle data
     * @return a populated Vehicle object
     * @throws SQLException if accessing the result set fails
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
     *
     * @return a list of Driver objects
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
     * Retrieves a driver by ID.
     *
     * @param id the driver ID
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
     * Gets a list of drivers who are available during a time window.
     *
     * @param date the date of the ride
     * @param startTime the start time window
     * @param endTime the end time window
     * @return a list of available Driver objects
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
     * Checks whether a specific driver is already scheduled for a ride
     * that overlaps with the specified time window on a given date.
     *
     * @param driverId the ID of the driver to check
     * @param date the date for which to check the schedule
     * @param startTime the start of the time window
     * @param endTime the end of the time window
     * @return true if the driver is scheduled during the specified time; false otherwise
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
     * Updates a driver's availability in the database.
     *
     * @param id the driver's ID
     * @param isAvailable true if available, false if not
     * @return true if the update was successful
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
     * Converts the current row of a ResultSet into a Driver object.
     *
     * @param rs the ResultSet positioned at a valid row containing driver data
     * @return a populated Driver object based on the row's data
     * @throws SQLException if accessing the result set fails
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
     *
     * @return a list of Schedule objects
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
     * Adds a new schedule entry to the database.
     *
     * @param schedule the Schedule to add
     * @return true if the schedule was added successfully
     * @throws SQLException if a database access error occurs
     */
    public boolean addSchedule(Schedule schedule) throws SQLException {
        String query = "INSERT INTO Schedules (DriverID, VehicleID, RequestID, " +
                "ScheduledDate, ScheduledTime) VALUES (?, ?, ?, ?, ?) RETURNING ScheduleID";

        try (PreparedStatement pstmt = dbConnection.prepareStatement(query)) {
            pstmt.setInt(1, schedule.getDriver().getDriverID());
            pstmt.setInt(2, schedule.getVehicle().getVehicleID());
            pstmt.setInt(3, schedule.getRideRequest().getRequestID());

            if (schedule.getDate() != null) {
                pstmt.setDate(4, java.sql.Date.valueOf(schedule.getDate()));
            } else {
                pstmt.setNull(4, java.sql.Types.DATE);
            }

            if (schedule.getTime() != null) {
                pstmt.setTime(5, java.sql.Time.valueOf(schedule.getTime()));
            } else {
                pstmt.setNull(5, java.sql.Types.TIME);
            }


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
     *
     * @param schedule the Schedule to update
     * @return true if the update was successful
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
     * Deletes all schedules associated with a specific driver.
     *
     * @param driverId the driver ID
     * @return true if any schedules were deleted
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
     *
     * @param date the date to query
     * @return a list of Schedule objects
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
     * Retrieves all schedules assigned to a specific driver.
     *
     * @param driverId the driver ID
     * @return a list of Schedule objects
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
     *
     * @param startDate the beginning date (inclusive)
     * @param endDate the end date (inclusive)
     * @return a list of Schedule objects
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
     * Converts the current row of a ResultSet into a Schedule object by
     * retrieving and assembling the related Driver, Vehicle, and RideRequest.
     *
     * @param rs the ResultSet positioned at a row containing schedule data
     * @return a fully populated Schedule object
     * @throws SQLException if accessing the result set or related entities fails
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


