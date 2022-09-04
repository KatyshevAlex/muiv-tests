package App;

import java.io.Serializable;

import javax.swing.JPanel;

public interface QuizQuestion extends Serializable {
    JPanel getPanel();
    JPanel getPanelEditable();
    boolean check() throws EmptyQuestionException;
    void colorAnswers();
    void save() throws EmptyQuestionException;
}
