package za.ac.cput.ui.panel;


import za.ac.cput.dto.request.CustomerRequest;
import za.ac.cput.domain.Customer;
import za.ac.cput.service.CustomerService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

public class CustomersPanel extends JPanel {
    private final CustomerService customerService;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JDialog addEditDialog;

    // Define colors
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 245);
    private static final Color HEADER_COLOR = new Color(60, 60, 60);
    private static final Color ADD_BUTTON_COLOR = new Color(40, 167, 69);
    private static final Color UPDATE_BUTTON_COLOR = new Color(0, 123, 255);
    private static final Color DELETE_BUTTON_COLOR = new Color(220, 53, 69);
    private static final Color TEXT_COLOR = new Color(33, 37, 41);

    public CustomersPanel(CustomerService customerService) {
        this.customerService = customerService;
        setLayout(new BorderLayout(10, 10));
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        initComponents();
        loadCustomers();
    }

    private void initComponents() {
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Customer Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton addButton = createStyledButton("Add Customer");
        addButton.addActionListener(e -> showAddEditDialog(null));
        headerPanel.add(addButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Customer table
        String[] columnNames = {"ID", "Name", "Email", "Phone", "NIC", "Update", "Delete"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5 || column == 6;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 5 || columnIndex == 6 ? JButton.class : Object.class;
            }
        };
        customerTable = new JTable(tableModel);
        customerTable.setRowHeight(40);
        customerTable.setFont(new Font("Arial", Font.PLAIN, 14));
        customerTable.setSelectionBackground(new Color(173, 216, 230));
        customerTable.setSelectionForeground(TEXT_COLOR);

        // Set custom renderers for all columns
        for (int i = 0; i < customerTable.getColumnCount(); i++) {
            if (i != 5 && i != 6) {
                customerTable.getColumnModel().getColumn(i).setCellRenderer(new CustomTableCellRenderer());
            }
        }

        customerTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer("Update", UPDATE_BUTTON_COLOR));
        customerTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor("Update", UPDATE_BUTTON_COLOR));
        customerTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer("Delete", DELETE_BUTTON_COLOR));
        customerTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor("Delete", DELETE_BUTTON_COLOR));

        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadCustomers() {
        tableModel.setRowCount(0);
        List<Customer> customers = customerService.getAll();
        for (Customer customer : customers) {
            Object[] rowData = {
                    customer.getId(),
                    customer.getName(),
                    customer.getEmail(),
                    customer.getPhone(),
                    customer.getNic(),
                    "Update",
                    "Delete"
            };
            tableModel.addRow(rowData);
        }
    }

    private void showAddEditDialog(Customer customer) {
        addEditDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add/Edit Customer", true);
        addEditDialog.setLayout(new BorderLayout(10, 10));
        addEditDialog.getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField nameField = createStyledTextField(customer != null ? customer.getName() : "");
        JTextField emailField = createStyledTextField(customer != null ? customer.getEmail() : "");
        JTextField phoneField = createStyledTextField(customer != null ? customer.getPhone() : "");
        JTextField nicField = createStyledTextField(customer != null ? customer.getNic() : "");
        JPasswordField passwordField = createStyledPasswordField();

        form.add(createStyledLabel("Name:"), gbc);
        form.add(nameField, gbc);
        form.add(createStyledLabel("Email:"), gbc);
        form.add(emailField, gbc);
        form.add(createStyledLabel("Phone:"), gbc);
        form.add(phoneField, gbc);
        form.add(createStyledLabel("NIC:"), gbc);
        form.add(nicField, gbc);
        form.add(createStyledLabel("Password:"), gbc);
        form.add(passwordField, gbc);

        JButton saveButton = createStyledButton("Save");
        saveButton.addActionListener(e -> {
            CustomerRequest customerRQ = new CustomerRequest();
            customerRQ.setName(nameField.getText());
            customerRQ.setEmail(emailField.getText());
            customerRQ.setPhone(phoneField.getText());
            customerRQ.setNic(nicField.getText());
            customerRQ.setPassword(new String(passwordField.getPassword()));

            if (customer == null) {
                customerService.create(customerRQ);
            } else {
                customerService.update(customer.getId(), customerRQ);
            }
            loadCustomers();
            addEditDialog.dispose();
        });

        addEditDialog.add(form, BorderLayout.CENTER);
        addEditDialog.add(saveButton, BorderLayout.SOUTH);
        addEditDialog.pack();
        addEditDialog.setLocationRelativeTo(this);
        addEditDialog.setVisible(true);
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

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(CustomersPanel.ADD_BUTTON_COLOR);
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

    private static class ButtonRenderer extends JButton implements TableCellRenderer {
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
        private final String action;
        private final Color backgroundColor;
        private boolean isPushed;

        public ButtonEditor(String action, Color backgroundColor) {
            super(new JCheckBox());
            this.action = action;
            this.backgroundColor = backgroundColor;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(Color.WHITE);
                button.setBackground(backgroundColor);
            }
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                performAction();
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        private void performAction() {
            int row = customerTable.getSelectedRow();
            if (row != -1) {
                Long id = (Long) customerTable.getValueAt(row, 0);
                if (action.equals("Update")) {
                    Customer customer = customerService.getById(id);
                    SwingUtilities.invokeLater(() -> showAddEditDialog(customer));
                } else if (action.equals("Delete")) {
                    SwingUtilities.invokeLater(() -> {
                        int confirm = JOptionPane.showConfirmDialog(
                                CustomersPanel.this,
                                "Are you sure you want to delete this customer?",
                                "Confirm Delete",
                                JOptionPane.YES_NO_OPTION
                        );
                        if (confirm == JOptionPane.YES_OPTION) {
                            customerService.delete(id);
                            loadCustomers();
                        }
                    });
                }
            }
        }
    }
}