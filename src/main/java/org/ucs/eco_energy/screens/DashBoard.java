package org.ucs.eco_energy.screens;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.*;
import javax.swing.border.*;

import org.ucs.eco_energy.models.Device;
import org.ucs.eco_energy.models.Establishment;
import org.ucs.eco_energy.repositories.AddDeviceRepository;
import org.ucs.eco_energy.services.ApiService;
import org.ucs.eco_energy.services.ScreenService;

public class DashBoard extends JPanel {

    private JLabel deviceDetailsLabel;
    private Device currentDevice;

    public DashBoard(Establishment establishment) {
        setLayout(new BorderLayout());

        // ======= Left Panel =======
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(250, 0));
        leftPanel.setBackground(new Color(45, 45, 45));

        JLabel titleLabel = new JLabel("Registered Devices", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JPanel deviceList = new JPanel();
        deviceList.setLayout(new BoxLayout(deviceList, BoxLayout.Y_AXIS));
        deviceList.setBackground(leftPanel.getBackground());
        deviceList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Device device : establishment.getDevices()) {
            JButton btn = new JButton(device.getName());
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btn.setBackground(new Color(60, 60, 60));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(new RoundedBorder(10));

            btn.addActionListener(e -> showDeviceDetails(device));
            deviceList.add(btn);
            deviceList.add(Box.createVerticalStrut(10));
        }

        JButton addButton = new JButton("Add Device");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addButton.setBackground(new Color(80, 80, 80));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorder(new RoundedBorder(10));
        addButton.addActionListener(e -> ScreenService.changeScreen(new AddDeviceScreen(establishment)));

        deviceList.add(Box.createVerticalStrut(20));
        deviceList.add(addButton);

        leftPanel.add(titleLabel, BorderLayout.NORTH);
        leftPanel.add(deviceList, BorderLayout.CENTER);
        add(leftPanel, BorderLayout.WEST);

        // ======= Center Panel =======
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel categoryFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        categoryFilterPanel.add(new JLabel("Category: "));
        String[] categories = {"All", "Kitchen", "Bathroom", "Office"};
        JComboBox<String> categoryCombo = new JComboBox<>(categories);
        categoryFilterPanel.add(categoryCombo);
        centerPanel.add(categoryFilterPanel, BorderLayout.NORTH);

        DeviceChartPanel chartPanel = new DeviceChartPanel(establishment.getDevices());
        centerPanel.add(chartPanel, BorderLayout.CENTER);

        deviceDetailsLabel = new JLabel("Click a filter or select a device.");
        deviceDetailsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        centerPanel.add(deviceDetailsLabel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        // ======= Right Panel =======
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel filterLabel = new JLabel("Filter by:");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        filterLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(filterLabel);
        rightPanel.add(Box.createVerticalStrut(10));

        String[] timeFilters = {"Day", "Week", "Month", "Year"};
        for (String filter : timeFilters) {
            JButton btn = new JButton(filter);
            btn.setMaximumSize(new Dimension(120, 35));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.setBackground(new Color(90, 90, 90));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBorder(new CompoundBorder(new RoundedBorder(15), new EmptyBorder(8, 20, 8, 20)));

            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(new Color(110, 110, 110));
                }
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(new Color(90, 90, 90));
                }
            });

            btn.addActionListener(e -> showConsumptionByFilter(filter));
            rightPanel.add(btn);
            rightPanel.add(Box.createVerticalStrut(10));
        }

        rightPanel.add(Box.createVerticalGlue());
        JButton helpButton = new JButton("Pedir Dica");
        helpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        helpButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        helpButton.setBackground(new Color(0, 120, 215));
        helpButton.setForeground(Color.WHITE);
        helpButton.setFocusPainted(false);
        helpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        helpButton.setBorder(new CompoundBorder(new RoundedBorder(10), new EmptyBorder(8, 20, 8, 20)));

        helpButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                helpButton.setBackground(new Color(0, 140, 235));
            }
            public void mouseExited(MouseEvent e) {
                helpButton.setBackground(new Color(0, 120, 215));
            }
        });

        helpButton.addActionListener(e -> {
            helpButton.setEnabled(false);

            new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    ApiService apiService = new ApiService();
                    return apiService.GetTip(establishment.getDevices());
                }

                @Override
                protected void done() {
                    try {
                        String message = get();

                        JTextArea textArea = new JTextArea(message);
                        textArea.setWrapStyleWord(true);
                        textArea.setLineWrap(true);
                        textArea.setEditable(false);
                        textArea.setFocusable(false);
                        textArea.setBackground(new Color(240, 240, 240));
                        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));

                        JScrollPane scrollPane = new JScrollPane(textArea);
                        scrollPane.setPreferredSize(new Dimension(400, 200));

                        JOptionPane.showMessageDialog(
                                DashBoard.this,
                                scrollPane,
                                "Sugestão de Economia",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(
                                DashBoard.this,
                                "Erro ao obter dica. Tente novamente.",
                                "Erro",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } finally {
                        helpButton.setEnabled(true);
                    }
                }
            }.execute();
        });

        rightPanel.add(helpButton);
        add(rightPanel, BorderLayout.EAST);
    }

    private void showConsumptionByFilter(String filter) {
        if (this.currentDevice == null) {
            deviceDetailsLabel.setText("No device selected.");
            return;
        }

        double consumption = this.currentDevice.getConsumptionByDay();
        switch (filter) {
            case "Day":
                deviceDetailsLabel.setText("Daily consumption: " + consumption + " KW/h.");
                break;
            case "Week":
                deviceDetailsLabel.setText("Weekly consumption: " + (consumption * 7) + " KW/h.");
                break;
            case "Month":
                deviceDetailsLabel.setText("Monthly consumption: " + (consumption * 30) + " KW/h.");
                break;
            case "Year":
                deviceDetailsLabel.setText("Yearly consumption: " + (consumption * 365) + " KW/h.");
                break;
        }
    }

    private void showDeviceDetails(Device currentDevice) {
        this.currentDevice = currentDevice;
        deviceDetailsLabel.setText("Selected device: " + currentDevice.getName());
    }

    class DeviceChartPanel extends JPanel {
        private List<Device> devices;

        public DeviceChartPanel(List<Device> devices) {
            this.devices = devices;
            setPreferredSize(new Dimension(400, 300));
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (devices == null || devices.isEmpty()) {
                g.drawString("No devices registered", 20, 20);
                return;
            }

            int width = getWidth();
            int height = getHeight();
            int barWidth = Math.max(40, width / devices.size() - 20);

            double maxConsumption = devices.stream()
                    .mapToDouble(d -> d.getPower() * d.getTimeUse())
                    .max().orElse(1.0);

            int x = 20;

            for (Device device : devices) {
                double consumption = device.getPower() * device.getTimeUse();
                int barHeight = (int) ((consumption / maxConsumption) * (height - 60));

                g.setColor(new Color(100, 149, 237));
                g.fillRect(x, height - barHeight - 30, barWidth, barHeight);

                g.setColor(Color.BLACK);
                g.drawString(device.getName(), x, height - 10);

                x += barWidth + 20;
            }
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
