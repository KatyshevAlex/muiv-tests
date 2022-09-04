package muiv.testing.application.fraims;

import muiv.testing.application.screans.EditTestScreen;
import muiv.testing.application.screans.HomeScreen;
import muiv.testing.application.screans.TakeTestScreen;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class MainFrame extends JFrame {
    private final HomeScreen homeScreen = new HomeScreen();
    private final EditTestScreen editTest = new EditTestScreen();
    private final TakeTestScreen takeTest = new TakeTestScreen();


    public MainFrame() {
        super("Контроль знаний сотрудника");
        setMinimumSize(new Dimension(600, 400));
        setPreferredSize(new Dimension(900, 600));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(homeScreen);
        pack();
        setVisible(true);
    }

    public void home() {
        setContentPane(homeScreen);
        revalidate();
        repaint();
    }

    public void editTest() {
        setContentPane(editTest);
        revalidate();
        repaint();
    }

    public void takeTest() {
        setContentPane(takeTest);
        revalidate();
        repaint();
    }
}
