package App;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public abstract class Multiple implements TestQuestion {

    // transient fields are not serializable
    protected transient JPanel panel;
    protected transient GridBagConstraints panelConstraints;
    protected transient JPanel editPanel;
    protected transient JTextArea questionTF;
    protected transient JPanel selectionChoiceHolder;
    protected transient GridBagConstraints selectionChoiceConstraints;
    protected transient ArrayList<JTextField> choicesTF = new ArrayList<>();
    protected transient JButton delete;
    protected transient JPanel addDelete;


    protected String question;
    protected int questionNumber;
    protected String[] choicesText;

    @Override
    public JPanel getPanel() {
        panel = new JPanel(new GridBagLayout());
        panelConstraints = new GridBagConstraints();
        panelConstraints.gridx = 0;
        panelConstraints.anchor = GridBagConstraints.LINE_START;
        panelConstraints.weightx = 1;
        panelConstraints.ipady = 10;
        panelConstraints.fill = GridBagConstraints.HORIZONTAL;

        JLabel questionLabel = new JLabel(
                Test.encloseInHTML(questionNumber + ". " + question));
        questionLabel.setFont(questionLabel.getFont().deriveFont(16f));
        panel.add(questionLabel, panelConstraints);
        addSelections();
        return panel;
    }

    @Override
    public JPanel getPanelEditable() {
        editPanel = new JPanel(new GridBagLayout());

        GridBagConstraints questionConstraints = new GridBagConstraints();
        questionConstraints.gridx = 0;
        questionConstraints.anchor = GridBagConstraints.LINE_START;
        questionConstraints.weightx = 1;
        JLabel questionLabel = new JLabel("Вопрос " + questionNumber + ":");
        questionTF = new JTextArea(question, 3, 50);

        questionTF.setLineWrap(true);
        questionTF.setWrapStyleWord(true);

        JScrollPane questionTFS = new JScrollPane(questionTF,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        selectionChoiceHolder = new JPanel(new GridBagLayout());
        selectionChoiceConstraints = new GridBagConstraints();
        selectionChoiceConstraints.gridx = 0;

        addDelete = new JPanel();
        JButton add = new JButton("Добавить вариант ответа");
        add.addActionListener((e) -> {
            addSelectionEdit();
            editPanel.revalidate();
            editPanel.repaint();
        });

        addDelete.add(add);
        delete = new JButton("Удалить вариант ответа");
        delete.addActionListener((e) -> deleteSelectionEdit());

        if (choicesText == null) {
            addSelectionEdit();
        } else {
            for (String text : choicesText) {
                addSelectionEdit(text);
            }
            setToCorrectAnswer();
        }

        editPanel.add(questionLabel, questionConstraints);
        editPanel.add(questionTFS, questionConstraints);
        editPanel.add(selectionChoiceHolder, questionConstraints);
        editPanel.add(addDelete, questionConstraints);
        editPanel.add(Box.createRigidArea(new Dimension(0, 50)), questionConstraints);
        return editPanel;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        choicesTF = new ArrayList<>();
    }

    protected abstract void addSelectionEdit();
    protected abstract void addSelectionEdit(String text);
    protected abstract void deleteSelectionEdit();
    protected abstract void setToCorrectAnswer();
    protected abstract void addSelections();
}
