package edu.ucalgary.oop;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

public class RideRequestTest {

    private RideRequest request;

    @Before
    public void setUp() {
        request = new RideRequest();
        request.setRequestID(1);
        request.setClientName("John Doe");
        request.setPickUpLocation("123 Main St");
        request.setDropOffLocation("456 Oak Ave");
        request.setPassengerCount(2);
        request.setSpecialRequirements("Wheelchair");
        request.setRequestDate(LocalDate.of(2025, 3, 15));
        request.setPickupTime(LocalTime.of(9, 30));
        request.setStatus("Pending");
    }

    @Test
    public void testGetRequestID() {
        assertThat(request.getRequestID(), is(1));
    }

    @Test
    public void testSetRequestID() {
        request.setRequestID(2);
        assertThat(request.getRequestID(), is(2));
    }

    @Test
    public void testGetClientName() {
        assertThat(request.getClientName(), is("John Doe"));
    }

    @Test
    public void testSetClientName() {
        request.setClientName("Jane Smith");
        assertThat(request.getClientName(), is("Jane Smith"));
    }

    @Test
    public void testGetPickupLocation() {
        assertThat(request.getPickUpLocation(), is("123 Main St"));
    }

    @Test
    public void testSetPickupLocation() {
        request.setPickUpLocation("789 Pine Rd");
        assertThat(request.getPickUpLocation(), is("789 Pine Rd"));
    }

    @Test
    public void testGetDropoffLocation() {
        assertThat(request.getDropOffLocation(), is("456 Oak Ave"));
    }

    @Test
    public void testSetDropoffLocation() {
        request.setDropOffLocation("321 Elm Blvd");
        assertThat(request.getDropOffLocation(), is("321 Elm Blvd"));
    }

    @Test
    public void testGetPassengerCount() {
        assertThat(request.getPassengerCount(), is(2));
    }

    @Test
    public void testSetPassengerCount() {
        request.setPassengerCount(3);
        assertThat(request.getPassengerCount(), is(3));
    }

    @Test
    public void testGetSpecialRequirements() {
        assertThat(request.getSpecialRequirements(), is("Wheelchair"));
    }

    @Test
    public void testSetSpecialRequirements() {
        request.setSpecialRequirements("Service Animal");
        assertThat(request.getSpecialRequirements(), is("Service Animal"));
    }

    @Test
    public void testGetRequestDate() {
        assertThat(request.getRequestDate(), is(LocalDate.of(2025, 3, 15)));
    }

    @Test
    public void testSetRequestDate() {
        LocalDate newDate = LocalDate.of(2025, 4, 1);
        request.setRequestDate(newDate);
        assertThat(request.getRequestDate(), is(newDate));
    }

    @Test
    public void testGetPickupTime() {
        assertThat(request.getPickupTime(), is(LocalTime.of(9, 30)));
    }

    @Test
    public void testSetPickupTime() {
        LocalTime newTime = LocalTime.of(10, 15);
        request.setPickupTime(newTime);
        assertThat(request.getPickupTime(), is(newTime));
    }

    @Test
    public void testGetStatus() {
        assertThat(request.getStatus(), is("Pending"));
    }

    @Test
    public void testSetStatus() {
        request.setStatus("Scheduled");
        assertThat(request.getStatus(), is("Scheduled"));
    }
}