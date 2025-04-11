import javax.swing.*;
import java.awt.*;
import screens.DashBoard;
import models.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Monitor de Consumo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 500);
            frame.setLayout(new BorderLayout());
            frame.setLocationRelativeTo(null);

            DashBoard dashBoard = new DashBoard();
            frame.add(dashBoard, BorderLayout.CENTER);

            frame.setVisible(true);
        });
    }
}
