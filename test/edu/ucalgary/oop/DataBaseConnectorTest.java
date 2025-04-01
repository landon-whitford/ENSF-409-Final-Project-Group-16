package edu.ucalgary.oop;

import org.junit.*;
import static org.junit.Assert.*;
import java.sql.*;

public class DataBaseConnectorTest {

    Driver driver;
    Vehicle vehicle;
    Connection dBConnect = null;
    private String dataBaseName = "accessibletransportation";

    @Before
    public void setUp() {
        try{
            dBConnect = DriverManager.getConnection("jdbc:postgresql://localhost/accessibletransportation",
                    "oop","ucalgary");
        } catch (SQLException e) {
            System.out.println("Query could not be completed: " + e.getMessage());
        }
    }

    @Test
    public void testConnection() throws SQLException {
        Connection connection = DataBaseConnector.createConnection(dataBaseName);
        assertEquals("createConnection correctly creates a connection to the database. ", dBConnect, connection);
    }



}
