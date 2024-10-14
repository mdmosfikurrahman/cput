package za.ac.cput.ui;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import za.ac.cput.domain.Customer;
import za.ac.cput.domain.User;
import za.ac.cput.service.*;
import za.ac.cput.ui.panel.LoginPanel;
import za.ac.cput.ui.panel.RegisterPanel;
import za.ac.cput.ui.panel.dashboard.AdminDashboard;
import za.ac.cput.ui.panel.dashboard.CustomerDashboard;
import za.ac.cput.ui.panel.dashboard.EmployeeDashboard;
import za.ac.cput.ui.panel.dashboard.HomeDashboard;
import za.ac.cput.ui.service.UIAuthenticationService;

import javax.swing.*;
import java.awt.*;

@SpringBootApplication
@ComponentScan(basePackages = {"za.ac.cput"})
@EntityScan("za.ac.cput.domain")
@EnableJpaRepositories("za.ac.cput.repository")
public class ScooterRentalApp extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final LoginPanel loginPanel;
    private final CustomerDashboard customerDashboard;
    private final EmployeeDashboard employeeDashboard;
    private final AdminDashboard adminDashboard;
    private final HomeDashboard homeDashboard;

    public ScooterRentalApp(ConfigurableApplicationContext context) {
        setTitle("Scooter Rental System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        UIAuthenticationService authService = new UIAuthenticationService();
        UserService userService = context.getBean(UserService.class);
        CustomerService customerService = context.getBean(CustomerService.class);
        ScooterService scooterService = context.getBean(ScooterService.class);
        FileStoreService fileStoreService = context.getBean(FileStoreService.class);
        RentalService rentalService = context.getBean(RentalService.class);

        loginPanel = new LoginPanel(authService, this::onLoginSuccess);
        RegisterPanel registerPanel = new RegisterPanel(customerService);
        customerDashboard = new CustomerDashboard(customerService, scooterService, rentalService);
        customerDashboard.setLogoutListener(this::handleLogout);
        employeeDashboard = new EmployeeDashboard(userService, customerService, scooterService, fileStoreService, rentalService);
        employeeDashboard.setLogoutListener(this::handleLogout);
        adminDashboard = new AdminDashboard(userService, customerService, scooterService, fileStoreService, rentalService);
        adminDashboard.setLogoutListener(this::handleLogout);
        homeDashboard = new HomeDashboard(scooterService);

        mainPanel.add(homeDashboard, "HOME");
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(registerPanel, "REGISTER");
        mainPanel.add(customerDashboard, "CUSTOMER");
        mainPanel.add(employeeDashboard, "EMPLOYEE");
        mainPanel.add(adminDashboard, "ADMIN");

        add(mainPanel);
        cardLayout.show(mainPanel, "HOME");
    }

    private void onLoginSuccess(String role, Object loggedInUser) {
        if ("ADMIN".equalsIgnoreCase(role)) {
            adminDashboard.setLoggedInAdmin((User) loggedInUser);
        } else if ("EMPLOYEE".equalsIgnoreCase(role)) {
            employeeDashboard.setLoggedInEmployee((User) loggedInUser);
        } else if ("CUSTOMER".equalsIgnoreCase(role)) {
            customerDashboard.setLoggedInCustomer((Customer) loggedInUser);
        }
        cardLayout.show(mainPanel, role.toUpperCase());
    }

    private void handleLogout() {
        homeDashboard.refreshScooterDisplay();
        cardLayout.show(mainPanel, "HOME");
        loginPanel.reset();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ConfigurableApplicationContext context = new SpringApplicationBuilder(ScooterRentalApp.class)
                    .headless(false)
                    .run(args);

            ScooterRentalApp app = new ScooterRentalApp(context);
            app.setVisible(true);
        });
    }

}