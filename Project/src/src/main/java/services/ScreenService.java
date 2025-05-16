package services;

import javax.swing.*;
import java.awt.*;

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
