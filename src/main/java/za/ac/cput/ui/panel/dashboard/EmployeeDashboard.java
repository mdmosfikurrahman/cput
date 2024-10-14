package za.ac.cput.ui.panel.dashboard;

import za.ac.cput.domain.Scooter;
import za.ac.cput.domain.User;
import za.ac.cput.service.*;
import za.ac.cput.ui.DashboardUtil;
import za.ac.cput.ui.dialog.UpdateScooterDialog;
import za.ac.cput.ui.panel.CustomersPanel;
import za.ac.cput.ui.panel.ProfilePanel;
import za.ac.cput.ui.panel.RentalsPanel;
import za.ac.cput.ui.panel.ScootersPanel;
import za.ac.cput.ui.panel.components.ScooterCard;

import javax.swing.*;

public class EmployeeDashboard extends BaseDashboard {
    private User loggedInEmployee;

    public EmployeeDashboard(UserService userService, CustomerService customerService, ScooterService scooterService, FileStoreService fileStoreService, RentalService rentalService) {
        super(userService, customerService, scooterService, fileStoreService, rentalService, "Employee Dashboard", new String[]{"Dashboard", "Customers", "Scooters", "Rentals", "Search", "Profile", "Logout"});
    }

    public void setLoggedInEmployee(User employee) {
        this.loggedInEmployee = employee;
        SwingUtilities.invokeLater(this::updateDashboardContent);
    }

    @Override
    protected void updateDashboardContent() {
        if (loggedInEmployee != null) {
            DashboardUtil.refreshDashboard(contentPanel, cardLayout, this::createDashboardPanel);
        }
    }

    @Override
    protected JPanel createDashboardPanel() {
        return DashboardUtil.createDashboardPanel(
                scooterService,
                "Welcome to Employee Dashboard",
                this::updateScooter,
                null,
                null
        );
    }

    private void updateScooter(Scooter scooter) {
        UpdateScooterDialog dialog = new UpdateScooterDialog(scooter, scooterService);
        dialog.setVisible(true);
        if (dialog.isUpdateSuccessful()) {
            DashboardUtil.refreshDashboard(contentPanel, cardLayout, this::createDashboardPanel);
        }
    }

    @Override
    protected ScooterCard createScooterCard(Scooter scooter) {
        return new ScooterCard(
                scooter,
                () -> updateScooter(scooter),
                null,
                null
        );
    }

    @Override
    protected JPanel createEmployeesPanel() {
        throw new UnsupportedOperationException("Employees panel not supported for EmployeesDashboard");
    }

    @Override
    protected JPanel createCustomersPanel() {
        return new CustomersPanel(customerService);
    }

    @Override
    protected JPanel createScootersPanel() {
        return new ScootersPanel(scooterService, fileStoreService);
    }

    @Override
    protected JPanel createRentalsPanel() {
        return new RentalsPanel(rentalService);
    }

    @Override
    protected JPanel createProfilePanel() {
        return new ProfilePanel(loggedInEmployee, userService);
    }
}
