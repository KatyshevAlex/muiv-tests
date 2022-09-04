package App;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.*;

public class TakeTestScreen extends JPanel {
    private final Test test;
    private final JScrollPane sp;

    public TakeTestScreen() {
        super(new BorderLayout());

        JPanel top = new JPanel(new BorderLayout());
        JButton back = new JButton("Назад");
        back.addActionListener(App::home);
        top.add(back, BorderLayout.WEST);
        JLabel title = new JLabel("Пройти тестирование", JLabel.CENTER);
        top.add(title, BorderLayout.CENTER);
        JButton loadFromFile = new JButton("Загрузить тестирование");
        loadFromFile.addActionListener(this::loadFromFile);
        top.add(loadFromFile, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        test = new Test();
        sp = new JScrollPane(test.getPanel(this));
        sp.setBorder(BorderFactory.createEmptyBorder());
        add(sp, BorderLayout.CENTER);
    }

    private void loadFromFile(ActionEvent e) {
        JFileChooser jfc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Serialized Object files", Test.FILE_EXTENSION);
        jfc.setFileFilter(filter);
        int returnVal = jfc.showOpenDialog(getRootPane());

        if ((returnVal == JFileChooser.APPROVE_OPTION) &&
                (test.numQuestions() == 0 ||
                        (JOptionPane.showConfirmDialog(getRootPane(), "Предупреждение: Вы потеряете ответы из текущего тестирование",
                                "Подтверждение", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE)
                                == JOptionPane.OK_OPTION
                        )
                )) {
            test.loadFromFile(jfc.getSelectedFile());
            sp.setViewportView(test.getPanel(this));
        }
    }
}
