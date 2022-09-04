package muiv.testing.application.objects;

import muiv.testing.application.entities.Test;
import muiv.testing.application.exceptions.EmptyQuestionException;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class MultipleSelection extends Multiple {
    private boolean[] correctAnswer;
    private transient ArrayList<JCheckBox> choices = new ArrayList<>();

    public MultipleSelection(int number) {
        questionNumber = number;
    }

    @Override
    protected void addSelectionEdit() {
        addSelectionEdit(null);
    }

    @Override
    protected void addSelectionEdit(String text) {
        JPanel selection = new JPanel();
        JCheckBox c = new JCheckBox();
        choices.add(c);
        JTextField question = new JTextField(text, 40);
        selection.add(c);
        selection.add(question);
        choicesTF.add(question);
        selectionChoiceHolder.add(selection, selectionChoiceConstraints);
        if (choices.size() == 2) {
            addDelete.add(delete);
        }
    }

    @Override
    protected void deleteSelectionEdit() {
        int index = choices.size() - 1;
        choices.remove(index);
        choicesTF.remove(index);
        selectionChoiceHolder.remove(index);
        if (choices.size() == 1)
            addDelete.remove(1);
        editPanel.revalidate();
        editPanel.repaint();
    }

    @Override
    protected void addSelections() {
        for (String choice : choicesText) {
            JCheckBox button = new JCheckBox(Test.encloseInHTML(choice));
            choices.add(button);
            panel.add(button, panelConstraints);
        }
    }

    @Override
    protected void setToCorrectAnswer() {
        int n = choices.size();
        for (int i = 0; i < n; i++) {
            choices.get(i).setSelected(correctAnswer[i]);
        }
    }

    @Override
    public boolean check() throws EmptyQuestionException {
        scope:
        {
            for (JCheckBox button : choices) {
                if (button.isSelected()) {
                    break scope;
                }
            }
            throw new EmptyQuestionException(questionNumber);
        }

        int n = choices.size();
        for (int i = 0; i < n; i++) {
            JCheckBox currentButton = choices.get(i);
            if (currentButton.isSelected() != correctAnswer[i]) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void colorAnswers() {
        int n = choices.size();
        for (int i = 0; i < n; i++) {
            JCheckBox currentButton = choices.get(i);
            if (correctAnswer[i]) {
                currentButton.setBackground(Test.GREEN);
            } else if (currentButton.isSelected()) {
                currentButton.setBackground(Test.RED);
            }

            currentButton.setEnabled(false);
        }
    }

    @Override
    public void save() throws EmptyQuestionException {
        boolean oneIsSelected = false;
        int numButtons = choices.size();
        correctAnswer = new boolean[numButtons];
        choicesText = new String[numButtons];
        for (int i = 0; i < numButtons; i++) {
            choicesText[i] = choicesTF.get(i).getText();
            if (choices.get(i).isSelected()) {
                oneIsSelected = true;
                correctAnswer[i] = true;
            } else {
                correctAnswer[i] = false;
            }
        }

        if (!oneIsSelected) throw new EmptyQuestionException(questionNumber);

        question = questionTF.getText();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        if (choicesText.length != correctAnswer.length) throw new ArrayIndexOutOfBoundsException();
        choices = new ArrayList<>();
    }
}
