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
import org.ucs.eco_energy.models.Device;
import org.ucs.eco_energy.models.Establishment;
import org.ucs.eco_energy.repositories.AddDeviceRepository;
import org.ucs.eco_energy.repositories.EstablishmentRepository;

import org.ucs.eco_energy.services.ScreenService;
import org.ucs.eco_energy.screens.DashBoard;

public class AddDeviceScreen extends JPanel {

    private JTextField nameField;
    private JTextField powerField;
    private JTextField timeField;
    private JComboBox<Category> categoryComboBox; 
    //private JTextField newCategoryField; 
    
    public AddDeviceScreen(Establishment establishment) {
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
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameLabel.setForeground(Color.WHITE);
        formPanel.add(nameLabel);
        nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameField.setForeground(Color.BLACK);
        nameField.setBackground(new Color(255, 255, 255));
        nameField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        formPanel.add(nameField);
        formPanel.add(Box.createVerticalStrut(15));


        // --- Power Field ---
        JLabel powerLabel = new JLabel("Power Device (Watts):"); // Be specific with unit
        powerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        powerLabel.setForeground(Color.WHITE);
        formPanel.add(powerLabel);
        powerField = new JTextField();
        powerField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        powerField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        powerField.setForeground(Color.BLACK);
        powerField.setBackground(new Color(255, 255, 255));
        powerField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        formPanel.add(powerField);
        formPanel.add(Box.createVerticalStrut(15));

        // --- Time Field ---
        JLabel timeLabel = new JLabel("Time of Use (hours/day):"); // Be specific
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        timeLabel.setForeground(Color.WHITE);
        formPanel.add(timeLabel);
        timeField = new JTextField();
        timeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        timeField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        timeField.setForeground(Color.BLACK);
        timeField.setBackground(new Color(255, 255, 255));
        timeField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        formPanel.add(timeField);
        formPanel.add(Box.createVerticalStrut(15));


        // --- Category ComboBox ---
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryLabel.setForeground(Color.WHITE);
        formPanel.add(categoryLabel);
        categoryComboBox = new JComboBox<Category>();
        updateCategoryComboBox();
        categoryComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        categoryComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryComboBox.setForeground(Color.BLACK);
        categoryComboBox.setBackground(new Color(255, 255, 255));
        formPanel.add(categoryComboBox);

        formPanel.add(Box.createVerticalStrut(20));

        // --- Add Button ---
        JButton addButton = new JButton("Add Device");
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

            if (selectedEnumCategory == null) {
                JOptionPane.showMessageDialog(this, "Please select a category.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            
            Device newDevice = new Device(name, power, time, selectedEnumCategory);

            AddDeviceRepository addDeviceRepository = new AddDeviceRepository();
            try {
                addDeviceRepository.saveDevice(newDevice,establishment);
                establishment.addDevice(newDevice);
                ScreenService.changeScreen(new DashBoard(establishment));
                System.out.println("Antes de trocar de tela");


            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving device: " + ex.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        formPanel.add(addButton);
        add(formPanel, BorderLayout.CENTER);

    }

    private void updateCategoryComboBox() {
        categoryComboBox.removeAllItems();
        for (Category category : Category.values()) {
            categoryComboBox.addItem(category);
        }

        if (categoryComboBox.getItemCount() > 0) {
            categoryComboBox.setSelectedIndex(0);
        }
    }
}