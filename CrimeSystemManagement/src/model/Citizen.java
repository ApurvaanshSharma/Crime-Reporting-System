package model;

public class Citizen extends Person {
    public Citizen(String name) {
        super(name);
    }

    @Override
    public String getRole() {
        return "Citizen";
    }

    @Override
    public String toString() {
        return name + " (Citizen)";
    }
}
