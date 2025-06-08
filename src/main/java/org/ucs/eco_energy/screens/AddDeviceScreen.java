package org.ucs.eco_energy.screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.ucs.eco_energy.models.Category;
import org.ucs.eco_energy.models.Device; // Import your enum
import org.ucs.eco_energy.repositories.AddDeviceRepository;
import org.ucs.eco_energy.repositories.EstablishmentRepository;

import org.ucs.eco_energy.services.ScreenService;

public class AddDeviceScreen extends JPanel {

    private JTextField nameField;
    private JTextField powerField;
    private JTextField timeField;
    private JComboBox<Category> categoryComboBox; 
    //private JTextField newCategoryField; 
    
    public AddDeviceScreen() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(45, 45, 45));

        JLabel title = new JLabel("Add New Device");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(getBackground());

        // --- Name Field ---
        JLabel nameLabel = new JLabel("Device Name:");
        // ... (styling as before)
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameLabel.setForeground(Color.WHITE);
        formPanel.add(nameLabel);
        nameField = new JTextField();
        // ... (styling as before)
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameField.setForeground(Color.BLACK);
        nameField.setBackground(new Color(255, 255, 255));
        nameField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        formPanel.add(nameField);
        formPanel.add(Box.createVerticalStrut(15));


        // --- Power Field ---
        JLabel powerLabel = new JLabel("Power Device (Watts):"); // Be specific with unit
        // ... (styling as before)
        powerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        powerLabel.setForeground(Color.WHITE);
        formPanel.add(powerLabel);
        powerField = new JTextField();
        // ... (styling as before)
        powerField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        powerField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        powerField.setForeground(Color.BLACK);
        powerField.setBackground(new Color(255, 255, 255));
        powerField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        formPanel.add(powerField);
        formPanel.add(Box.createVerticalStrut(15));

        // --- Time Field ---
        JLabel timeLabel = new JLabel("Time of Use (hours/day):"); // Be specific
        // ... (styling as before)
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        timeLabel.setForeground(Color.WHITE);
        formPanel.add(timeLabel);
        timeField = new JTextField();
        // ... (styling as before)
        timeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        timeField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        timeField.setForeground(Color.BLACK);
        timeField.setBackground(new Color(255, 255, 255));
        timeField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        formPanel.add(timeField);
        formPanel.add(Box.createVerticalStrut(15));


        // --- Category ComboBox ---
        JLabel categoryLabel = new JLabel("Category:");
        // ... (styling as before)
        categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryLabel.setForeground(Color.WHITE);
        formPanel.add(categoryLabel);
        categoryComboBox = new JComboBox<Category>(); // Correctly typed
        updateCategoryComboBox();
        // ... (styling as before)
        categoryComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        categoryComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryComboBox.setForeground(Color.BLACK);
        categoryComboBox.setBackground(new Color(255, 255, 255));
        formPanel.add(categoryComboBox);

        formPanel.add(Box.createVerticalStrut(20));

        // --- Add Button ---
        JButton addButton = new JButton("Add Device");
        // ... (styling as before)
        addButton.setBackground(new Color(70, 130, 180));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addButton.setFocusPainted(false);
        addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addButton.setAlignmentX(Component.RIGHT_ALIGNMENT);


        addButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addButton.setBackground(new Color(100, 149, 237));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addButton.setBackground(new Color(70, 130, 180));
            }
        });

        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String powerStr = powerField.getText().trim();
            String timeStr = timeField.getText().trim();

            // --- Input Validation ---
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in the device name.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (powerStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in the device power.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (timeStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in the time of use.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double power;
            double time;
            try {
                power = Double.parseDouble(powerStr);
                time = Double.parseDouble(timeStr);
                if (power <= 0 || time <= 0) {
                    JOptionPane.showMessageDialog(this, "Power and Time of Use must be positive numbers.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for Power and Time of Use.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            
            Category selectedEnumCategory; 
            selectedEnumCategory = (Category) categoryComboBox.getSelectedItem(); 

            if (selectedEnumCategory == null) { // Should not happen if combobox is populated, but good check
                JOptionPane.showMessageDialog(this, "Please select a category.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            
            Device newDevice = new Device(name, power, time, selectedEnumCategory);
            // If using default constructor and setters:
            //Device newDevice = new Device();
            //newDevice.setName(name);
            //newDevice.setPower(power);
            //newDevice.setTimeOfUse(time);
            //newDevice.setCategory(selectedEnumCategory);


            // --- Save Device (Repository) ---
            AddDeviceRepository addDeviceRepository = new AddDeviceRepository();
            try {
                addDeviceRepository.saveDevice(newDevice); // Make sure your repository handles Device object correctly

                JOptionPane.showMessageDialog(this,
                        "Added device: " + newDevice.getName() +
                        " in category: " + newDevice.getCategory().getDescription(), // Use getDisplayName() for the message
                        "Device Added", JOptionPane.INFORMATION_MESSAGE);

                // --- Clear Fields ---
                nameField.setText("");
                powerField.setText("");
                timeField.setText("");
                // newCategoryField.setText(""); // If you implement this field
                if (categoryComboBox.getItemCount() > 0) {
                    categoryComboBox.setSelectedIndex(0);
                }
                // createNewCategoryCheckBox.setSelected(false);
                // newCategoryField.setEnabled(false); // If you implement the "new category" feature
            } catch (Exception ex) { // Catch potential exceptions from saving
                JOptionPane.showMessageDialog(this, "Error saving device: " + ex.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace(); // For debugging
            }
        });

        formPanel.add(addButton);
        add(formPanel, BorderLayout.CENTER);

        // Initialize newCategoryField if you plan to use it
        //newCategoryField = new JTextField();
        // You would also need to add it to a panel, likely near categoryComboBox,
        // and manage its visibility/enabled state based on a checkbox or "Other" selection.
    }

    private void updateCategoryComboBox() {
        categoryComboBox.removeAllItems();
        for (Category category : Category.values()) {
            categoryComboBox.addItem(category); // Adds the enum constant itself
        }
        // Optionally set a default selection
        if (categoryComboBox.getItemCount() > 0) {
            categoryComboBox.setSelectedIndex(0); // Select the first item by default
        }
    }
}