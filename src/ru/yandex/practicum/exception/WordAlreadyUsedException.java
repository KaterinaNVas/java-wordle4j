package ru.yandex.practicum.exception;

public class WordAlreadyUsedException extends Exception {
    public WordAlreadyUsedException() {
    }

    public WordAlreadyUsedException(String word) {
        super("Слово '" + word + "' уже было использовано!");
    }


    public WordAlreadyUsedException(String message, Throwable cause) {
        super(message, cause);
    }

    public WordAlreadyUsedException(Throwable cause) {
        super(cause);
    }

    public WordAlreadyUsedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
