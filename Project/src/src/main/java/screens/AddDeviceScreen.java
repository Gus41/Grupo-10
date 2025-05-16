package screens;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import model.Device;
import model.Category;
import repositories.AddDeviceRepository;

public class AddDeviceScreen extends JPanel {

    private JTextField nameField;
    private JTextField powerField;
    private JTextField timeField;
    private JComboBox<String> categoryComboBox;
    private JTextField newCategoryField;
    private List<Category> categories;

    public AddDeviceScreen() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(45, 45, 45));

        categories = new ArrayList<>();

        JLabel title = new JLabel("Add New Device");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(getBackground());

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

        JLabel powerLabel = new JLabel("Power Device:");
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

        JLabel timeLabel = new JLabel("Time of Use");
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

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryLabel.setForeground(Color.WHITE);
        formPanel.add(categoryLabel);

        categoryComboBox = new JComboBox<>();
        updateCategoryComboBox();
        categoryComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        categoryComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryComboBox.setForeground(Color.BLACK);
        categoryComboBox.setBackground(new Color(255, 255, 255));
        formPanel.add(categoryComboBox);

        formPanel.add(Box.createVerticalStrut(20));

        JButton addButton = new JButton("Add Device");
        addButton.setBackground(new Color(70, 130, 180));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addButton.setFocusPainted(false);
        addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        addButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addButton.setBackground(new Color(100, 149, 237));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addButton.setBackground(new Color(70, 130, 180));
            }
        });

        addButton.addActionListener(e -> {
            String name = nameField.getText();
            Double power = Double.valueOf(powerField.getText());
            Double time = Double.valueOf(timeField.getText());

            String selectedCategory = (String) categoryComboBox.getSelectedItem();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in the device name.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (createNewCategoryCheckBox.isSelected()) {
                String newCategoryName = newCategoryField.getText().trim();
                if (newCategoryName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid category name.", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                newCategoryName = selectedCategory;
                updateCategoryComboBox();
            }

            Device newDevice = new Device(0, name, selectedCategory,power, time);
            AddDeviceRepository addDeviceRepository = new AddDeviceRepository();
            addDeviceRepository.saveDevice(newDevice);

            JOptionPane.showMessageDialog(this, "Added device: " + newDevice.getName() + " in category " + newDevice.getCategory());

            nameField.setText("");
            newCategoryField.setText("");
            categoryComboBox.setSelectedIndex(0);
            createNewCategoryCheckBox.setSelected(false);
            newCategoryField.setEnabled(false);
        });

        formPanel.add(addButton);

        add(formPanel, BorderLayout.CENTER);
    }

    private void updateCategoryComboBox() {
        categoryComboBox.removeAllItems();
        categoryComboBox.addItem("Eletrodomésticos");
        categoryComboBox.addItem("Eletrônicos");
    }
}
