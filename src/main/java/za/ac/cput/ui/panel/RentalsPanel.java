package za.ac.cput.ui.panel;


import za.ac.cput.domain.RentalDetail;
import za.ac.cput.enums.RentalStatus;
import za.ac.cput.service.RentalService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RentalsPanel extends JPanel {
    private final RentalService rentalService;
    private JTable rentalTable;
    private DefaultTableModel tableModel;
    private JDialog detailDialog;

    // Define colors
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 245);
    private static final Color HEADER_COLOR = new Color(60, 60, 60);
    private static final Color ADD_BUTTON_COLOR = new Color(40, 167, 69);
    private static final Color UPDATE_BUTTON_COLOR = new Color(0, 123, 255);
    private static final Color DELETE_BUTTON_COLOR = new Color(220, 53, 69);
    private static final Color TEXT_COLOR = new Color(33, 37, 41);

    // Define colors for status badges
    private static final Color PENDING_COLOR = new Color(255, 193, 7);
    private static final Color ACCEPTED_COLOR = new Color(0, 123, 255);
    private static final Color REJECTED_COLOR = new Color(220, 53, 69);
    private static final Color COMPLETED_COLOR = new Color(40, 167, 69);

    public RentalsPanel(RentalService rentalService) {
        this.rentalService = rentalService;
        setLayout(new BorderLayout(10, 10));
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        initComponents();
        loadRentals();
    }

    private void initComponents() {
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Rental Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        // Rental table
        String[] columnNames = {"ID", "Customer", "Scooter", "Start Date", "No. of Days", "Status", "Total Price", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Make only the Actions column editable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 7 ? JButton.class : Object.class;
            }
        };
        rentalTable = new JTable(tableModel);
        rentalTable.setRowHeight(40);
        rentalTable.setFont(new Font("Arial", Font.PLAIN, 14));
        rentalTable.setSelectionBackground(new Color(173, 216, 230));
        rentalTable.setSelectionForeground(TEXT_COLOR);

        // Set custom renderers for all columns
        for (int i = 0; i < rentalTable.getColumnCount(); i++) {
            if (i != 5 && i != 7) {
                rentalTable.getColumnModel().getColumn(i).setCellRenderer(new CustomTableCellRenderer());
            }
        }

        rentalTable.getColumnModel().getColumn(5).setCellRenderer(new StatusRenderer());
        rentalTable.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer("View/Edit", UPDATE_BUTTON_COLOR));
        rentalTable.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor("View/Edit", UPDATE_BUTTON_COLOR));

        JScrollPane scrollPane = new JScrollPane(rentalTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadRentals() {
        tableModel.setRowCount(0);
        List<RentalDetail> rentals = rentalService.getAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (RentalDetail rental : rentals) {
            Object[] rowData = {
                    rental.getId(),
                    rental.getCustomer().getName(),
                    rental.getScooter().getModel(),
                    rental.getStartDate().format(formatter),
                    rental.getNoOfDates(),
                    rental.getStatus(),
                    rental.getTotalPrice(),
                    "View/Edit"
            };
            tableModel.addRow(rowData);
        }
    }

    private void showDetailDialog(RentalDetail rental) {
        detailDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Rental Details", true);
        detailDialog.setLayout(new BorderLayout(10, 10));
        detailDialog.getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        addDetailRow(detailsPanel, "ID:", rental.getId().toString(), gbc);
        addDetailRow(detailsPanel, "Customer:", rental.getCustomer().getName(), gbc);
        addDetailRow(detailsPanel, "Scooter:", rental.getScooter().getModel().toString(), gbc);
        addDetailRow(detailsPanel, "Start Date:", rental.getStartDate().toString(), gbc);
        addDetailRow(detailsPanel, "No. of Days:", String.valueOf(rental.getNoOfDates()), gbc);
        addDetailRow(detailsPanel, "Total Price:", String.valueOf(rental.getTotalPrice()), gbc);
        addDetailRow(detailsPanel, "Current Status:", createStatusLabel(rental.getStatus()), gbc);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        actionPanel.setBackground(BACKGROUND_COLOR);
        switch (rental.getStatus()) {
            case PENDING:
                addActionButton(actionPanel, "Approve", () -> updateRentalStatus(rental, RentalStatus.ACCEPTED));
                addActionButton(actionPanel, "Reject", () -> updateRentalStatus(rental, RentalStatus.REJECTED));
                break;
            case ACCEPTED:
                addActionButton(actionPanel, "Complete", () -> updateRentalStatus(rental, RentalStatus.COMPLETED));
                break;
            case REJECTED:
            case COMPLETED:
                // No action buttons for REJECTED or COMPLETED statuses
                break;
        }

        detailDialog.add(detailsPanel, BorderLayout.CENTER);
        detailDialog.add(actionPanel, BorderLayout.SOUTH);
        detailDialog.pack();
        detailDialog.setLocationRelativeTo(this);
        detailDialog.setVisible(true);
    }

    private void addDetailRow(JPanel panel, String label, String value, GridBagConstraints gbc) {
        panel.add(createStyledLabel(label), gbc);
        panel.add(createStyledLabel(value), gbc);
    }

    private void addDetailRow(JPanel panel, String label, JLabel valueLabel, GridBagConstraints gbc) {
        panel.add(createStyledLabel(label), gbc);
        panel.add(valueLabel, gbc);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(TEXT_COLOR);
        return label;
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

    private void addActionButton(JPanel panel, String label, Runnable action) {
        JButton button = createStyledButton(label, UPDATE_BUTTON_COLOR);
        button.addActionListener(e -> {
            action.run();
            JOptionPane.showMessageDialog(detailDialog, "Rental status updated successfully.");
            loadRentals();
            detailDialog.dispose();
        });
        panel.add(button);
    }

    private void updateRentalStatus(RentalDetail rental, RentalStatus newStatus) {
        try {
            switch (newStatus) {
                case ACCEPTED:
                    rentalService.acceptRental(rental.getId());
                    break;
                case REJECTED:
                    rentalService.rejectRental(rental.getId());
                    break;
                case COMPLETED:
                    rentalService.completeRental(rental.getId());
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected status: " + newStatus);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(detailDialog, "Error updating rental status: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private class CustomTableCellRenderer extends DefaultTableCellRenderer {
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

    private class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer(String text, Color backgroundColor) {
            setText(text);
            setOpaque(true);
            setFont(new Font("Arial", Font.BOLD, 12));
            setForeground(Color.WHITE);
            setBackground(backgroundColor);
            setBorderPainted(false);
            setFocusPainted(false);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private final Color backgroundColor;

        public ButtonEditor(String action, Color backgroundColor) {
            super(new JCheckBox());
            this.backgroundColor = backgroundColor;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            button.setFont(new Font("Arial", Font.BOLD, 12));
            button.setForeground(Color.WHITE);
            button.setBackground(backgroundColor);
            return button;
        }

        public Object getCellEditorValue() {
            if ("View/Edit".equals(label)) {
                int row = rentalTable.getSelectedRow();
                if (row != -1) {
                    Long id = (Long) rentalTable.getValueAt(row, 0);
                    RentalDetail rental = rentalService.getById(id);
                    SwingUtilities.invokeLater(() -> showDetailDialog(rental));
                }
            }
            return label;
        }
    }
}