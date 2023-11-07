package by.kihtenkoolga.exception;

public class JsonIncorrectDataParseException extends RuntimeException {

    private static final String MESSAGE = "Incorrect data for type : %s";

    public JsonIncorrectDataParseException(String dataType) {
        super(String.format(MESSAGE, dataType));
    }

}
