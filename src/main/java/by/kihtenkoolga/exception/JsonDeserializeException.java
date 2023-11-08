package by.kihtenkoolga.exception;

public class JsonDeserializeException extends RuntimeException{

    private static final String MESSAGE = "Incorrect start parse position ind : %s";

    public JsonDeserializeException(int incorrectPosition) {
        super(String.format(MESSAGE, incorrectPosition));
    }

}
