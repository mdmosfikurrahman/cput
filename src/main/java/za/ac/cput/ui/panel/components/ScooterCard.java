package za.ac.cput.ui.panel.components;

import za.ac.cput.domain.Scooter;
import za.ac.cput.ui.DashboardUtil;

import javax.swing.*;
import java.awt.*;

public class ScooterCard extends JPanel {
    private static final Color ACCENT_COLOR = new Color(0, 123, 255);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color BUTTON_COLOR = new Color(40, 167, 69);
    private static final Color DELETE_BUTTON_COLOR = new Color(220, 53, 69);

    public ScooterCard(Scooter scooter, Runnable updateAction, Runnable deleteAction, Runnable bookAction) {
        configurePanel();

        JLabel imageLabel = DashboardUtil.createImageLabel(scooter);
        JPanel detailsPanel = DashboardUtil.createDetailsPanel(scooter);
        JPanel buttonPanel = createButtonPanel(updateAction, deleteAction, bookAction);

        setLayout(new BorderLayout(10, 10));
        add(imageLabel, BorderLayout.CENTER);
        add(detailsPanel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.NORTH);
    }

    private void configurePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 2));
        setBackground(CARD_COLOR);
        setPreferredSize(new Dimension(200, 300)); // Fixed size for uniformity
    }

    private JPanel createButtonPanel(Runnable updateAction, Runnable deleteAction, Runnable bookAction) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(CARD_COLOR);

        if (updateAction != null) {
            buttonPanel.add(createButton("Update", ACCENT_COLOR, updateAction));
        }

        if (deleteAction != null) {
            buttonPanel.add(createButton("Delete", DELETE_BUTTON_COLOR, deleteAction));
        }

        if (bookAction != null) {
            buttonPanel.add(createButton("Book", BUTTON_COLOR, bookAction));
        }

        return buttonPanel;
    }

    private JButton createButton(String text, Color backgroundColor, Runnable action) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> action.run());
        button.setPreferredSize(new Dimension(100, 30)); // Consistent button size
        return button;
    }
}
