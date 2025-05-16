import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import screens.AddEstablishmentScreen;
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

            // ScreenService.changeScreen(new DashBoard());
            // LoginRegisterScreen loginRegisterScreen = new LoginRegisterScreen();
            //ScreenService.changeScreen(new LoginRegisterScreen());


            LoginRegisterScreen loginRegisterScreen = new LoginRegisterScreen();
            //ScreenService.changeScreen(new AddDeviceScreen());
            ScreenService.changeScreen(new AddEstablishmentScreen("username"));
            //ScreenService.changeScreen(new AddDeviceScreen());
            


            frame.setVisible(true);
        });
    }
}
