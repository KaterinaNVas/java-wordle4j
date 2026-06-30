package ru.yandex.practicum.exception;

public class DictionaryEmptyException extends RuntimeException  {

    public DictionaryEmptyException() {
    }

    public DictionaryEmptyException(String message) {
        super(message);
    }

    public DictionaryEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public DictionaryEmptyException(Throwable cause) {
        super(cause);
    }

    public DictionaryEmptyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
