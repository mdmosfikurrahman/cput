package za.ac.cput.ui.panel.dashboard;

import za.ac.cput.domain.Scooter;
import za.ac.cput.domain.User;
import za.ac.cput.service.*;
import za.ac.cput.ui.DashboardUtil;
import za.ac.cput.ui.dialog.UpdateScooterDialog;
import za.ac.cput.ui.panel.*;
import za.ac.cput.ui.panel.components.ScooterCard;

import javax.swing.*;

public class AdminDashboard extends BaseDashboard {
    private User loggedInAdmin;

    public AdminDashboard(UserService userService, CustomerService customerService, ScooterService scooterService, FileStoreService fileStoreService, RentalService rentalService) {
        super(userService, customerService, scooterService, fileStoreService, rentalService, "Admin Dashboard", new String[]{"Dashboard", "Employees", "Customers", "Scooters", "Rentals", "Search", "Profile", "Logout"});
    }

    public void setLoggedInAdmin(User admin) {
        this.loggedInAdmin = admin;
        SwingUtilities.invokeLater(this::updateDashboardContent);
    }

    @Override
    protected void updateDashboardContent() {
        if (loggedInAdmin != null) {
            DashboardUtil.refreshDashboard(contentPanel, cardLayout, this::createDashboardPanel);
        }
    }

    @Override
    protected JPanel createDashboardPanel() {
        return DashboardUtil.createDashboardPanel(
                scooterService,
                "Welcome, " + (loggedInAdmin != null ? loggedInAdmin.getName() : "Admin"),
                this::updateScooter,
                this::deleteScooter,
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

    private void deleteScooter(Scooter scooter) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this scooter?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            scooterService.delete(scooter.getId());
            DashboardUtil.refreshDashboard(contentPanel, cardLayout, this::createDashboardPanel);
        }
    }

    @Override
    protected ScooterCard createScooterCard(Scooter scooter) {
        return new ScooterCard(
                scooter,
                () -> updateScooter(scooter),
                () -> deleteScooter(scooter),
                null
        );
    }

    @Override
    protected JPanel createCustomersPanel() {
        return new CustomersPanel(customerService);
    }

    @Override
    protected JPanel createEmployeesPanel() {
        return new EmployeesPanel(userService);
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
        return new ProfilePanel(loggedInAdmin, userService);
    }
}
