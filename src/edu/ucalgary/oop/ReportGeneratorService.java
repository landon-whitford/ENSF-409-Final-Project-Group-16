package edu.ucalgary.oop;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.io.FileWriter;
import java.io.File;

import java.io.IOException;
import java.util.List;

import edu.ucalgary.oop.DataAccessManager;

    public class ReportGeneratorService {
    private DataAccessManager dataManager;

    public ReportGeneratorService(DataAccessManager dataManager) {
        this.dataManager = dataManager;
    }

    public void createDailyScheduleFile(LocalDate Date) {
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy'_MM'_dd");
        String fileName = "daily_schedule_" + Date.format(formatter1) + ".txt";
        File myObj = new File(fileName);

        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("LLLL' dd',' yyyy");
        String stringDate = Date.format(formatter2);

        try { // first try to create the file and catch an exception if unsuccessful
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } 
            else {
                System.out.println("File already exists.");
            } 
        } catch (IOException e) {
            System.err.println("An error occured while creating the daily schedule file.");
            e.printStackTrace();
            return;
        }

        List<Schedule> schedules = dataManager.getSchedulesByDate(Date);

        try(FileWriter myWriter = new FileWriter(myObj)) { // now try to write to the file and catch an exception if unsuccessful
           myWriter.write("Accesible Transportation Daily Schedule - " + stringDate + "\n\n");
           
            
        } catch (IOException e) {
            System.err.println("An error occured while creating the daily schedule file.");
            e.printStackTrace();
            return;
        }
    }

    
}
