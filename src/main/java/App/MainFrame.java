package App;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class MainFrame extends JFrame {
    private final HomeScreen hs = new HomeScreen();
    private final EditTestScreen editTest = new EditTestScreen();
    private final TakeTestScreen takeTest = new TakeTestScreen();

    public MainFrame() {
        super("Контроль знаний сотрудника");
        setMinimumSize(new Dimension(600, 400));
        setPreferredSize(new Dimension(900, 600));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(hs);
        pack();
        setVisible(true);
    }

    public void home() {
        setContentPane(hs);
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
