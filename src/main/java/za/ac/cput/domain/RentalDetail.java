package za.ac.cput.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import za.ac.cput.enums.RentalStatus;

import java.time.LocalDate;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Entity
@Table(name = "rentalDetails")
public class RentalDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "scooter_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Scooter scooter;
    private LocalDate startDate;
    private int noOfDates;
    private RentalStatus status;
    private int totalPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Scooter getScooter() {
        return scooter;
    }

    public void setScooter(Scooter scooter) {
        this.scooter = scooter;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getNoOfDates() {
        return noOfDates;
    }

    public void setNoOfDates(int noOfDates) {
        this.noOfDates = noOfDates;
    }

    public RentalStatus getStatus() {
        return status;
    }

    public void setStatus(RentalStatus status) {
        this.status = status;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public RentalDetail() {
    }

    public RentalDetail(Long id, Customer customer, Scooter scooter, LocalDate startDate, int noOfDates, RentalStatus status, int totalPrice) {
        this.id = id;
        this.customer = customer;
        this.scooter = scooter;
        this.startDate = startDate;
        this.noOfDates = noOfDates;
        this.status = status;
        this.totalPrice = totalPrice;
    }
}
