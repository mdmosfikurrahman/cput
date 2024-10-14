package za.ac.cput.ui.panel;

import za.ac.cput.dto.request.CustomerRequest;
import za.ac.cput.service.CustomerService;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {
    private final CustomerService customerService;

    public RegisterPanel(CustomerService customerService) {
        this.customerService = customerService;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name field
        JLabel nameLabel = new JLabel("Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(nameLabel, gbc);

        JTextField nameField = new JTextField(20);
        gbc.gridx = 1;
        add(nameField, gbc);

        // Email field
        JLabel emailLabel = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(emailLabel, gbc);

        JTextField emailField = new JTextField(20);
        gbc.gridx = 1;
        add(emailField, gbc);

        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        add(passwordField, gbc);

        // Phone field
        JLabel phoneLabel = new JLabel("Phone:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(phoneLabel, gbc);

        JTextField phoneField = new JTextField(20);
        gbc.gridx = 1;
        add(phoneField, gbc);

        // NIC field
        JLabel nicLabel = new JLabel("NIC:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(nicLabel, gbc);

        JTextField nicField = new JTextField(20);
        gbc.gridx = 1;
        add(nicField, gbc);

        // Register button
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String phone = phoneField.getText();
            String nic = nicField.getText();

            // Create CustomerRequest object
            CustomerRequest customerRequest = new CustomerRequest();
            customerRequest.setName(name);
            customerRequest.setEmail(email);
            customerRequest.setPassword(password);
            customerRequest.setPhone(phone);
            customerRequest.setNic(nic);

            // Implement registration logic (e.g., calling customerService)
            try {
                customerService.create(customerRequest);
                // Show success message
                JOptionPane.showMessageDialog(this, "Registration successful! Please log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Optionally switch back to login panel
                CardLayout cardLayout = (CardLayout) getParent().getLayout();
                cardLayout.show(getParent(), "LOGIN"); // Show the login panel after registration
            } catch (Exception ex) {
                // Handle registration failure
                JOptionPane.showMessageDialog(this, "Registration failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2; // Span across both columns
        add(registerButton, gbc);
    }
}
