package za.ac.cput.ui.panel;

import za.ac.cput.ui.service.UIAuthenticationService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.BiConsumer;

public class LoginPanel extends JPanel {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton loginButton;
    private final UIAuthenticationService authService;
    private final BiConsumer<String, Object> onLoginSuccess;
    private final Image backgroundImage;
    private JLabel loadingLabel;

    public LoginPanel(UIAuthenticationService authService, BiConsumer<String, Object> onLoginSuccess) {
        this.authService = authService;
        this.onLoginSuccess = onLoginSuccess;

        // Load the background image
        backgroundImage = new ImageIcon("src/main/resources/background.png").getImage();

        setLayout(new GridBagLayout());
        setOpaque(false); // Make the panel transparent
        initComponents();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        // Draw the semi-transparent rounded rectangle
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black
        g2d.fillRoundRect(30, 30, getWidth() - 60, getHeight() - 60, 20, 20); // Rounded rectangle
    }

    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Scooter Rental System Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER; // Center anchor
        add(titleLabel, gbc);

        gbc.gridwidth = 1; // Reset grid width for the next components
        gbc.anchor = GridBagConstraints.WEST;

        // Continue with other components
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(createStyledLabel("Email:"), gbc);

        gbc.gridx = 1;
        emailField = createStyledTextField();
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(createStyledLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = createStyledPasswordField();
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(createStyledLabel("Role:"), gbc);

        gbc.gridx = 1;
        String[] roles = {"Customer", "Employee", "Admin"};
        roleComboBox = createStyledComboBox(roles);
        add(roleComboBox, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2; // Span across two columns for button
        loginButton = createStyledButton();
        add(loginButton, gbc);

        loadingLabel = new JLabel("Loading...");
        loadingLabel.setForeground(Color.WHITE);
        loadingLabel.setVisible(false);
        gbc.gridy = 5;
        gbc.gridwidth = 2; // Span across two columns for loading label
        gbc.anchor = GridBagConstraints.CENTER; // Center anchor
        add(loadingLabel, gbc);

        loginButton.addActionListener(e -> login());

        // Add hover effect to the button
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(0, 100, 220));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(new Color(0, 123, 255));
            }
        });
    }


    private void login() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        loadingLabel.setVisible(true);
        loginButton.setEnabled(false);

        SwingWorker<Object, Void> worker = new SwingWorker<>() {
            @Override
            protected Object doInBackground() {
                return authService.login(email, password, role);
            }

            @Override
            protected void done() {
                loadingLabel.setVisible(false);
                loginButton.setEnabled(true);
                try {
                    Object loggedInUser = get();
                    if (loggedInUser != null) {
                        onLoginSuccess.accept(role, loggedInUser);
                    } else {
                        JOptionPane.showMessageDialog(LoginPanel.this, "Login failed. Please check your credentials.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(LoginPanel.this, "An error occurred during login.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    public void reset() {
        emailField.setText("");
        passwordField.setText("");
        roleComboBox.setSelectedIndex(0);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(20);
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setForeground(new Color(60, 63, 65));
        textField.setBackground(new Color(245, 245, 245));
        textField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return textField;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setForeground(new Color(60, 63, 65));
        passwordField.setBackground(new Color(245, 245, 245));
        passwordField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return passwordField;
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        comboBox.setForeground(new Color(60, 63, 65));
        comboBox.setBackground(new Color(245, 245, 245));
        comboBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return comboBox;
    }

    private JButton createStyledButton() {
        JButton button = new JButton("Login");
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 123, 255));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 40));
        return button;
    }
}
