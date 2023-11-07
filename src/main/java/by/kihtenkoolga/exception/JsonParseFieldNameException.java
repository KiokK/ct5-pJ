package by.kihtenkoolga.exception;

public class JsonParseFieldNameException extends RuntimeException{

    private static final String MESSAGE = "Error deserialization classes field with name: %s";

    public JsonParseFieldNameException(String fieldName) {
        super(String.format(MESSAGE, fieldName));
    }

}
