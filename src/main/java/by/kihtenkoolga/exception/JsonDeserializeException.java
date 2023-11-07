package by.kihtenkoolga.exception;

public class JsonDeserializeException extends RuntimeException{

    private static final String MESSAGE = "Incorrect start parse position ind : %s";

    public JsonDeserializeException(String arg) {
        super(String.format(MESSAGE, arg));
    }
}
