package za.ac.cput.ui.panel.dashboard;

import za.ac.cput.domain.Scooter;
import za.ac.cput.service.ScooterService;
import za.ac.cput.ui.panel.components.ScooterCard;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HomeDashboard extends JPanel {
    private final ScooterService scooterService;

    public HomeDashboard(ScooterService scooterService) {
        this.scooterService = scooterService;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 245)); // Background color

        // Create the scooter display area with enhanced layout
        JPanel scooterDisplayPanel = new JPanel();
        scooterDisplayPanel.setLayout(new GridLayout(0, 3, 20, 20)); // Improved spacing

        // Fetch scooters from the service and add them to the display
        List<Scooter> scooters = scooterService.getAvailableScooters();
        for (Scooter scooter : scooters) {
            ScooterCard scooterCard = createScooterCard(scooter);
            scooterDisplayPanel.add(scooterCard);
        }

        JScrollPane scrollPane = new JScrollPane(scooterDisplayPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove default border
        add(scrollPane, BorderLayout.CENTER);

        // Create the button panel for Login and Register
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 255, 255)); // Button panel background
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding

        JButton loginButton = createStyledButton("Login");
        JButton registerButton = createStyledButton("Register");

        // Action listener to show the login panel
        loginButton.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) getParent().getLayout();
            cardLayout.show(getParent(), "LOGIN"); // Switch to the login panel
        });

        // Action listener to show the register panel
        registerButton.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) getParent().getLayout();
            cardLayout.show(getParent(), "REGISTER"); // Switch to the register panel
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Center buttons

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private ScooterCard createScooterCard(Scooter scooter) {
        return new ScooterCard(
                scooter,
                null, // No update action needed in HomeDashboard
                null, // No delete action needed in HomeDashboard
                null  // No booking action needed in HomeDashboard
        );
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 123, 255)); // Accent color
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> {
            // Add hover effect
            button.setBackground(button.getBackground().brighter());
        });
        return button;
    }

    public void refreshScooterDisplay() {
        removeAll(); // Clear existing components

        // Re-create the scooter display area
        JPanel scooterDisplayPanel = new JPanel();
        scooterDisplayPanel.setLayout(new GridLayout(0, 3, 20, 20));

        // Fetch scooters from the service and add them to the display
        List<Scooter> scooters = scooterService.getAvailableScooters();
        for (Scooter scooter : scooters) {
            ScooterCard scooterCard = createScooterCard(scooter);
            scooterDisplayPanel.add(scooterCard);
        }

        JScrollPane scrollPane = new JScrollPane(scooterDisplayPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove default border
        add(scrollPane, BorderLayout.CENTER);

        // Re-create the button panel for Login and Register
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 255, 255));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton loginButton = createStyledButton("Login");
        JButton registerButton = createStyledButton("Register");

        loginButton.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) getParent().getLayout();
            cardLayout.show(getParent(), "LOGIN");
        });

        registerButton.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) getParent().getLayout();
            cardLayout.show(getParent(), "REGISTER");
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        add(buttonPanel, BorderLayout.SOUTH);

        revalidate(); // Revalidate the layout
        repaint();    // Repaint the component
    }

}
