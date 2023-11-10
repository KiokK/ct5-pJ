package by.kihtenkoolga.exception;

public class JsonDeserializeException extends RuntimeException {

    public JsonDeserializeException(int incorrectPosition) {
        super(String.format("Incorrect start parse position ind : %s", incorrectPosition));
    }

}
