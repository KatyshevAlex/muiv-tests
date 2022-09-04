package App;

import javax.swing.*;
import java.awt.*;
import javax.swing.ImageIcon;
import java.net.URL;

public class HomeScreen extends JPanel {

    public HomeScreen() {
        super();
        URL url = getClass().getResource("/muiv.png");
        JLabel picture = new JLabel ();
        picture.setAlignmentX(Component.CENTER_ALIGNMENT);
        // check if url is null in case someone (forgot to put/removed) image in jar
        if (url == null) {
            System.err.println("Unable to load image");
        } else {
            ImageIcon logo = new ImageIcon(url);
            picture.setIcon(logo);
        }

        // set layout to a boxlayout
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        // init buttons
        JButton createTest = new JButton("Создать тестирование");
        createTest.setMaximumSize(new Dimension(250, 90));
        createTest.setPreferredSize(new Dimension(250, 90));
        createTest.setFont(createTest.getFont().deriveFont(20f));
        createTest.setFocusable(true);
        createTest.addActionListener(App::editTest);

        JButton takeTest = new JButton("Пройти тестирование");
        takeTest.setMaximumSize(new Dimension(250, 90));
        takeTest.setPreferredSize(new Dimension(250, 90));
        takeTest.setFont(takeTest.getFont().deriveFont(20f));
        takeTest.setFocusable(true);
        takeTest.addActionListener(App::takeTest);

        JButton quit = new JButton("Выход");
        quit.setMaximumSize(new Dimension(100, 50));
        quit.setPreferredSize(new Dimension(100, 50));
        quit.setFont(quit.getFont().deriveFont(20f));
        quit.setFocusable(true);
        quit.addActionListener(e -> System.exit(0));

        createTest.setAlignmentX(Component.CENTER_ALIGNMENT);
        takeTest.setAlignmentX(Component.CENTER_ALIGNMENT);
        quit.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalGlue());
        add(picture);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(createTest);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(takeTest);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(quit);
        add(Box.createVerticalGlue());
    }
}
