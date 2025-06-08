package org.ucs.eco_energy;

// /**
//  * Hello world!
//  */
// public class App {
//     public static void main(String[] args) {
//         System.out.println("Hello World!");
//     }
// }

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.ucs.eco_energy.screens.LoginRegisterScreen;
import org.ucs.eco_energy.services.ScreenService;

public class App {
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
            ScreenService.changeScreen(new LoginRegisterScreen());


            LoginRegisterScreen loginRegisterScreen = new LoginRegisterScreen();
            ScreenService.changeScreen(loginRegisterScreen);


            frame.setVisible(true);
        });
    }
}
