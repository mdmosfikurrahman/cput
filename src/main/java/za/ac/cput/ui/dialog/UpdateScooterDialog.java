package za.ac.cput.ui.dialog;

import za.ac.cput.dto.request.ScooterRequest;
import za.ac.cput.domain.Scooter;
import za.ac.cput.enums.Model;
import za.ac.cput.enums.ScooterStatus;
import za.ac.cput.service.ScooterService;

import javax.swing.*;
import java.awt.*;

public class UpdateScooterDialog extends JDialog {
    private final Scooter scooter;
    private final ScooterService scooterService;
    private boolean updateSuccessful = false;

    private final JComboBox<Model> modelComboBox;
    private final JTextField yearField;
    private final JTextField rentalPerDayField;

    public UpdateScooterDialog(Scooter scooter, ScooterService scooterService) {
        this.scooter = scooter;
        this.scooterService = scooterService;

        setTitle("Update Scooter");
        setModal(true);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        modelComboBox = new JComboBox<>(Model.values());
        modelComboBox.setSelectedItem(scooter.getModel());
        yearField = new JTextField(String.valueOf(scooter.getYear()), 20);
        rentalPerDayField = new JTextField(String.valueOf(scooter.getRentalPerDay()), 20);

        add(new JLabel("Model:"), gbc);
        add(modelComboBox, gbc);
        add(new JLabel("Year:"), gbc);
        add(yearField, gbc);
        add(new JLabel("Rental Per Day:"), gbc);
        add(rentalPerDayField, gbc);
        add(new JLabel("Status:"), gbc);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> updateScooter());
        add(updateButton, gbc);

        pack();
        setLocationRelativeTo(null);
    }

    private void updateScooter() {
        try {
            Model model = (Model) modelComboBox.getSelectedItem();
            int year = Integer.parseInt(yearField.getText());
            int rentalPerDay = Integer.parseInt(rentalPerDayField.getText());

            ScooterRequest form = new ScooterRequest(model, year, rentalPerDay, ScooterStatus.NOT_AVAILABLE);

            scooterService.update(scooter.getId(), form);
            updateSuccessful = true;
            JOptionPane.showMessageDialog(this, "Scooter updated successfully!");
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for Year and Rental Per Day.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating scooter: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isUpdateSuccessful() {
        return updateSuccessful;
    }
}