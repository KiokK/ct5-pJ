package by.kihtenkoolga.exception;

public class ArraySerializeException extends RuntimeException{

    private static final String MESSAGE = "Array serialize exception at index: %d";

    public ArraySerializeException(int serializeIndexException) {
        super(String.format(MESSAGE, serializeIndexException));
    }

}
