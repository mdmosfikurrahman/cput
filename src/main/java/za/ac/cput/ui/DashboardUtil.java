package za.ac.cput.ui;

import za.ac.cput.domain.Scooter;
import za.ac.cput.service.ScooterService;
import za.ac.cput.ui.panel.components.LogoutListener;
import za.ac.cput.ui.panel.components.ScooterCard;
import za.ac.cput.ui.panel.components.ScooterCardCreator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DashboardUtil {
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 245);
    private static final Color ACCENT_COLOR = new Color(0, 123, 255);
    private static final Color CARD_COLOR = new Color(255, 255, 255);
    private static final Color TEXT_COLOR = new Color(33, 37, 41);
    private static final Color HEADER_COLOR = new Color(60, 60, 60);

    public static void handleLogout(LogoutListener logoutListener) {
        int confirm = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to logout?", "Confirm Logout",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (logoutListener != null) {
                logoutListener.onLogout();
            }
        }
    }

    private static JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    public static JButton createActionButton(String text, ActionListener listener, Color... buttonColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        Color backgroundColor = buttonColor.length > 0 ? buttonColor[0] : ACCENT_COLOR;
        button.setBackground(backgroundColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(listener);
        return button;
    }

    public static JLabel createImageLabel(Scooter scooter) {
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(150, 150));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        if (scooter.getImage() != null && scooter.getImage().getUrl() != null) {
            String imagePath = scooter.getImage().getUrl().replace("\"", "");
            String fullImagePath = "http://localhost:8080" + imagePath;

            try {
                URI uri = new URI(fullImagePath);
                URL url = uri.toURL();
                BufferedImage img = ImageIO.read(url);
                if (img != null) {
                    Image scaledImage = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                } else {
                    imageLabel.setText("Image not available");
                }
            } catch (Exception e) {
                imageLabel.setText("Failed to load image");
            }
        } else {
            imageLabel.setText("No image");
        }
        return imageLabel;
    }

    public static JPanel createDetailsPanel(Scooter scooter) {
        JPanel detailsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        detailsPanel.setBackground(CARD_COLOR);
        detailsPanel.add(createStyledLabel("Model: " + scooter.getModel()));
        detailsPanel.add(createStyledLabel("Year: " + scooter.getYear()));
        detailsPanel.add(createStyledLabel("Rental Per Day: R" + scooter.getRentalPerDay()));
        detailsPanel.add(createStyledLabel("Status: " + scooter.getStatus()));
        return detailsPanel;
    }

    public static void refreshDashboard(JPanel contentPanel, CardLayout cardLayout, Supplier<JPanel> panelSupplier) {
        contentPanel.removeAll();
        JPanel newDashboardPanel = panelSupplier.get();
        contentPanel.add(newDashboardPanel, "Dashboard");
        cardLayout.show(contentPanel, "Dashboard");
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public static void showDashboard(Consumer<String> updateContent) {
        updateContent.accept("Dashboard");
    }

    public static JPanel createDashboardPanel(ScooterService scooterService, String welcomeMessage,
                                              Consumer<Scooter> updateAction,
                                              Consumer<Scooter> deleteAction,
                                              Consumer<Scooter> bookAction) {
        JPanel dashboardPanel = new JPanel(new BorderLayout(20, 20));
        dashboardPanel.setBackground(BACKGROUND_COLOR);
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = new JLabel(welcomeMessage);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(TEXT_COLOR);
        dashboardPanel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel scooterCardsPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        scooterCardsPanel.setBackground(BACKGROUND_COLOR);
        List<Scooter> scooters = scooterService.getAll();

        for (Scooter scooter : scooters) {
            ScooterCard card = new ScooterCard(
                    scooter,
                    updateAction != null ? () -> updateAction.accept(scooter) : null,
                    deleteAction != null ? () -> deleteAction.accept(scooter) : null,
                    bookAction != null ? () -> bookAction.accept(scooter) : null
            );
            scooterCardsPanel.add(card);
        }

        JScrollPane scrollPane = new JScrollPane(scooterCardsPanel);
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        dashboardPanel.add(scrollPane, BorderLayout.CENTER);

        return dashboardPanel;
    }

    public static void performSearch(
            JPanel resultPanel,
            String query,
            ScooterService scooterService,
            ScooterCardCreator cardCreator) {

        resultPanel.removeAll();
        List<Scooter> results = scooterService.search(query);

        if (results.isEmpty()) {
            JLabel noResultLabel = new JLabel("No results found");
            noResultLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            noResultLabel.setForeground(TEXT_COLOR);
            resultPanel.add(noResultLabel);
        } else {
            for (Scooter scooter : results) {
                ScooterCard card = cardCreator.createCard(scooter);
                resultPanel.add(card);
            }
        }

        resultPanel.revalidate();
        resultPanel.repaint();
    }

    public static JPanel createSearchPanel(Consumer<String> searchAction) {
        JPanel searchPanel = new JPanel(new BorderLayout(20, 20));
        searchPanel.setBackground(BACKGROUND_COLOR);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setPreferredSize(new Dimension(100, 30)); // Fixed width of 100

        JButton searchButton = createActionButton("Search", e -> searchAction.accept(searchField.getText()));
        JPanel searchBarPanel = new JPanel(new BorderLayout());
        searchBarPanel.add(searchField, BorderLayout.CENTER);
        searchBarPanel.add(searchButton, BorderLayout.EAST);

        searchPanel.add(searchBarPanel, BorderLayout.NORTH);

        JPanel resultPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        resultPanel.setBackground(BACKGROUND_COLOR);
        searchPanel.add(resultPanel, BorderLayout.CENTER);

        searchPanel.putClientProperty("resultPanel", resultPanel);

        return searchPanel;
    }

    public static JButton createStyledButton(String text, Color foregroundColor, Color backgroundColor, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(foregroundColor);
        button.setBackground(backgroundColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(actionListener);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(backgroundColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(backgroundColor);
            }
        });

        return button;
    }

    public static JPanel createHeaderPanel(String dashboardTitle, List<String> tabs, Consumer<String> updateContent) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel systemLabel = new JLabel("Scooter Rental System");
        systemLabel.setFont(new Font("Arial", Font.BOLD, 24));
        systemLabel.setForeground(Color.WHITE);
        headerPanel.add(systemLabel, BorderLayout.WEST);

        JLabel dashboardLabel = new JLabel(dashboardTitle);
        dashboardLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        dashboardLabel.setForeground(Color.WHITE);
        headerPanel.add(dashboardLabel, BorderLayout.EAST);

        JPanel navPanel = createNavPanel(tabs, updateContent);
        headerPanel.add(navPanel, BorderLayout.SOUTH);

        return headerPanel;
    }

    private static JPanel createNavPanel(List<String> tabs, Consumer<String> updateContent) {
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        navPanel.setBackground(HEADER_COLOR);

        for (String tabName : tabs) {
            JButton tabButton = createStyledButton(
                    tabName,
                    Color.WHITE,
                    HEADER_COLOR,
                    e -> updateContent.accept(tabName)
            );
            navPanel.add(tabButton);
        }

        return navPanel;
    }

}
