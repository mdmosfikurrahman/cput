package za.ac.cput.ui.panel;


import za.ac.cput.dto.request.UserRequest;
import za.ac.cput.domain.User;
import za.ac.cput.service.UserService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class ProfilePanel extends JPanel {
    private User adminUser;
    private UserService userService;
    private JTextField nameField, emailField, phoneField;
    private JPasswordField passwordField;
    private JLabel profilePicLabel;

    // Define colors
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 245);
    private static final Color HEADER_COLOR = new Color(60, 60, 60);
    private static final Color BUTTON_COLOR = new Color(0, 123, 255);
    private static final Color TEXT_COLOR = new Color(33, 37, 41);

    public ProfilePanel(User adminUser, UserService userService) {
        this.adminUser = adminUser;
        this.userService = userService;
        setLayout(new BorderLayout(20, 20));
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Profile Image
        profilePicLabel = new JLabel();
        profilePicLabel.setPreferredSize(new Dimension(150, 150));
        profilePicLabel.setHorizontalAlignment(JLabel.CENTER);
        loadProfilePicture();
        mainPanel.add(profilePicLabel, gbc);

        nameField = createStyledTextField(adminUser.getName());
        emailField = createStyledTextField(adminUser.getEmail());
        phoneField = createStyledTextField(adminUser.getPhone());
        passwordField = createStyledPasswordField();

        mainPanel.add(createStyledLabel("Name:"), gbc);
        mainPanel.add(nameField, gbc);
        mainPanel.add(createStyledLabel("Email:"), gbc);
        mainPanel.add(emailField, gbc);
        mainPanel.add(createStyledLabel("Phone:"), gbc);
        mainPanel.add(phoneField, gbc);
        mainPanel.add(createStyledLabel("New Password (leave blank to keep current):"), gbc);
        mainPanel.add(passwordField, gbc);

        JButton updateButton = new JButton("Update Profile");
        updateButton.setBackground(BUTTON_COLOR);
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.addActionListener(e -> updateProfile());
        mainPanel.add(updateButton, gbc);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    private JTextField createStyledTextField(String text) {
        JTextField textField = new JTextField(text, 20);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        return textField;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        return passwordField;
    }

    private void loadProfilePicture() {
        try {
            // Assuming the profile picture is named "userProfile.jpeg" and is in the project's root directory
            File imageFile = new File("userProfile.jpeg");
            if (imageFile.exists()) {
                BufferedImage img = ImageIO.read(imageFile);
                Image scaledImg = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                BufferedImage circleBuffer = new BufferedImage(150, 150, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circleBuffer.createGraphics();
                g2.setClip(new Ellipse2D.Float(0, 0, 150, 150));
                g2.drawImage(scaledImg, 0, 0, null);
                profilePicLabel.setIcon(new ImageIcon(circleBuffer));
            } else {
                profilePicLabel.setText("No Image");
            }
        } catch (Exception e) {
            e.printStackTrace();
            profilePicLabel.setText("Error loading image");
        }
    }

    private void updateProfile() {
        String newName = nameField.getText();
        String newEmail = emailField.getText();
        String newPhone = phoneField.getText();
        String newPassword = new String(passwordField.getPassword());

        try {
            UserRequest userRQ = new UserRequest();
            userRQ.setName(newName);
            userRQ.setEmail(newEmail);
            userRQ.setPhone(newPhone);
            userRQ.setNic(adminUser.getNic());
            if (!newPassword.isEmpty()) {
                userRQ.setPassword(newPassword); // In real-world, hash this password
            }

            userService.update(adminUser.getId(), userRQ);

            JOptionPane.showMessageDialog(this, "Profile updated successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating profile: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
