package ru.yandex.practicum;

import ru.yandex.practicum.exception.InvalidWordLengthException;
import ru.yandex.practicum.exception.WordAlreadyUsedException;
import ru.yandex.practicum.exception.WordNotFoundInDictionaryException;

import java.util.ArrayList;
import java.util.List;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {
    private final String answer;                  // загаданное слово
    private final int maxAttempts;                // максимум попыток
    private int steps;                            // сколько попыток сделано
    private final WordleDictionary dictionary;    // словарь
    private final List<String> usedWords;       // использованные слова
    private boolean gameFinished;                 // игра завершена?
    private boolean gameWon;                      // игра выиграна?
    private final List<String> usedHints;


    public WordleGame(WordleDictionary dictionary, String answer, int maxAttempts) {
        this.dictionary = dictionary;
        this.answer = WordleDictionary.normalizeString(answer);
        this.maxAttempts = maxAttempts;
        this.usedHints = new ArrayList<>();
        this.steps = 0;
        this.usedWords = new ArrayList<>();
        this.gameFinished = false;
        this.gameWon = false;

    }


    public boolean checkAnswer(String guess) {
        if (guess == null) {
            return false;
        }

        return WordleDictionary.normalizeString(guess).equals(answer);
    }

    public String[] makeGuess(String guess) throws InvalidWordLengthException,
            WordNotFoundInDictionaryException,
            WordAlreadyUsedException {
        if (guess.length() != getAnswer().length()) {
            throw new InvalidWordLengthException(getAnswer().length(), guess.length());
        }

        if (!getDictionary().contains(guess)) {
            throw new WordNotFoundInDictionaryException(guess);
        }

        if (getUsedWords().contains(guess)) {
            throw new WordAlreadyUsedException(guess);
        }

        addUsedWord(guess);
        incrementSteps();

        String[] result = new String[getAnswer().length()];

        for (int i = 0; i < getAnswer().length(); i++) {
            result[i] = WordleDictionary.getLetterStatus(getAnswer(), guess, i);
        }
        if (checkAnswer(guess)) {
            setGameWon(true);
            setGameFinished(true);
            return result;
        }

        if (getSteps() >= getMaxAttempts()) {
            setGameWon(false);
            setGameFinished(true);
        }

        return result;
    }

    public String getHint() {
        if (isGameFinished()) {
            return "Игра уже завершена. Загаданное слово: " + getAnswer();
        }

        String hint = getDictionary().getRandomWordByLength(getAnswer().length());

        if (hint == null) {
            return "Нет слов подходящей длины в словаре.";
        }

        if (getUsedWords().contains(hint) || getUsedHints().contains(hint)) {
            List<String> wordsOfLength = getDictionary().getWordsByLength(getAnswer().length());
            String foundWord = null;
            for (String word : wordsOfLength) {
                if (!getUsedWords().contains(word) && !getUsedHints().contains(word)) {
                    foundWord = word;
                    break;
                }
            }
            if (foundWord == null) {
                return "Нет доступных слов для подсказки.";
            }
            hint = foundWord;
        }
        addUsedHint(hint);
        return "Подсказка: " + hint;
    }
    public void setSteps(int steps) {
        this.steps = steps;
    }

    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }

    public void setGameWon(boolean gameWon) {
        this.gameWon = gameWon;
    }

    public String getAnswer() {
        return answer;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public int getSteps() {
        return steps;
    }

    public WordleDictionary getDictionary() {
        return dictionary;
    }

    public List<String> getUsedWords() {
        return new ArrayList<>(usedWords);
    }

    public List<String> getUsedHints() {
        return new ArrayList<>(usedHints);
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public void addUsedHint(String hint) {
        usedHints.add(hint);
    }

    public void addUsedWord(String word) {
        usedWords.add(word);
    }

    public void incrementSteps() {
        steps++;
    }
}
