package edu.ucalgary.oop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

public class DataAccessManager {

    private Connection dbConnection;

    public DataAccessManager() throws SQLException {
        connect();
    }

    public void connect() throws SQLException {
        this.dbConnection = DatabaseConnector.getConnection();
    }

    public void disconnect() throws SQLException {
        this.dbConnection = null;
    }

    //------------------------------------------------------------
    // RideRequest methods
    //------------------------------------------------------------

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
            pstmt.setTime(7, java.sql.Time.valueOf(request.getPickupTime()));
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
            pstmt.setTime(7, java.sql.Time.valueOf(request.getPickupTime()));
            pstmt.setString(8, request.getStatus());
            pstmt.setInt(9, request.getRequestID());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean updateRideStatus(int id, String status) throws SQLException {
        String query = "UPDATE RideRequests SET Status = ? WHERE RequestID = ?";

        try (PreparedStatement pstmt = dbConnection.prepareStatement(query)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, id);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    private RideRequest mapResultSetToRideRequest(ResultSet rs) throws SQLException {
        RideRequest request = new RideRequest();

        request.setRequestID(rs.getInt("RequestID"));
        request.setClientName(rs.getString("ClientName"));
        request.setPickUpLocation(rs.getString("PickupLocation"));
        request.setDropOffLocation(rs.getString("DropoffLocation"));
        request.setPassengerCount(rs.getInt("PassengerCount"));
        request.setSpecialRequirements(rs.getString("SpecialRequirements"));

        java.sql.Date sqlDate = rs.getDate("RequestDate");
        request.setRequestDate(Date.valueOf(sqlDate.toLocalDate()));

        java.sql.Time sqlTime = rs.getTime("PickupTime");
        request.setPickupTime(Time.valueOf(sqlTime.toLocalTime()));

        request.setStatus(rs.getString("Status"));

        return request;
    }
    //------------------------------------------------------------
    // Vehicle methods
    //------------------------------------------------------------

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
}
