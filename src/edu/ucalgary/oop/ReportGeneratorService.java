package edu.ucalgary.oop;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.io.FileWriter;
import java.io.File;

import java.io.IOException;
import java.io.FileOutputStream;

import edu.ucalgary.oop.DataAccessManager;

    public class ReportGeneratorService {
    private DataAccessManager dataManager;

    public ReportGeneratorService(DataAccessManager dataManager) {
        this.dataManager = dataManager;
    }

    public void createDailyScheduleFile(LocalDate Date) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy'_MM'_dd");
        

        String fileName = "daily_schedule_" + currentDate.format(formatter) + ".txt";

        try { // first try to create the file and catch an exception if unsuccessful
            File myObj = new File(fileName);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } 
            else {
                System.out.println("File already exists.");
            } 
        } catch (IOException e) {
            System.err.println("An error occured while creating the daily schedule file.");
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter(fileName);

        }



    }

    
}
