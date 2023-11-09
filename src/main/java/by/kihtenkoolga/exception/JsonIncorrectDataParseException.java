package by.kihtenkoolga.exception;

public class JsonIncorrectDataParseException extends RuntimeException {

    public JsonIncorrectDataParseException(String dataType) {
        super(String.format("Incorrect data for type : %s", dataType));
    }

}
