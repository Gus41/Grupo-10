package screens;

import models.Category;
import models.Device;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AddDeviceScreen extends JPanel {

    private JTextField nameField;
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

        JCheckBox createNewCategoryCheckBox = new JCheckBox("Create New Category");
        createNewCategoryCheckBox.setBackground(getBackground());
        createNewCategoryCheckBox.setForeground(Color.WHITE);
        createNewCategoryCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(createNewCategoryCheckBox);

        newCategoryField = new JTextField();
        newCategoryField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        newCategoryField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        newCategoryField.setForeground(Color.BLACK);
        newCategoryField.setBackground(new Color(255, 255, 255));
        newCategoryField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        newCategoryField.setEnabled(false); // Inicia desabilitado
        formPanel.add(newCategoryField);

        createNewCategoryCheckBox.addActionListener(e -> {
            boolean selected = createNewCategoryCheckBox.isSelected();
            newCategoryField.setEnabled(selected);
        });

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
            Category selectedCategory = (Category) categoryComboBox.getSelectedItem();

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
                // Cria a nova categoria
                Category newCategory = new Category(categories.size() + 1, newCategoryName);
                categories.add(newCategory);
                selectedCategory = newCategory;
                updateCategoryComboBox();
            }

            Device newDevice = new Device(0, name, selectedCategory);

            JOptionPane.showMessageDialog(this, "Added device: " + newDevice.getName() + " in category " + newDevice.getCategory().getName());

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
        for (Category category : categories) {
            categoryComboBox.addItem(category.getName());
        }
    }
}
