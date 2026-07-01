package ru.yandex.practicum.exception;

public class InvalidWordLengthException extends RuntimeException {
    public InvalidWordLengthException() {
    }

    public InvalidWordLengthException(int expectedLength, int actualLength) {
        super("Слово должно содержать " + expectedLength + " букв, а вы ввели " + actualLength);
    }

    public InvalidWordLengthException(String message) {
        super(message);
    }

    public InvalidWordLengthException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidWordLengthException(Throwable cause) {
        super(cause);
    }

    public InvalidWordLengthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
