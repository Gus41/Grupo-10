package org.ucs.eco_energy.screens;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import org.ucs.eco_energy.models.Device;
import org.ucs.eco_energy.models.Establishment;
import org.ucs.eco_energy.repositories.AddDeviceRepository;
import org.ucs.eco_energy.services.ScreenService;

public class DashBoard extends JPanel {

    private JLabel deviceDetailsLabel;
    private Device currentDevice;

    /**
     * @param establishment
     */
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


        // String[] devices = {"Refrigerator", "Shower", "Computer"};
        

        AddDeviceRepository deviceRepository = new AddDeviceRepository();
        java.util.List<Device> devices = deviceRepository.getAllDevices();


//        java.util.List<Device> dev = establishment.getDevices();

  //      for (Device device : dev) {
    //        JButton btn = new JButton(device.getName());
      //      btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        //    btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
          //  btn.setBackground(new Color(60, 60, 60));
            //btn.setForeground(Color.WHITE);
     //       btn.setFocusPainted(false);
       //     btn.setBorder(new RoundedBorder(10)); // Rounded border added back

//            btn.addActionListener(e -> showDeviceDetails(device));
  //          deviceList.add(btn);
    //        deviceList.add(Box.createVerticalStrut(10));
      //  }

        JButton addButton = new JButton("Add Device");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addButton.setBackground(new Color(80, 80, 80));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorder(new RoundedBorder(10));
        addButton.addActionListener(e-> ScreenService.changeScreen(new AddDeviceScreen()));

        deviceList.add(Box.createVerticalStrut(20));
        deviceList.add(addButton);

        leftPanel.add(titleLabel, BorderLayout.NORTH);
        leftPanel.add(deviceList, BorderLayout.CENTER);

        add(leftPanel, BorderLayout.WEST);

        // ======= Center Panel (Category Filter + Details) =======
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Category filter (dummy)
        JPanel categoryFilterPanel = new JPanel();
        categoryFilterPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        categoryFilterPanel.add(new JLabel("Category: "));

        String[] categories = {"All", "Kitchen", "Bathroom", "Office"};
        JComboBox<String> categoryCombo = new JComboBox<>(categories);
        categoryFilterPanel.add(categoryCombo);

        centerPanel.add(categoryFilterPanel, BorderLayout.NORTH);

        // Device details label (example placeholder)
        deviceDetailsLabel = new JLabel("All data will be rendered here");
        deviceDetailsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        centerPanel.add(deviceDetailsLabel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // ======= Right Panel (Time Filter) =======
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

            // Hover effect
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

        add(rightPanel, BorderLayout.EAST);
    }

    private void showConsumptionByFilter(String filter){
        Double consumption = this.currentDevice.getConsumptionByDay();
        if(filter == "Day"){
            deviceDetailsLabel.setText("The consumption by day is: " + String.valueOf(consumption) + "KW/h.");   
        }
        else if(filter == "Week"){
            deviceDetailsLabel.setText("The consumption by week is: " + String.valueOf(consumption*7) + "KW/h.");   
        }
        else if(filter == "Month"){
            deviceDetailsLabel.setText("The consumption by month is: " + String.valueOf(consumption*30) + "KW/h.");   
        }       
        else if(filter == "Year"){
            deviceDetailsLabel.setText("The consumption by year is: " + String.valueOf(consumption*365) + "KW/h.");   
        }       
    }

    private void showDeviceDetails(Device currentDevice) {
        deviceDetailsLabel.setText("Details for: " + currentDevice.getName());
        this.currentDevice = currentDevice;
    }

    private void AddDeviceRepository() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // Custom rounded border class
    static  class RoundedBorder implements Border {
        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }


        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
        }

        public boolean isBorderOpaque() {
            return false;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(Color.GRAY);
            g.drawRoundRect(x, y, width-1, height-1, radius, radius);
        }
    }
}