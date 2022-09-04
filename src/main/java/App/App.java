package App;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMoonlightIJTheme;

import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;

public class App
{
    private static MainFrame f;
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> {
            if (!FlatMoonlightIJTheme.setup())
                System.err.println("Error: not able to install look and feel");
            f = new MainFrame();
        });
    }

    public static void home(ActionEvent e) {
        f.home();
    }

    public static void editTest(ActionEvent e) {
        f.editTest();
    }

    public static void takeTest(ActionEvent e) {
        f.takeTest();
    }
}
