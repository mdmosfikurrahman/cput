package za.ac.cput.ui.panel;

import za.ac.cput.dto.request.ScooterRequest;
import za.ac.cput.domain.Scooter;
import za.ac.cput.enums.Model;
import za.ac.cput.enums.ScooterStatus;
import za.ac.cput.service.FileStoreService;
import za.ac.cput.service.ScooterService;
import za.ac.cput.ui.dialog.UpdateScooterDialog;
import za.ac.cput.util.FileUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScootersPanel extends JPanel {
    private static final Logger logger = Logger.getLogger(ScootersPanel.class.getName());
    private final ScooterService scooterService;
    private final FileStoreService fileStoreService;
    private JTable scooterTable;
    private DefaultTableModel tableModel;
    private JDialog addEditDialog;
    private File selectedImageFile;
    private JLabel imagePreviewLabel;
    private byte[] currentImageData;

    // Define colors
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 245);
    private static final Color HEADER_COLOR = new Color(60, 60, 60);
    private static final Color ADD_BUTTON_COLOR = new Color(40, 167, 69);
    private static final Color UPDATE_BUTTON_COLOR = new Color(0, 123, 255);
    private static final Color DELETE_BUTTON_COLOR = new Color(220, 53, 69);
    private static final Color TEXT_COLOR = new Color(33, 37, 41);

    public ScootersPanel(ScooterService scooterService, FileStoreService fileStoreService) {
        this.scooterService = scooterService;
        this.fileStoreService = fileStoreService;
        setLayout(new BorderLayout(10, 10));
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        initComponents();
        loadScooters();
    }

    private void initComponents() {
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Scooter Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton addButton = createStyledButton("Add Scooter", ADD_BUTTON_COLOR);
        addButton.addActionListener(e -> showAddEditDialog(null));
        headerPanel.add(addButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Scooter table
        String[] columnNames = {"ID", "Model", "Year", "Rental Per Day", "Status", "Update", "Delete"};
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
        scooterTable = new JTable(tableModel);
        scooterTable.setRowHeight(40);
        scooterTable.setFont(new Font("Arial", Font.PLAIN, 14));
        scooterTable.setSelectionBackground(new Color(173, 216, 230));
        scooterTable.setSelectionForeground(TEXT_COLOR);

        // Set custom renderers for all columns
        for (int i = 0; i < scooterTable.getColumnCount(); i++) {
            if (i != 5 && i != 6) {
                scooterTable.getColumnModel().getColumn(i).setCellRenderer(new CustomTableCellRenderer());
            }
        }

        scooterTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer("Update", UPDATE_BUTTON_COLOR));
        scooterTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor("Update", UPDATE_BUTTON_COLOR));
        scooterTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer("Delete", DELETE_BUTTON_COLOR));
        scooterTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor("Delete", DELETE_BUTTON_COLOR));

        JScrollPane scrollPane = new JScrollPane(scooterTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadScooters() {
        tableModel.setRowCount(0);
        List<Scooter> scooters = scooterService.getAll();
        for (Scooter scooter : scooters) {
            Object[] rowData = {
                    scooter.getId(),
                    scooter.getModel(),
                    scooter.getYear(),
                    scooter.getRentalPerDay(),
                    scooter.getStatus(),
                    "Update",
                    "Delete"
            };
            tableModel.addRow(rowData);
        }
    }

    private void showAddEditDialog(Scooter scooter) {
        addEditDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add/Edit Scooter", true);
        addEditDialog.setLayout(new BorderLayout(10, 10));
        addEditDialog.getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JComboBox<Model> modelField = new JComboBox<>(Model.values());
        JTextField yearField = createStyledTextField(scooter != null ? String.valueOf(scooter.getYear()) : "");
        JTextField rentalPerDayField = createStyledTextField(scooter != null ? String.valueOf(scooter.getRentalPerDay()) : "");
        JComboBox<ScooterStatus> statusField = new JComboBox<>(ScooterStatus.values());
        JButton imageButton = createStyledButton("Select Image", ADD_BUTTON_COLOR);
        imagePreviewLabel = new JLabel("No image selected");

        if (scooter != null) {
            modelField.setSelectedItem(scooter.getModel());
            statusField.setSelectedItem(scooter.getStatus());
            if (scooter.getImage() != null && scooter.getImage().getUrl() != null) {
                String imagePath = scooter.getImage().getUrl().replace("\"", "");
                String fullImagePath = "http://localhost:8080" + imagePath;
                displayImage(fullImagePath);
            }
        }

        imageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif");
            fileChooser.setFileFilter(filter);
            int result = fileChooser.showOpenDialog(addEditDialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedImageFile = fileChooser.getSelectedFile();
                displayImage(selectedImageFile.getAbsolutePath());
            }
        });

        form.add(createStyledLabel("Model:"), gbc);
        form.add(modelField, gbc);
        form.add(createStyledLabel("Year:"), gbc);
        form.add(yearField, gbc);
        form.add(createStyledLabel("Rental Per Day:"), gbc);
        form.add(rentalPerDayField, gbc);
        form.add(createStyledLabel("Status:"), gbc);
        form.add(statusField, gbc);
        form.add(createStyledLabel("Image:"), gbc);
        form.add(imageButton, gbc);
        form.add(createStyledLabel("Preview:"), gbc);
        form.add(imagePreviewLabel, gbc);

        JButton saveButton = createStyledButton("Save", ADD_BUTTON_COLOR);
        saveButton.addActionListener(e -> {
            ScooterRequest scooterRQ = new ScooterRequest();
            scooterRQ.setModel((Model) modelField.getSelectedItem());
            scooterRQ.setYear(Integer.parseInt(yearField.getText()));
            scooterRQ.setRentalPerDay(Integer.parseInt(rentalPerDayField.getText()));
            scooterRQ.setScooterStatus((ScooterStatus) statusField.getSelectedItem());

            try {
                if (selectedImageFile != null) {
                    if (scooter == null) {
                        scooterService.create(scooterRQ, FileUtils.convertFileToMultipartFile(selectedImageFile));
                    } else {
                        scooterService.update(scooter.getId(), scooterRQ, FileUtils.convertFileToMultipartFile(selectedImageFile));
                    }
                } else if (currentImageData != null) {
                    if (scooter != null) {
                        scooterService.update(scooter.getId(), scooterRQ, null);
                    }
                } else {
                    if (scooter == null) {
                        scooterService.create(scooterRQ, null);
                    } else {
                        scooterService.update(scooter.getId(), scooterRQ, null);
                    }
                }
                loadScooters();
                addEditDialog.dispose();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(addEditDialog, "Error processing image file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(addEditDialog, "Error saving scooter: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
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

    private void displayImage(URL imageUrl) {
        try {
            BufferedImage img = ImageIO.read(imageUrl);
            if (img != null) {
                Image scaledImage = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                imagePreviewLabel.setIcon(new ImageIcon(scaledImage));
                imagePreviewLabel.setText("");
            } else {
                imagePreviewLabel.setIcon(null);
                imagePreviewLabel.setText("Unable to load image");
            }
        } catch (IOException e) {
            e.printStackTrace();
            imagePreviewLabel.setIcon(null);
            imagePreviewLabel.setText("Error loading image");
        }
    }

    private void displayImage(String imagePath) {
        try {
            BufferedImage img;
            if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                // It's a URL
                URL url = new URL(imagePath);
                img = ImageIO.read(url);
            } else {
                // It's a local file path
                File file = new File(imagePath);
                img = ImageIO.read(file);
            }

            if (img != null) {
                Image scaledImage = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                imagePreviewLabel.setIcon(new ImageIcon(scaledImage));
                imagePreviewLabel.setText("");
            } else {
                imagePreviewLabel.setIcon(null);
                imagePreviewLabel.setText("Unable to load image");
            }
        } catch (IOException e) {
            logger.log(Level.parse("Error loading image from path: {}"), imagePath, e);
            imagePreviewLabel.setIcon(null);
            imagePreviewLabel.setText("Error loading image: " + e.getMessage());
        }
    }

    private void displayImage(byte[] imageData) {
        if (imageData != null && imageData.length > 0) {
            ImageIcon imageIcon = new ImageIcon(imageData);
            Image image = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            imagePreviewLabel.setIcon(new ImageIcon(image));
            imagePreviewLabel.setText("");
            currentImageData = imageData;
        } else {
            imagePreviewLabel.setIcon(null);
            imagePreviewLabel.setText("No image selected");
            currentImageData = null;
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
            int row = scooterTable.getSelectedRow();
            if (row != -1) {
                Long id = (Long) scooterTable.getValueAt(row, 0);
                if (action.equals("Update")) {
                    Scooter scooter = scooterService.getById(id);

                    // Open update dialog
                    UpdateScooterDialog dialog = new UpdateScooterDialog(scooter, scooterService);
                    dialog.setVisible(true);

                } else if (action.equals("Delete")) {
                    SwingUtilities.invokeLater(() -> {
                        int confirm = JOptionPane.showConfirmDialog(
                                ScootersPanel.this,
                                "Are you sure you want to delete this scooter?",
                                "Confirm Delete",
                                JOptionPane.YES_NO_OPTION
                        );
                        if (confirm == JOptionPane.YES_OPTION) {
                            scooterService.delete(id);
                            loadScooters();
                        }
                    });
                }
            }
        }
    }


}