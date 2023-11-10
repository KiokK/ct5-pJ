package by.kihtenkoolga.exception;

public class ArraySerializeException extends RuntimeException{

    public ArraySerializeException(int serializeIndexException) {
        super(String.format("Array serialize exception at index: %d", serializeIndexException));
    }

}
