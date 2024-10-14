package za.ac.cput.ui.panel.dashboard;

import lombok.Setter;
import za.ac.cput.domain.Scooter;
import za.ac.cput.service.*;
import za.ac.cput.ui.DashboardUtil;
import za.ac.cput.ui.panel.components.LogoutListener;
import za.ac.cput.ui.panel.components.ScooterCard;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public abstract class BaseDashboard extends JPanel {
    protected final CardLayout cardLayout;
    protected final JPanel contentPanel;
    protected final UserService userService;
    protected final CustomerService customerService;
    protected final ScooterService scooterService;
    protected final FileStoreService fileStoreService;
    protected final RentalService rentalService;

    @Setter
    protected LogoutListener logoutListener;
    protected static final Color BACKGROUND_COLOR = new Color(240, 240, 245);

    protected BaseDashboard(UserService userService, CustomerService customerService, ScooterService scooterService, FileStoreService fileStoreService, RentalService rentalService, String title, String[] tabs) {
        this.userService = userService;
        this.customerService = customerService;
        this.scooterService = scooterService;
        this.fileStoreService = fileStoreService;
        this.rentalService = rentalService;

        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        JPanel headerPanel = DashboardUtil.createHeaderPanel(title, Arrays.asList(tabs), this::updateContent);
        add(headerPanel, BorderLayout.NORTH);

        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        add(contentPanel, BorderLayout.CENTER);

        DashboardUtil.showDashboard(this::updateContent);
    }

    protected abstract void updateDashboardContent();

    protected void updateContent(String selectedTab) {
        contentPanel.removeAll();
        JPanel newPanel;

        switch (selectedTab) {
            case "Employees":
                newPanel = createEmployeesPanel();
                break;
            case "Customers":
                newPanel = createCustomersPanel();
                break;
            case "Scooters":
                newPanel = createScootersPanel();
                break;
            case "Rentals":
                newPanel = createRentalsPanel();
                break;
            case "Profile":
                newPanel = createProfilePanel();
                break;
            case "Dashboard":
                newPanel = createDashboardPanel();
                break;
            case "Search":
                newPanel = createSearchPanel();
                break;
            case "Logout":
                DashboardUtil.handleLogout(logoutListener);
                return;
            default:
                newPanel = new JPanel();
                newPanel.add(new JLabel(selectedTab + " Content"));
        }

        contentPanel.add(newPanel, selectedTab);
        cardLayout.show(contentPanel, selectedTab);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    protected abstract JPanel createDashboardPanel();

    protected JPanel createSearchPanel() {
        return DashboardUtil.createSearchPanel(this::performSearch);
    }

    protected void performSearch(String query) {
        JPanel searchPanel = (JPanel) contentPanel.getComponent(0);
        JPanel resultPanel = (JPanel) searchPanel.getClientProperty("resultPanel");

        DashboardUtil.performSearch(resultPanel, query, scooterService, this::createScooterCard);
    }

    protected abstract ScooterCard createScooterCard(Scooter scooter);

    protected abstract JPanel createEmployeesPanel();
    protected abstract JPanel createCustomersPanel();

    protected abstract JPanel createScootersPanel();

    protected abstract JPanel createRentalsPanel();

    protected abstract JPanel createProfilePanel();

}
