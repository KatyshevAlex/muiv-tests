package muiv.testing.application.exceptions;

public class EmptyQuestionException extends Exception {
    private final int questionNumber;
    public EmptyQuestionException(int number) {
        questionNumber = number;
    }
    public int getNumber() {
        return questionNumber;
    }
}
