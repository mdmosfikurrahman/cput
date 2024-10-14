package za.ac.cput.ui.panel.dashboard;

import com.toedter.calendar.JDateChooser;
import za.ac.cput.dto.request.RentalRequest;
import za.ac.cput.domain.Customer;
import za.ac.cput.domain.RentalDetail;
import za.ac.cput.domain.Scooter;
import za.ac.cput.service.CustomerService;
import za.ac.cput.service.RentalService;
import za.ac.cput.service.ScooterService;
import za.ac.cput.ui.DashboardUtil;
import za.ac.cput.ui.panel.CustomerProfilePanel;
import za.ac.cput.ui.panel.CustomerRentalsPanel;
import za.ac.cput.ui.panel.ScootersPanel;
import za.ac.cput.ui.panel.components.ScooterCard;

import javax.swing.*;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CustomerDashboard extends BaseDashboard {
    private Customer loggedInCustomer;

    public CustomerDashboard(CustomerService customerService, ScooterService scooterService, RentalService rentalService) {
        super(null, customerService, scooterService, null, rentalService, "Customer Dashboard", new String[]{"Dashboard", "Profile", "Rentals", "Search", "Logout"});
    }

    public void setLoggedInCustomer(Customer customer) {
        this.loggedInCustomer = customer;
        SwingUtilities.invokeLater(this::updateDashboardContent);
    }

    @Override
    protected void updateDashboardContent() {
        if (loggedInCustomer != null) {
            DashboardUtil.refreshDashboard(contentPanel, cardLayout, this::createDashboardPanel);
        }
    }

    @Override
    protected JPanel createDashboardPanel() {
        return DashboardUtil.createDashboardPanel(
                scooterService,
                "Welcome to Customer Dashboard",
                null,
                null,
                this::bookScooter
        );
    }

    private void bookScooter(Scooter scooter) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JDateChooser startDateChooser = new JDateChooser();
        startDateChooser.setDateFormatString("yyyy-MM-dd");
        startDateChooser.setMinSelectableDate(new Date());

        JDateChooser endDateChooser = new JDateChooser();
        endDateChooser.setDateFormatString("yyyy-MM-dd");
        endDateChooser.setMinSelectableDate(new Date());

        panel.add(new JLabel("Start Date:"));
        panel.add(startDateChooser);
        panel.add(new JLabel("End Date:"));
        panel.add(endDateChooser);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Book Scooter", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Date startDate = startDateChooser.getDate();
                Date endDate = endDateChooser.getDate();

                if (startDate == null || endDate == null || endDate.before(startDate)) {
                    JOptionPane.showMessageDialog(this, "Please select valid start and end dates.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                long diff = endDate.getTime() - startDate.getTime();
                int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

                RentalRequest rentalRequest = new RentalRequest();
                rentalRequest.setCustomerId(loggedInCustomer.getId());
                rentalRequest.setScooterId(scooter.getId());
                rentalRequest.setStartDate(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                rentalRequest.setNoOfDates(days);

                RentalDetail rental = rentalService.create(rentalRequest);
                JOptionPane.showMessageDialog(this, "Scooter booked successfully! Rental ID: " + rental.getId());
                DashboardUtil.refreshDashboard(contentPanel, cardLayout, this::createDashboardPanel);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error booking scooter: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected ScooterCard createScooterCard(Scooter scooter) {
        return new ScooterCard(
                scooter,
                null,
                null,
                () -> bookScooter(scooter)
        );
    }

    @Override
    protected JPanel createEmployeesPanel() {
        throw new UnsupportedOperationException("Employees panel not supported for CustomerDashboard");
    }

    @Override
    protected JPanel createCustomersPanel() {
        throw new UnsupportedOperationException("Customers panel not supported for CustomerDashboard");
    }

    @Override
    protected JPanel createScootersPanel() {
        return new ScootersPanel(scooterService, null);
    }

    @Override
    protected JPanel createRentalsPanel() {
        return new CustomerRentalsPanel(loggedInCustomer, rentalService);
    }


    @Override
    protected JPanel createProfilePanel() {
        return new CustomerProfilePanel(loggedInCustomer, customerService);
    }
}
