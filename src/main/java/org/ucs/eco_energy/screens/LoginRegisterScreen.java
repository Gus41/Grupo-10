package org.ucs.eco_energy.screens;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;

import org.ucs.eco_energy.repositories.EstablishmentRepository;
import org.ucs.eco_energy.repositories.UserRepository;
import org.ucs.eco_energy.services.ScreenService;

import org.ucs.eco_energy.models.Establishment;
import org.ucs.eco_energy.models.User;


public class LoginRegisterScreen extends JPanel {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField contactField;
    private JLabel messageLabel;
    private UserRepository userRepository;
    private ScreenService screenService = new ScreenService();


    public LoginRegisterScreen() {
        userRepository = new UserRepository();
        setLayout(new GridBagLayout());
        setBackground(new Color(30, 30, 30));

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(40, 40, 40));
        container.setMaximumSize(new Dimension(800, Integer.MAX_VALUE));
        container.setBorder(new CompoundBorder(new RoundedBorder(20), new EmptyBorder(30, 40, 10, 40)));

        JLabel titleLabel = new JLabel("Login / Register");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Adicionando os labels antes dos campos de entrada
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameLabel.setForeground(Color.WHITE);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordLabel.setForeground(Color.WHITE);

        JLabel contactLabel = new JLabel("Contact         ");
        contactLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contactLabel.setForeground(Color.WHITE);

        usernameField = createTextField("Username");
        passwordField = createPasswordField("Password");
        contactField = createTextField("Contact (Register only)");

        JButton loginButton = createButton("Login");
        JButton registerButton = createButton("Register");

        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> register());

        messageLabel = new JLabel(" ");
        messageLabel.setForeground(Color.LIGHT_GRAY);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Organizando os componentes na tela
        container.add(titleLabel);
        container.add(Box.createVerticalStrut(20));
        container.add(usernameLabel);
        container.add(usernameField);
        container.add(Box.createVerticalStrut(12));
        container.add(passwordLabel);
        container.add(passwordField);
        container.add(Box.createVerticalStrut(12));
        container.add(contactLabel);
        container.add(contactField);
        container.add(Box.createVerticalStrut(20));
        container.add(loginButton);
        container.add(Box.createVerticalStrut(10));
        container.add(registerButton);
        container.add(Box.createVerticalStrut(20));
        container.add(messageLabel);

        add(container);
    }

    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField(15);
        styleField(field, placeholder);
        return field;
    }

    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(15);
        styleField(field, placeholder);
        return field;
    }

    private void styleField(JTextComponent field, String placeholder) {
        field.setMaximumSize(new Dimension(320, 35));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(new Color(55, 55, 55));
        field.setCaretColor(Color.WHITE);

        field.setForeground(Color.WHITE);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));

        btn.setBackground(new Color(100, 100, 100));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);

        Dimension buttonSize = new Dimension(180, 32);
        btn.setMaximumSize(buttonSize);
        btn.setPreferredSize(buttonSize);

        btn.setBorder(new CompoundBorder(new RoundedBorder(20), new EmptyBorder(4, 12, 4, 12)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(130, 130, 130));
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(100, 100, 100));
            }
        });

        return btn;
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        Optional<User> matched = userRepository
                .getUsers()
                .stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst();

        if (matched.isPresent()) {
            EstablishmentRepository repo = new EstablishmentRepository();
            Establishment establishment = repo.getEstablishmentByUser(username);
            ScreenService.changeScreen(new DashBoard(establishment));
        } else {
            messageLabel.setText("Invalid credentials.");
            messageLabel.setForeground(Color.RED);
        }
    }

    private void register() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String contact = contactField.getText().trim();

        boolean exists = userRepository
                .getUsers()
                .stream()
                .anyMatch(u -> u.getUsername().equals(username));

        if (exists) {
            messageLabel.setText("Username already exists.");
            messageLabel.setForeground(Color.ORANGE);
        } else if (username.isEmpty() || password.isEmpty() || contact.isEmpty()
                || username.equals("Username") || password.equals("Password") || contact.equals("Contact (Register only)")) {
            messageLabel.setText("All fields are required.");
            messageLabel.setForeground(Color.YELLOW);
        } else {
            User newUser = new User(username, password, contact);
            userRepository.addUser(newUser);
            ScreenService.changeScreen(new AddEstablishmentScreen(newUser));
        }
    }

    static class RoundedBorder implements Border {
        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
        }

        public boolean isBorderOpaque() {
            return false;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(Color.GRAY);
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }
}
