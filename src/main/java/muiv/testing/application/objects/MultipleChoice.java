package muiv.testing.application.objects;

import muiv.testing.application.exceptions.EmptyQuestionException;

import javax.swing.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Enumeration;
import java.util.HashMap;

public class MultipleChoice extends Multiple {

    private static class MyButtonGroup extends ButtonGroup {
        public AbstractButton get(int index) {
            return buttons.get(index);
        }

        public void remove(int index) {
            AbstractButton b = buttons.get(index);
            buttons.remove(index);
            if(isSelected(b.getModel())) {
                clearSelection();
            }
            b.getModel().setGroup(null);
        }
    }

    private int correctAnswer;
    private transient MyButtonGroup buttons = new MyButtonGroup();
    private transient HashMap<ButtonModel, Integer> buttonIDs = new HashMap<>();
    private transient int currentID = -1;

    public MultipleChoice(int number) {
        questionNumber = number;
    }

    @Override
    protected void addSelectionEdit() {
        addSelectionEdit(null);
    }

    @Override
    protected void addSelectionEdit(String text) {
        JPanel selection = new JPanel();
        JRadioButton r = new JRadioButton();
        buttons.add(r);
        buttonIDs.put(r.getModel(), ++currentID);
        JTextField question = new JTextField(text, 40);
        choicesTF.add(question);
        selection.add(r);
        selection.add(question);
        selectionChoiceHolder.add(selection, selectionChoiceConstraints);
        if (currentID == 1) {
            addDelete.add(delete);
        }
    }

    @Override
    protected void deleteSelectionEdit() {
        buttonIDs.remove(buttons.get(currentID).getModel());
        buttons.remove(currentID);
        selectionChoiceHolder.remove(currentID);
        choicesTF.remove(currentID);
        if (currentID == 1) {
            addDelete.remove(1);
        }
        currentID--;
        editPanel.revalidate();
        editPanel.repaint();
    }

    @Override
    protected void setToCorrectAnswer() {
        buttons.get(correctAnswer).setSelected(true);
    }

    @Override
    protected void addSelections() {
        for (String s : choicesText) {
            JRadioButton button = new JRadioButton(Test.encloseInHTML(s));
            buttons.add(button);
            buttonIDs.put(button.getModel(), ++currentID);
            panel.add(button, panelConstraints);
        }
    }

    @Override
    public boolean check() throws EmptyQuestionException {
        ButtonModel selected = buttons.getSelection();
        if (selected == null) {
            throw new EmptyQuestionException(questionNumber);
        }
        int selectedIndex = buttonIDs.get(selected);
        return selectedIndex == correctAnswer;
    }

    @Override
    public void colorAnswers() {
        ButtonModel selected = buttons.getSelection();
        int selectedIndex = buttonIDs.get(selected);
        buttons.get(correctAnswer).setBackground(Test.GREEN);
        if (selectedIndex != correctAnswer) {
            buttons.get(selectedIndex).setBackground(Test.RED);
        }
        Enumeration<AbstractButton> buttonEnum = buttons.getElements();
        while (buttonEnum.hasMoreElements()) {
            buttonEnum.nextElement().setEnabled(false);
        }
    }

    @Override
    public void save() throws EmptyQuestionException {
        ButtonModel selected = buttons.getSelection();
        if (selected == null) {
            throw new EmptyQuestionException(questionNumber);
        }
        question = questionTF.getText();
        correctAnswer = buttonIDs.get(selected);
        int numChoices = choicesTF.size();
        choicesText = new String[numChoices];
        for (int i = 0; i < numChoices; i++) {
            choicesText[i] = choicesTF.get(i).getText();
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        if (correctAnswer < 0 || correctAnswer >= choicesText.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        buttons = new MyButtonGroup();
        buttonIDs = new HashMap<>();
        currentID = -1;
    }
}
