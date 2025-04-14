package threads;

import model.CrimeReport;
import system.CrimeSystem;

public class ReportSaver extends Thread {
    private CrimeReport report;
    private CrimeSystem system;

    public ReportSaver(CrimeReport report, CrimeSystem system) {
        this.report = report;
        this.system = system;
    }

    @Override
    public void run() {
        system.saveReportToFile(report);
        System.out.println("Report saved asynchronously.");
    }
}
