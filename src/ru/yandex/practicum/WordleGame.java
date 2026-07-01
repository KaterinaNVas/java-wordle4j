package ru.yandex.practicum;

import ru.yandex.practicum.exception.InvalidWordLengthException;
import ru.yandex.practicum.exception.WordAlreadyUsedException;
import ru.yandex.practicum.exception.WordNotFoundInDictionaryException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordleGame {
    private final String answer;                  // загаданное слово
    private final int maxAttempts;                // максимум попыток
    private final WordleDictionary dictionary;    // словарь
    private final List<String> usedWords;       // использованные слова
    private final List<String> usedHints;
    private int steps;                            // сколько попыток сделано
    private boolean gameFinished;                 // игра завершена?
    private boolean gameWon;                      // игра выиграна?


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

    public String makeGuess(String guess) throws InvalidWordLengthException, WordNotFoundInDictionaryException, WordAlreadyUsedException {

        if (isGameFinished()) {
            throw new IllegalStateException("Игра уже завершена");
        }

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

        String result = WordleDictionary.getReadyString(getAnswer(), guess);

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

        if (getUsedWords().isEmpty()) {
            return "Сделайте первый ход, чтобы получить подсказку!";
        }

        List<String> possibleWords = new ArrayList<>(getDictionary().getWordsByLength(getAnswer().length()));

        possibleWords.removeAll(getUsedWords());
        possibleWords.removeAll(getUsedHints());

        List<Character> absentLetters = new ArrayList<>();
        List<Character> presentLetters = new ArrayList<>();
        Map<Integer, Character> correctPositions = new HashMap<>();
        Map<Integer, List<Character>> wrongPositions = new HashMap<>();

        for (String usedWord : getUsedWords()) {
            String result = WordleDictionary.getReadyString(getAnswer(), usedWord);

            for (int i = 0; i < result.length(); i++) {
                char status = result.charAt(i);
                char letter = usedWord.charAt(i);

                if (status == '+') {
                    correctPositions.put(i, letter);
                } else if (status == '^') {
                    if (!presentLetters.contains(letter)) {
                        presentLetters.add(letter);
                    }
                    wrongPositions.computeIfAbsent(i, k -> new ArrayList<>()).add(letter);
                } else if (status == '-') {
                    if (!absentLetters.contains(letter)) {
                        absentLetters.add(letter);
                    }
                }
            }
        }

        List<String> filteredWords = new ArrayList<>();

        for (String word : possibleWords) {
            boolean isValid = true;

            for (char c : absentLetters) {
                if (word.indexOf(c) != -1) {
                    isValid = false;
                    break;
                }
            }
            if (!isValid) continue;

            for (char c : presentLetters) {
                if (word.indexOf(c) == -1) {
                    isValid = false;
                    break;
                }
            }
            if (!isValid) continue;

            for (Map.Entry<Integer, Character> entry : correctPositions.entrySet()) {
                int pos = entry.getKey();
                char letter = entry.getValue();
                if (word.charAt(pos) != letter) {
                    isValid = false;
                    break;
                }
            }
            if (!isValid) continue;

            for (Map.Entry<Integer, List<Character>> entry : wrongPositions.entrySet()) {
                int pos = entry.getKey();
                for (char letter : entry.getValue()) {
                    if (word.charAt(pos) == letter) {
                        isValid = false;
                        break;
                    }
                }
                if (!isValid) break;
            }
            if (!isValid) continue;

            filteredWords.add(word);
        }

        if (filteredWords.isEmpty()) {
            return "Нет подходящих слов для подсказки.";
        }

        int randomIndex = (int) (Math.random() * filteredWords.size());
        String hint = filteredWords.get(randomIndex);

        addUsedHint(hint);
        return "Подсказка: " + hint;
    }


    public String getLetterHint() {
        if (isGameFinished()) {
            return "Игра уже завершена.";
        }
        StringBuilder hint = new StringBuilder();
        for (int i = 0; i < getAnswer().length(); i++) {
            boolean found = false;
            for (String word : getUsedWords()) {
                if (word.length() > i && word.charAt(i) == getAnswer().charAt(i)) {
                    found = true;
                    break;
                }
            }
            if (found) {
                hint.append(getAnswer().charAt(i));
            } else {
                hint.append('_');
            }
        }
        return hint.toString();
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

    public void setSteps(int steps) {
        this.steps = steps;
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

    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public void setGameWon(boolean gameWon) {
        this.gameWon = gameWon;
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

    public int getRemainingAttempts() {
        return maxAttempts - steps;
    }
}
