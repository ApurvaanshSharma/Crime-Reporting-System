package system;

import model.CrimeReport;
import model.Citizen;
import model.PoliceOfficer;
import model.Person;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CrimeSystem {
    private static final String FILE_NAME = "reports.txt";
    private List<CrimeReport> reports = new ArrayList<>();

    public CrimeSystem() {
        loadReports();
    }

    public void addReport(CrimeReport report) {
        reports.add(report);
        // Save the report asynchronously using a separate thread (ReportSaver).
        // (This thread is started in Main, so this is just keeping the reports in memory.)
        System.out.println("Report added (will be saved asynchronously).");
    }

    // Modified viewReports method to implement role-based access control
    public void viewReports(Person currentUser) {
        if (reports.isEmpty()) {
            System.out.println("No reports available.");
            return;
        }

        boolean isPolice = currentUser.getRole().equals("Police");
        boolean foundReports = false;

        for (CrimeReport report : reports) {
            // If user is Police, show all reports
            // If user is Citizen, only show reports submitted by this user
            if (isPolice || report.getReporter().getName().equals(currentUser.getName())) {
                System.out.println("----------");
                System.out.println(report);
                foundReports = true;
            }
        }

        // Show a message if no reports are found for this citizen
        if (!foundReports && !isPolice) {
            System.out.println("You haven't submitted any reports yet.");
        }
    }

    // Method called by ReportSaver to save a report to file.
    public void saveReportToFile(CrimeReport report) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(report.toFileString());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving report: " + e.getMessage());
        }
    }

    // Load reports from file into the system's list.
    public void loadReports() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                CrimeReport report = parseReport(line);
                if (report != null) {
                    reports.add(report);
                }
            }
        } catch (IOException e) {
            System.out.println("No existing report file found. Starting fresh.");
        }
    }

    private CrimeReport parseReport(String line) {
        String[] parts = line.split(";");
        if (parts.length != 6) return null;
        // parts: id, title, description, date, reporterName, role
        String title = parts[1];
        String description = parts[2];
        String date = parts[3];
        String name = parts[4];
        String role = parts[5];
        // Create a Person depending on role.
        Person reporter;
        if (role.equals("Citizen")) {
            reporter = new Citizen(name);
        } else {
            reporter = new PoliceOfficer(name);
        }
        return new CrimeReport(title, description, date, reporter);
    }
}