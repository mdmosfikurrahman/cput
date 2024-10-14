package za.ac.cput.dto.request;


import java.time.LocalDate;

public class RentalRequest {

    private Long customerId;
    private Long scooterId;
    private LocalDate startDate;
    private int noOfDates;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getScooterId() {
        return scooterId;
    }

    public void setScooterId(Long scooterId) {
        this.scooterId = scooterId;
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
}
