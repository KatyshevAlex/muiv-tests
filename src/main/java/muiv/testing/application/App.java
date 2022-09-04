package muiv.testing.application;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMoonlightIJTheme;
import muiv.testing.application.fraims.MainFrame;

import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;

public class App {
    private static MainFrame mainFrame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlatMoonlightIJTheme.setup();
            mainFrame = new MainFrame();
        });
    }

    public static void home(ActionEvent e) {
        mainFrame.home();
    }

    public static void editTest(ActionEvent e) {
        mainFrame.editTest();
    }

    public static void takeTest(ActionEvent e) {
        mainFrame.takeTest();
    }
}
