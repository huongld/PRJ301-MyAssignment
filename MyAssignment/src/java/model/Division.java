package model;

public class Division {
    private int divisionID;
    private String divisionName;

    public Division() {
    }

    public Division(int divisionID, String divisionName) {
        this.divisionID = divisionID;
        this.divisionName = divisionName;
    }

    // Getters and Setters
    public int getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }
}