package za.ac.cput.ui.panel;

import za.ac.cput.dto.request.UserRequest;
import za.ac.cput.domain.User;
import za.ac.cput.enums.UserRole;
import za.ac.cput.service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmployeesPanel extends JPanel {
    private final UserService userService;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JDialog addEditDialog;

    // Define colors
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 245);
    private static final Color HEADER_COLOR = new Color(60, 60, 60);
    private static final Color ADD_BUTTON_COLOR = new Color(40, 167, 69);
    private static final Color UPDATE_BUTTON_COLOR = new Color(0, 123, 255);
    private static final Color DELETE_BUTTON_COLOR = new Color(220, 53, 69);
    private static final Color TEXT_COLOR = new Color(33, 37, 41);

    public EmployeesPanel(UserService userService) {
        this.userService = userService;
        setLayout(new BorderLayout(10, 10));
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        initComponents();
        loadEmployees();
    }

    private void initComponents() {
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Employee Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton addButton = createStyledButton("Add Employee");
        addButton.addActionListener(e -> showAddEditDialog(null));
        headerPanel.add(addButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Employee table
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
        employeeTable = new JTable(tableModel);
        employeeTable.setRowHeight(40);
        employeeTable.setFont(new Font("Arial", Font.PLAIN, 14));
        employeeTable.setSelectionBackground(new Color(173, 216, 230));
        employeeTable.setSelectionForeground(TEXT_COLOR);
        employeeTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor("Update", UPDATE_BUTTON_COLOR));
        employeeTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor("Delete", DELETE_BUTTON_COLOR));

        // Set custom renderers for all columns
        for (int i = 0; i < employeeTable.getColumnCount(); i++) {
            if (i != 5 && i != 6) {
                employeeTable.getColumnModel().getColumn(i).setCellRenderer(new CustomTableCellRenderer());
            }
        }

        employeeTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer("Update", UPDATE_BUTTON_COLOR));
        employeeTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor("Update", UPDATE_BUTTON_COLOR));
        employeeTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer("Delete", DELETE_BUTTON_COLOR));
        employeeTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor("Delete", DELETE_BUTTON_COLOR));

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadEmployees() {
        tableModel.setRowCount(0);  // Clear existing data
        List<User> employees = userService.getAll().stream()
                .filter(user -> user.getUserRole() == UserRole.EMPLOYEE)
                .toList();
        for (User employee : employees) {
            Object[] rowData = {
                    employee.getId(),
                    employee.getName(),
                    employee.getEmail(),
                    employee.getPhone(),
                    employee.getNic(),
                    "Update",
                    "Delete"
            };
            tableModel.addRow(rowData);
        }
    }

    private void showAddEditDialog(User employee) {
        addEditDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add/Edit Employee", true);
        addEditDialog.setLayout(new BorderLayout(10, 10));
        addEditDialog.getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField nameField = createStyledTextField(employee != null ? employee.getName() : "");
        JTextField emailField = createStyledTextField(employee != null ? employee.getEmail() : "");
        JTextField phoneField = createStyledTextField(employee != null ? employee.getPhone() : "");
        JTextField nicField = createStyledTextField(employee != null ? employee.getNic() : "");
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
            UserRequest userRQ = new UserRequest();
            userRQ.setName(nameField.getText());
            userRQ.setEmail(emailField.getText());
            userRQ.setPhone(phoneField.getText());
            userRQ.setNic(nicField.getText());
            userRQ.setPassword(new String(passwordField.getPassword()));

            if (employee == null) {
                userService.create(userRQ);
            } else {
                userService.update(employee.getId(), userRQ);
            }
            loadEmployees();
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
        button.setBackground(EmployeesPanel.ADD_BUTTON_COLOR);
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

    private static class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
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
            int row = employeeTable.getSelectedRow();
            if (row != -1) {
                Long id = (Long) employeeTable.getValueAt(row, 0);
                if (action.equals("Update")) {
                    User employee = userService.getById(id);
                    SwingUtilities.invokeLater(() -> showAddEditDialog(employee));
                } else if (action.equals("Delete")) {
                    SwingUtilities.invokeLater(() -> {
                        int confirm = JOptionPane.showConfirmDialog(
                                EmployeesPanel.this,
                                "Are you sure you want to delete this employee?",
                                "Confirm Delete",
                                JOptionPane.YES_NO_OPTION
                        );
                        if (confirm == JOptionPane.YES_OPTION) {
                            userService.delete(id);
                            loadEmployees();
                        }
                    });
                }
            }
        }
    }
}