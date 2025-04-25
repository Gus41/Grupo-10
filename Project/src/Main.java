import javax.swing.*;
import java.awt.*;

import screens.AddDeviceScreen;
import screens.AddEstablishmentScreen;
import screens.DashBoard;
import screens.LoginRegisterScreen;
import services.ScreenService;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Monitor de Consumo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLayout(new BorderLayout());
            frame.setLocationRelativeTo(null);

            ScreenService.initialize(frame);

            LoginRegisterScreen loginRegisterScreen = new LoginRegisterScreen();
            ScreenService.changeScreen(new AddEstablishmentScreen());

            frame.setVisible(true);
        });
    }
}
