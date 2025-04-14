package model;

public class PoliceOfficer extends Person {
    public PoliceOfficer(String name) {
        super(name);
    }

    @Override
    public String getRole() {
        return "Police";
    }

    @Override
    public String toString() {
        return name + " (Police)";
    }
}
