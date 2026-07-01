package ru.yandex.practicum.exception;

public class WordNotFoundInDictionaryException extends Exception {

    public WordNotFoundInDictionaryException(String word) {
        super("Слово '" + word + "' не найдено в словаре");
    }

    public WordNotFoundInDictionaryException(String message, Throwable cause) {
        super(message, cause);
    }
}