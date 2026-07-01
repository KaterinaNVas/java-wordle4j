package ru.yandex.practicum.exception;

public class DictionaryEmptyException extends RuntimeException {

    public DictionaryEmptyException(String message) {
        super(message);
    }

    public DictionaryEmptyException(String message, Throwable cause) {
        super(message, cause);
    }
}