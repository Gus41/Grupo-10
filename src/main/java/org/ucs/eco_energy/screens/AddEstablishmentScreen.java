package org.ucs.eco_energy.screens;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.ucs.eco_energy.models.Device;
import org.ucs.eco_energy.models.Establishment;
import org.ucs.eco_energy.models.User;
import org.ucs.eco_energy.repositories.EstablishmentRepository;
import org.ucs.eco_energy.services.ScreenService;

public class AddEstablishmentScreen extends JPanel {

    String username;

    private JTextField nameField;
    private EstablishmentRepository establishmentRepository = new EstablishmentRepository();

    public AddEstablishmentScreen(User user) {
        this.username = username;

        setLayout(new GridBagLayout());
        setBackground(new Color(45, 45, 45));

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(getBackground());
        container.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Add New Establishment");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        container.add(title);

        JLabel nameLabel = new JLabel("Establishment Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(nameLabel);

        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(250, 30));
        nameField.setMaximumSize(new Dimension(250, 30));
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameField.setForeground(Color.BLACK);
        nameField.setBackground(Color.WHITE);
        nameField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(nameField);
        container.add(Box.createVerticalStrut(15));

        JButton addButton = new JButton("Add Establishment");
        addButton.setBackground(new Color(70, 130, 180));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addButton.setFocusPainted(false);
        addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(AddEstablishmentScreen.this, "Please enter a valid establishment name.", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Device[] devices = {};
                Establishment newEstablishment = new Establishment();
                newEstablishment.addUser(user);
                newEstablishment.setName(name);

                establishmentRepository.addEstablishment(newEstablishment);
                ScreenService.changeScreen(new DashBoard(newEstablishment));
                nameField.setText("");
            }
        });

        container.add(addButton);

        add(container);
    }
}
