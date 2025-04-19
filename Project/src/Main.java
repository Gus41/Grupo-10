import javax.swing.*;
import java.awt.*;
import screens.DashBoard;
import screens.LoginRegisterScreen;
import models.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Monitor de Consumo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLayout(new BorderLayout());
            frame.setLocationRelativeTo(null);

            //screens
            LoginRegisterScreen loginRegisterScreen = new LoginRegisterScreen();
            DashBoard dashBoard = new DashBoard();

            frame.add(loginRegisterScreen, BorderLayout.CENTER);

            frame.setVisible(true);
        });
    }
}
