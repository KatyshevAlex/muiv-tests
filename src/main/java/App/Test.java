package App;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;


public class Test {
    public static final String FILE_EXTENSION = "ser";
    public static final Color RED = new Color(255, 116, 165, 76);
    public static final Color GREEN = new Color(78, 239, 205, 76);
    private ArrayList<TestQuestion> questions = new ArrayList<>();

    private JPanel editTestPanel;
    private GridBagConstraints editTestConstraints;
    private JPanel testScreen;
    private JPanel buttons;
    private JButton delete;
    private JButton submit;
    private String title;
    private JTextField titleField;
    private int questionNumber = 0;

    private static class WidthPanel extends JPanel {
        public WidthPanel(final LayoutManager layout) {
            super(layout);
        }

        @Override
        public Dimension getPreferredSize() {
            final int h = super.getPreferredSize().height;
            final int w = getParent().getSize().width;
            return new Dimension(w, h);
        }
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile(final File f) {
        String tempTitle;
        ArrayList<TestQuestion> tempQuestions;
        final JRootPane jrp =
            testScreen != null ? testScreen.getRootPane()
            : null;

        try (FileInputStream in = new FileInputStream(f);
                BufferedInputStream buf = new BufferedInputStream(in);
                ObjectInputStream ois = new ObjectInputStream(buf)) {
            tempTitle = (String)ois.readObject();
            tempQuestions = (ArrayList<TestQuestion>)ois.readObject();

            if (tempTitle == null) {
                JOptionPane.showMessageDialog(jrp, "добавлена пустая строка", "Предупреждение",
                        JOptionPane.WARNING_MESSAGE);
            }

            for (final Object o : tempQuestions)
                if (!(o instanceof TestQuestion))
                    throw new ClassCastException("Cannot cast " + o.getClass() +
                            " to TestQuestion.");
        } catch (final IOException e) {
            JOptionPane.showMessageDialog(jrp, "Невозможно загрузить файл\n"
                    + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (final ClassNotFoundException e) {
            JOptionPane.showMessageDialog(jrp, "Неверный формат файла\n"
                    + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (final ClassCastException e) {
            JOptionPane.showMessageDialog(jrp, "Неверный формат файла\n"
                    + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (final ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(jrp,
                    "Формат ответа не поддерживается", "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } catch (final NullPointerException e) {
            JOptionPane.showMessageDialog(jrp,
                    "Формат ответа не поддерживается", "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        final String filename = f.getName();

        title = tempTitle != "" ? tempTitle
            : filename.endsWith("." + FILE_EXTENSION) ? filename.substring(0, filename.length() - 4)
            : filename;

        questions = tempQuestions;
        questionNumber = questions.size();
        testScreen = null;
    }

    public void saveToFile() {
        try {
            for (final TestQuestion q : questions) {
                q.save();
            }
        } catch (final EmptyQuestionException e) {
            JOptionPane.showMessageDialog(testScreen.getRootPane(), "Для вопроса " + e.getNumber() +
                    " небыл выбран корректный ответ", "Пустой Вопрос",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        final JFileChooser jfc = new JFileChooser();
        final FileNameExtensionFilter filter = new FileNameExtensionFilter("Serialized class files",
                FILE_EXTENSION);
        jfc.setFileFilter(filter);
        if (jfc.showSaveDialog(testScreen.getRootPane()) != JFileChooser.APPROVE_OPTION)
            return;
        title = titleField.getText();
        String name = jfc.getSelectedFile().getPath();
        if (!name.endsWith("." + FILE_EXTENSION)) {
            name += "." + FILE_EXTENSION;
        }
        final File file = new File(name);
        try (FileOutputStream f = new FileOutputStream(file);
                BufferedOutputStream buf = new BufferedOutputStream(f);
                ObjectOutputStream out = new ObjectOutputStream(buf)) {
            out.writeObject(title);
            out.writeObject(questions);
        } catch (final IOException e) {
            JOptionPane.showMessageDialog(testScreen.getRootPane(), "Unable to write to file:\n" +
                    e.getMessage(),
                    "IO Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(testScreen.getRootPane(), "Успешно сохранено в файл ",
                "Готово!", JOptionPane.INFORMATION_MESSAGE);
    }

    public int numQuestions() {
        return questionNumber;
    }

    public JPanel getEditPanel() {
        if (testScreen != null) return testScreen;

        testScreen = new JPanel(new GridBagLayout());
        final GridBagConstraints testScreenConstraints = new GridBagConstraints();
        testScreenConstraints.gridx = 0;
        testScreenConstraints.anchor = GridBagConstraints.NORTHWEST;
        testScreenConstraints.weightx = 1;
        testScreenConstraints.fill = GridBagConstraints.HORIZONTAL;

        final JPanel titlePanel = new JPanel();
        titlePanel.add(new JLabel("Тема: "));
        titleField = new JTextField(title, 20);
        titlePanel.add(titleField);
        testScreen.add(titlePanel, testScreenConstraints);

        buttons = new JPanel();

        final JButton multipleChoice = new JButton("Один верный ответ");
        multipleChoice.addActionListener((e) -> addQuestion(new MultipleChoice(++questionNumber)));
        buttons.add(multipleChoice);

        final JButton multipleSelection = new JButton("Несколько верных ответов");
        multipleSelection.addActionListener((e) ->
                addQuestion(new MultipleSelection(++questionNumber)));
        buttons.add(multipleSelection);

        final JButton fillBlank = new JButton("Свободное заполнение");
        fillBlank.addActionListener((e) -> addQuestion(new FillBlank(++questionNumber)));
        buttons.add(fillBlank);

        delete = new JButton("Удалить");
        delete.addActionListener(this::deleteQuestion);

        initEditTestPanel();
        testScreen.add(editTestPanel, testScreenConstraints);
        testScreen.add(buttons, testScreenConstraints);

        final JButton save = new JButton("Сохранить");
        save.setPreferredSize(new Dimension(200, 50));
        save.setMaximumSize(new Dimension(200, 50));
        save.addActionListener((e) -> saveToFile());
        testScreenConstraints.fill = GridBagConstraints.NONE;
        testScreenConstraints.anchor = GridBagConstraints.CENTER;
        testScreen.add(save, testScreenConstraints);

        testScreenConstraints.weighty = 1;
        testScreen.add(Box.createVerticalGlue(), testScreenConstraints);

        return testScreen;
    }

    private void initEditTestPanel() {
        editTestPanel = new JPanel(new GridBagLayout());
        editTestConstraints = new GridBagConstraints();
        editTestConstraints.gridx = 0;
        editTestConstraints.weightx = 1;
        editTestConstraints.insets = new Insets(0, 30, 0, 0);
        editTestConstraints.anchor = GridBagConstraints.NORTHWEST;
        editTestConstraints.fill = GridBagConstraints.HORIZONTAL;
        for (final TestQuestion item : questions) {
            editTestPanel.add(item.getPanelEditable(), editTestConstraints);
        }
        if (questions.size() > 0) {
            buttons.add(delete);
        }
    }

    private void addQuestion(final TestQuestion q) {
        questions.add(q);
        editTestPanel.add(q.getPanelEditable(), editTestConstraints);
        if (questionNumber == 1) {
            buttons.add(delete);
        }
        testScreen.revalidate();
        testScreen.repaint();
    }

    private void deleteQuestion(final ActionEvent e) {
        questionNumber--;
        questions.remove(questionNumber);
        editTestPanel.remove(questionNumber);
        if (questionNumber == 0) {
            buttons.remove(3);
        }
        testScreen.revalidate();
        testScreen.repaint();
    }

    public JPanel getPanel(final JPanel parent) {
        if (testScreen != null) {
            return testScreen;
        }
        testScreen = new WidthPanel(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 30, 0, 30);
        if (questions.size() == 0) {
            if (title != null) {
                title = null;
                JOptionPane.showMessageDialog(parent.getRootPane(),
                        "В тестирование не добавлено вопросов. ",
                        "Пустое тестирование", JOptionPane.WARNING_MESSAGE);
            }

            final JLabel info = new JLabel("Нажмите кнопку \"Загрузить тестирование\"");
            final Font biggerFont = info.getFont().deriveFont(24f);
            info.setFont(biggerFont);
            testScreen.add(info);
            return testScreen;
        }
        gbc.fill = GridBagConstraints.HORIZONTAL;
        final JLabel titleLabel = new JLabel(encloseInHTML(title), JLabel.CENTER);
        final Font biggerFont = titleLabel.getFont().deriveFont(24f);
        titleLabel.setFont(biggerFont);
        testScreen.add(titleLabel, gbc);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.ipady = 20;

        for (final TestQuestion item : questions) {
            testScreen.add(item.getPanel(), gbc);
            testScreen.add(Box.createRigidArea(new Dimension(0, 50)));
        }

        submit = new JButton("Отправить на проверку");
        submit.setFont(submit.getFont().deriveFont(16f));
        submit.setPreferredSize(new Dimension(200, 50));
        submit.setMaximumSize(new Dimension(200, 50));
        submit.addActionListener(this::submit);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        testScreen.add(submit, gbc);

        gbc.weighty = 1;
        testScreen.add(Box.createVerticalGlue(), gbc);
        return testScreen;
    }

    private void submit(final ActionEvent e) {
        int score = 0;
        try {
            for (final TestQuestion q : questions) {
                score += q.check() ? 1 : 0;
            }
        } catch (final EmptyQuestionException ex) {
            JOptionPane.showMessageDialog(testScreen.getRootPane(),
                    "Вопрос " + ex.getNumber() + " не имеет ответа",
                    "Вопрос не отвечен", JOptionPane.WARNING_MESSAGE);
            return;
        }

        final double percentage = (double) score / questionNumber * 100;
        submit.setEnabled(false);
        JOptionPane.showMessageDialog(testScreen.getRootPane(),
                String.format("Вы ответили %d/%d правильно (%.2f%%)", score, questionNumber, percentage),
                "Результат", JOptionPane.INFORMATION_MESSAGE);
        for (final TestQuestion q : questions) {
            q.colorAnswers();
        }
    }

    public static String encloseInHTML(final String s) {
        final StringBuilder sb = new StringBuilder("<html>");
        final int n = s.length();
        for (int i = 0; i < n; i++) {
            final char c = s.charAt(i);
            switch (c) {
                case '&':
                    sb.append("&amp;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                default:
                    sb.append(c);
            }
        }
        sb.append("</html>");
        return sb.toString();
    }
}
