package muiv.testing.application.objects;

import muiv.testing.application.entities.Test;
import muiv.testing.application.exceptions.EmptyQuestionException;

import javax.swing.*;

import java.awt.*;


public class FillBlank implements TestQuestion {
    private transient JTextArea questionTA;
    private transient JTextField answerTF;
    private transient JLabel correctAnswer;
    private String questionText;
    private String answerText;
    private final int questionNumber;

    public FillBlank(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    @Override
    public JPanel getPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel title = new JLabel(Test.encloseInHTML(questionNumber + ". " + questionText));
        title.setFont(title.getFont().deriveFont(16f));
        panel.add(title, gbc);

        JPanel answerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        answerTF = new JTextField(20);
        correctAnswer = new JLabel("Верный ответ: " + answerText);
        correctAnswer.setVisible(false);
        answerPanel.add(answerTF);
        answerPanel.add(correctAnswer);
        panel.add(answerPanel, gbc);
        return panel;
    }

    /**
     * get question panel when editing test
     *
     * @return the panel to show
     */
    @Override
    public JPanel getPanelEditable() {
        JPanel editPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 0;
        gbc.weightx = 1;

        JLabel questionLabel = new JLabel("Вопрос " + questionNumber + ":");

        questionTA = new JTextArea(questionText, 3, 50);
        questionTA.setLineWrap(true);
        questionTA.setWrapStyleWord(true);
        JScrollPane questionTFS = new JScrollPane(questionTA,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel bottom = new JPanel();
        JLabel label = new JLabel("Ответ: ");
        answerTF = new JTextField(answerText, 20);
        bottom.add(label);
        bottom.add(answerTF);

        editPanel.add(questionLabel, gbc);
        editPanel.add(questionTFS, gbc);
        editPanel.add(Box.createRigidArea(new Dimension(0, 10)), gbc);
        editPanel.add(bottom, gbc);
        // create vertical spacing
        editPanel.add(Box.createRigidArea(new Dimension(0, 50)), gbc);
        return editPanel;
    }

    /**
     * whether the answer filled in is the correct answer
     *
     * @return true if answer is correct
     */
    @Override
    public boolean check() throws EmptyQuestionException {
        String answer = answerTF.getText().trim();
        if (answer.length() == 0) {
            throw new EmptyQuestionException(questionNumber);
        }
        return answer.equalsIgnoreCase(answerText);
    }

    @Override
    public void colorAnswers() {
        String answer = answerTF.getText().trim();
        // disable editing
        answerTF.setEnabled(false);
        if (answer.equalsIgnoreCase(answerText)) {
            answerTF.setBackground(Test.GREEN);
            return;
        }
        correctAnswer.setVisible(true);
        answerTF.setBackground(Test.RED);
    }

    /**
     * saves the question and the correct answer to serialized variables
     */
    @Override
    public void save() throws EmptyQuestionException {
        String answer = answerTF.getText().trim();
        if (answer.length() == 0) {
            throw new EmptyQuestionException(questionNumber);
        }
        questionText = questionTA.getText();
        answerText = answer;
    }
}
