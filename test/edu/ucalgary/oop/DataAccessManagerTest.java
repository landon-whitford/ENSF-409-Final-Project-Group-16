package edu.ucalgary.oop;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.*;
import static org.junit.Assert.*;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class DataAccessManagerTest {

    DataAccessManager dataAccessManager = null;
    List<RideRequest> rideRequests = new ArrayList<>();

    @Before
    public void setUp() throws SQLException {
        try{
            dataAccessManager = new DataAccessManager();
        }catch (SQLException e){
            System.out.println("Could not make connection. ");
            System.err.println(e.getMessage());
        }

        Date date1 = Date.valueOf("2024-01-10");
        Date date2 = Date.valueOf("2024-01-20");
        Date date3 = Date.valueOf("2024-01-11");

        Time time1 = Time.valueOf("09:00:00");
        Time time2 = Time.valueOf("10:30:00");
        Time time3 = Time.valueOf("08:00:00");

        rideRequests.add(new RideRequest(1, "Priya Patel", "123 Main St",
                "456 Elm St", 1, "Wheelchair",
                date1, time1, "Pending"));
        rideRequests.add(new RideRequest(2, "Neveah Miller", "789 Oak St",
                "321 Pine St", 2, "",
                date2, time2, "Pending"));
        rideRequests.add(new RideRequest(3, "Chen Li", "654 Maple St",
                "987 Cedar St", 4, "Wheelchair",
                date3, time3, "Pending"));
    }

    @Test
    public void testDisconnect() throws SQLException {
        dataAccessManager.disconnect();
        assertEquals("disconnect did not set the dbConnect to null. ", null, dataAccessManager.getDBConnection());
    }

    @Test
    public void testGetAllRideRequests() throws SQLException {
        //for(int i = 0; i < rideRequests.length; i++){}
        assertEquals("getAllRideRequests incorrectly assigns requestID from the database. ",
                rideRequests.get(0).getRequestID(), dataAccessManager.getAllRideRequests().get(0).getRequestID());
        assertEquals("getAllRideRequests incorrectly assigns clientName from the database. ",
                true, dataAccessManager.getAllRideRequests().get(0).getClientName().equals(rideRequests.get(0).getClientName()));
        assertEquals("getAllRideRequests incorrectly assigns pickUpLocation from the database. ",
                true, dataAccessManager.getAllRideRequests().get(0).getPickUpLocation().equals(rideRequests.get(0).getPickUpLocation()));

    }

}
