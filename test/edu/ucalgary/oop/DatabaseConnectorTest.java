package edu.ucalgary.oop;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.*;
import static org.junit.Assert.*;

public class DatabaseConnectorTest {

    Connection connection;

    @Before
    public void setUp() {
        try{
            connection = DatabaseConnector.getConnection();
        } catch (SQLException e){
            System.out.println("Could not make connection. ");
            System.err.println(e.getMessage());
        }
    }

    @Test
    public void testCloseConnection() throws SQLException {
        DatabaseConnector.closeConnection();
        assertEquals("Connection could not be closed. ", true, connection.isClosed());
    }

    @Test
    public void callGetConnectionTwice() throws SQLException {
        Connection connection2 = DatabaseConnector.getConnection();
        assertEquals("getConnection creates a new connection. ", connection,  connection2);
        DatabaseConnector.closeConnection();
        assertEquals("getConnection doesn't close both. ", connection,  connection2);
    }

}
