package by.kihtenkoolga.exception;

public class JsonParseFieldNameException extends RuntimeException{

    public JsonParseFieldNameException(String fieldName) {
        super(String.format("Error deserialization classes field with name: %s", fieldName));
    }

}
