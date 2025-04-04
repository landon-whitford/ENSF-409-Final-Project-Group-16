package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class ReportGeneratorServiceTest {
    // Stub implementation of DataAccessManager for testing
    private static class StubDataAccessManager extends DataAccessManager {
        private List<Schedule> schedules = new ArrayList<>();
        private List<Vehicle> vehicles = new ArrayList<>();
        private List<Driver> drivers = new ArrayList<>();

        public StubDataAccessManager() throws SQLException {
            super(); // Call parent constructor
        }

        @Override
        public List<Schedule> getSchedulesByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
            return new ArrayList<>(schedules);
        }

        @Override
        public List<Vehicle> getAllVehicles() throws SQLException {
            return new ArrayList<>(vehicles);
        }

        @Override
        public List<Driver> getAllDrivers() throws SQLException {
            return new ArrayList<>(drivers);
        }

        // Helper methods for test setup
        public boolean addSchedule(Schedule schedule) throws SQLException {
            schedules.add(schedule);
            return true;
        }

        public void setupSchedules(List<Schedule> testSchedules) {
            this.schedules = new ArrayList<>(testSchedules);
        }

        public void addVehicle(Vehicle vehicle) {
            vehicles.add(vehicle);
        }

        public void addDriver(Driver driver) {
            drivers.add(driver);
        }
    }

    private StubDataAccessManager stubDataManager;
    private ReportGeneratorService reportService;

    @Before
    public void setUp() throws SQLException {
        // Initialize stub data manager
        stubDataManager = new StubDataAccessManager();

        // Create report generator service with stub
        reportService = new ReportGeneratorService(stubDataManager);
    }

    @Test
    public void testCreateDailyScheduleFile() throws Exception {
        // Prepare test data
        LocalDate testDate = LocalDate.of(2025, 2, 26);

        // Create a driver
        Driver driver = new Driver();
        driver.setDriverID(1);
        driver.setName("Aisha Khan");
        driver.setLicenseNumber("D1234567");
        driver.setAvailable(true);
        stubDataManager.addDriver(driver);

        // Create a vehicle
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleID(1);
        vehicle.setLicensePlate("ABC123");
        vehicle.setWheelchairAccessible(true);
        stubDataManager.addVehicle(vehicle);

        // Create a ride request
        RideRequest request = new RideRequest();
        request.setClientName("Priya Patel");
        request.setPickUpLocation("123 Main St");
        request.setDropOffLocation("456 Elm St");
        request.setSpecialRequirements("Wheelchair");

        // Create a schedule
        Schedule schedule = new Schedule();
        schedule.setDriver(driver);
        schedule.setVehicle(vehicle);
        schedule.setRideRequest(request);
        schedule.setDate(testDate);
        schedule.setTime(LocalTime.of(9, 0));
        stubDataManager.addSchedule(schedule);

        // Execute
        boolean result = reportService.createDailyScheduleFile(testDate);

        // Verify
        assertTrue("Daily schedule file should be created", result);

        // Check file existence and name
        String expectedFileName = "daily_schedule_" + testDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".txt";
        File reportFile = new File(expectedFileName);
        assertTrue("Daily schedule file should exist", reportFile.exists());

        // Optional: You might want to add more detailed file content verification
    }

    @Test
    public void testCreateWeeklyReportFile() throws Exception {
        // Prepare test data
        LocalDate testDate = LocalDate.of(2025, 2, 24); // A Monday

        // Create vehicles
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setVehicleID(1);
        vehicle1.setLicensePlate("ABC123");
        vehicle1.setMaintenanceDueDate(LocalDate.of(2025, 6, 15));
        stubDataManager.addVehicle(vehicle1);

        Vehicle vehicle2 = new Vehicle();
        vehicle2.setVehicleID(2);
        vehicle2.setLicensePlate("XYZ456");
        vehicle2.setMaintenanceDueDate(LocalDate.of(2025, 6, 1));
        stubDataManager.addVehicle(vehicle2);

        // Create drivers
        Driver driver1 = new Driver();
        driver1.setDriverID(1);
        driver1.setName("Aisha Khan");
        driver1.setAvailable(true);
        stubDataManager.addDriver(driver1);

        Driver driver2 = new Driver();
        driver2.setDriverID(2);
        driver2.setName("Thomas Three Suns");
        driver2.setAvailable(false);
        stubDataManager.addDriver(driver2);

        // Create ride requests and schedules
        RideRequest request1 = new RideRequest();
        request1.setSpecialRequirements("Wheelchair");

        RideRequest request2 = new RideRequest();
        request2.setSpecialRequirements(null);

        Schedule schedule1 = new Schedule();
        schedule1.setDriver(driver1);
        schedule1.setVehicle(vehicle1);
        schedule1.setRideRequest(request1);
        schedule1.setDate(testDate);

        Schedule schedule2 = new Schedule();
        schedule2.setDriver(driver1);
        schedule2.setVehicle(vehicle1);
        schedule2.setRideRequest(request2);
        schedule2.setDate(testDate);

        stubDataManager.addSchedule(schedule1);
        stubDataManager.addSchedule(schedule2);

        // Execute
        boolean result = reportService.createWeeklyReportFile(testDate);

        // Verify
        assertTrue("Weekly report file should be created", result);

        // Check file existence and name
        LocalDate mondayOfWeek = testDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        String expectedFileName = "weekly_report_" + mondayOfWeek.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".txt";
        File reportFile = new File(expectedFileName);
        assertTrue("Weekly report file should exist", reportFile.exists());

        // Optional: You might want to add more detailed file content verification
    }

    @Test
    public void testCreateWeeklyReportWithNoRides() throws Exception {
        // Prepare test data with no schedules
        LocalDate testDate = LocalDate.of(2025, 2, 24); // A Monday

        // Create vehicles and drivers to ensure they exist
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleID(1);
        vehicle.setLicensePlate("ABC123");
        vehicle.setMaintenanceDueDate(LocalDate.of(2025, 6, 15));
        stubDataManager.addVehicle(vehicle);

        Driver driver = new Driver();
        driver.setDriverID(1);
        driver.setName("Test Driver");
        driver.setAvailable(true);
        stubDataManager.addDriver(driver);

        // Execute
        boolean result = reportService.createWeeklyReportFile(testDate);

        // Verify
        assertTrue("Weekly report file should be created even with no rides", result);

        // Check file existence and name
        LocalDate mondayOfWeek = testDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        String expectedFileName = "weekly_report_" + mondayOfWeek.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".txt";
        File reportFile = new File(expectedFileName);
        assertTrue("Weekly report file should exist", reportFile.exists());
    }

    // Clean up test files after tests
    @After
    public void tearDown() {
        // Remove generated files
        String[] filePatterns = {
                "daily_schedule_*.txt",
                "weekly_report_*.txt"
        };

        for (String pattern : filePatterns) {
            File[] filesToDelete = new File(".").listFiles(
                    (dir, name) -> name.matches(pattern.replace("*", ".*"))
            );

            if (filesToDelete != null) {
                for (File file : filesToDelete) {
                    file.delete();
                }
            }
        }
    }
}