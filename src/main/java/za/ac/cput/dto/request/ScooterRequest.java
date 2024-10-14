package za.ac.cput.dto.request;

import za.ac.cput.enums.Model;
import za.ac.cput.enums.ScooterStatus;

public class ScooterRequest {

    private Model model;
    private int year;
    private int rentalPerDay;
    private ScooterStatus scooterStatus;

    public ScooterRequest() {
    }

    public ScooterRequest(Model model, int year, int rentalPerDay, ScooterStatus scooterStatus) {
        this.model = model;
        this.year = year;
        this.rentalPerDay = rentalPerDay;
        this.scooterStatus = scooterStatus;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getRentalPerDay() {
        return rentalPerDay;
    }

    public void setRentalPerDay(int rentalPerDay) {
        this.rentalPerDay = rentalPerDay;
    }

    public ScooterStatus getScooterStatus() {
        return scooterStatus;
    }

    public void setScooterStatus(ScooterStatus scooterStatus) {
        this.scooterStatus = scooterStatus;
    }
}
