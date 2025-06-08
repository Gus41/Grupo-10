package org.ucs.eco_energy.services;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ScreenService {
    private static JFrame frame;

    public static void initialize(JFrame mainFrame) {
        frame = mainFrame;
    }

    public static void changeScreen(JPanel newScreen) {
        frame.getContentPane().removeAll();
        frame.getContentPane().add(newScreen, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }
}
