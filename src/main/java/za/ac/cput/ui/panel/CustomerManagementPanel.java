package za.ac.cput.ui.panel;

import za.ac.cput.ui.model.CustomerUIModel;
import za.ac.cput.ui.service.UICustomerService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CustomerManagementPanel extends JPanel {
    private JTable customerTable;
    private JButton addButton, editButton, deleteButton;
    private UICustomerService customerService;

    public CustomerManagementPanel(UICustomerService customerService) {
        this.customerService = customerService;
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        customerTable = new JTable(); // TODO: Implement table model
        JScrollPane scrollPane = new JScrollPane(customerTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addCustomer());
        editButton.addActionListener(e -> editCustomer());
        deleteButton.addActionListener(e -> deleteCustomer());

        refreshCustomerList();
    }

    private void refreshCustomerList() {
        List<CustomerUIModel> customers = customerService.getAllCustomers();
        // TODO: Update table model with customers
    }

    private void addCustomer() {
        // TODO: Open AddCustomerDialog
    }

    private void editCustomer() {
        // TODO: Open EditCustomerDialog with selected customer
    }

    private void deleteCustomer() {
        // TODO: Confirm and delete selected customer
    }
}