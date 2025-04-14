package model;

public class CrimeReport {
    private static int count = 0;
    private int id;
    private String title;
    private String description;
    private String date;  // Simple String date (format: dd-mm-yyyy)
    private Person reporter;

    public CrimeReport(String title, String description, String date, Person reporter) {
        this.id = ++count;
        this.title = title;
        this.description = description;
        this.date = date;
        this.reporter = reporter;
    }

    // Add getter for reporter to allow access control
    public Person getReporter() {
        return reporter;
    }

    public String toFileString() {
        // Format: id;title;description;date;reporterName;reporterRole
        return id + ";" + title + ";" + description + ";" + date + ";" + reporter.getName() + ";" + reporter.getRole();
    }

    @Override
    public String toString() {
        return "ID: " + id + "\nTitle: " + title +
                "\nDescription: " + description +
                "\nDate: " + date +
                "\nReporter: " + reporter;
    }
}