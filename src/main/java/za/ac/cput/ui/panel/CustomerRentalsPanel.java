package za.ac.cput.ui.panel;

import za.ac.cput.domain.Customer;
import za.ac.cput.domain.RentalDetail;
import za.ac.cput.enums.RentalStatus;
import za.ac.cput.service.RentalService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CustomerRentalsPanel extends JPanel {
    private final Customer customer;
    private final RentalService rentalService;
    private DefaultTableModel tableModel;

    // Define colors (reuse from RentalsPanel)
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 245);
    private static final Color TEXT_COLOR = new Color(33, 37, 41);

    // Define colors for status badges
    private static final Color PENDING_COLOR = new Color(255, 193, 7);
    private static final Color ACCEPTED_COLOR = new Color(0, 123, 255);
    private static final Color REJECTED_COLOR = new Color(220, 53, 69);
    private static final Color COMPLETED_COLOR = new Color(40, 167, 69);

    public CustomerRentalsPanel(Customer customer, RentalService rentalService) {
        this.customer = customer;
        this.rentalService = rentalService;
        setLayout(new BorderLayout(10, 10));
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        initComponents();
        loadRentals();
    }

    private void initComponents() {
        // Create table
        String[] columnNames = {"ID", "Scooter Model", "Start Date", "Number of Days", "Total Price", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // All columns are non-editable
            }
        };
        JTable rentalTable = new JTable(tableModel);
        rentalTable.setRowHeight(40);
        rentalTable.setFont(new Font("Arial", Font.PLAIN, 14));
        rentalTable.setSelectionBackground(new Color(173, 216, 230));
        rentalTable.setSelectionForeground(TEXT_COLOR);

        // Set custom renderers for all columns
        for (int i = 0; i < rentalTable.getColumnCount(); i++) {
            if (i != 5) { // Don't apply custom renderer to status column
                rentalTable.getColumnModel().getColumn(i).setCellRenderer(new CustomTableCellRenderer());
            }
        }
        rentalTable.getColumnModel().getColumn(5).setCellRenderer(new StatusRenderer());

        JScrollPane scrollPane = new JScrollPane(rentalTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Add a refresh button
        JButton refreshButton = createStyledButton(new Color(40, 167, 69));
        refreshButton.addActionListener(e -> loadRentals());
        add(refreshButton, BorderLayout.SOUTH);
    }

    private void loadRentals() {
        tableModel.setRowCount(0);
        List<RentalDetail> rentals = rentalService.getRentalsByCustomerId(customer.getId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (RentalDetail rental : rentals) {
            Object[] row = {
                    rental.getId(),
                    rental.getScooter().getModel(),
                    rental.getStartDate().format(formatter),
                    rental.getNoOfDates(),
                    rental.getTotalPrice(),
                    rental.getStatus()
            };
            tableModel.addRow(row);
        }
    }

    private JButton createStyledButton(Color backgroundColor) {
        JButton button = new JButton("Refresh");
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private static class CustomTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setFont(new Font("Arial", Font.PLAIN, 14));
            return c;
        }
    }

    private class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value instanceof RentalStatus) {
                return createStatusLabel((RentalStatus) value);
            }
            return label;
        }
    }

    private JLabel createStatusLabel(RentalStatus status) {
        JLabel label = new JLabel(status.toString());
        label.setOpaque(true);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        label.setFont(new Font("Arial", Font.BOLD, 12));
        switch (status) {
            case PENDING:
                label.setBackground(PENDING_COLOR);
                break;
            case ACCEPTED:
                label.setBackground(ACCEPTED_COLOR);
                break;
            case REJECTED:
                label.setBackground(REJECTED_COLOR);
                break;
            case COMPLETED:
                label.setBackground(COMPLETED_COLOR);
                break;
        }
        label.setForeground(Color.WHITE);
        return label;
    }
}
