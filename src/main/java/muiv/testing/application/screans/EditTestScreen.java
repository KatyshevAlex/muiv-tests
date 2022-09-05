package muiv.testing.application.screans;

import muiv.testing.application.App;
import muiv.testing.application.objects.Test;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.*;
import java.awt.*;

public class EditTestScreen extends JPanel {
    private Test test;
    private final JScrollPane sp;

    public EditTestScreen() {
        super(new BorderLayout());
        final JPanel top = new JPanel(new BorderLayout());
        final JPanel left = new JPanel();
        final JButton back = new JButton("Назад");

        back.addActionListener(App::home);
        left.add(back);
        top.add(left, BorderLayout.WEST);

        final JLabel titleScreen = new JLabel("Редактировать тестирование", JLabel.CENTER);
        top.add(titleScreen, BorderLayout.CENTER);

        final JPanel buttons = new JPanel();
        final JButton loadFromFile = new JButton("Загрузить из файла");
        loadFromFile.addActionListener(this::loadFromFile);
        buttons.add(loadFromFile);
        final JButton clear = new JButton("Очистить");
        clear.addActionListener(this::clear);
        buttons.add(clear);
        top.add(buttons, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        test = new Test();
        sp = new JScrollPane(test.getEditPanel());
        sp.setBorder(BorderFactory.createEmptyBorder());
        add(sp, BorderLayout.CENTER);
    }

    private void loadFromFile(final ActionEvent e) {
        final JFileChooser op = new JFileChooser();
        final FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Serialized Object files", Test.FILE_EXTENSION);
        op.setFileFilter(filter);
        final int returnVal = op.showOpenDialog(getRootPane());

        if ((returnVal == JFileChooser.APPROVE_OPTION) &&
                (test.numQuestions() == 0 ||
                        (JOptionPane.showConfirmDialog(getRootPane(), "Перезаписать выбранное тестирование?",
                                "Подтверждение",
                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE)
                                == JOptionPane.OK_OPTION
                        )
                )) {
            test.loadFromFile(op.getSelectedFile());
            sp.setViewportView(test.getEditPanel());
        }
    }

    private void clear(final ActionEvent e) {
        if (JOptionPane.showConfirmDialog(getRootPane(),
                    "Предупреждение: Это очистит экран.\n Продолжить?",
                "Предупреждение", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)
                    == JOptionPane.YES_OPTION) {
            test = new Test();
            sp.setViewportView(test.getEditPanel());
        }
    }
}
