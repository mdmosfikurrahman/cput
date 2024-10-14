package za.ac.cput.domain;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import za.ac.cput.enums.Model;
import za.ac.cput.enums.ScooterStatus;

@Entity
@Table(name = "scooters")
public class Scooter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Model model;
    private int year;
    private int rentalPerDay;
    private ScooterStatus status;
    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private Attachment image;

    public Scooter() {
    }

    public Scooter(Long id, Model model, int year, int rentalPerDay, ScooterStatus status, Attachment image) {
        this.id = id;
        this.model = model;
        this.year = year;
        this.rentalPerDay = rentalPerDay;
        this.status = status;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ScooterStatus getStatus() {
        return status;
    }

    public void setStatus(ScooterStatus status) {
        this.status = status;
    }

    public Attachment getImage() {
        return image;
    }

    public void setImage(Attachment image) {
        this.image = image;
    }
}
