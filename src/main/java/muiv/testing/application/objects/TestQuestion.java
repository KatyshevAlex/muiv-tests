package muiv.testing.application.objects;

import muiv.testing.application.exceptions.EmptyQuestionException;

import java.io.Serializable;

import javax.swing.JPanel;

public interface TestQuestion extends Serializable {
    JPanel getPanel();
    JPanel getPanelEditable();
    boolean check() throws EmptyQuestionException;
    void colorAnswers();
    void save() throws EmptyQuestionException;
}
