package ru.yandex.practicum;

import ru.yandex.practicum.exception.InvalidWordLengthException;
import ru.yandex.practicum.exception.WordAlreadyUsedException;
import ru.yandex.practicum.exception.WordNotFoundInDictionaryException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public String makeGuess(String guess)
            throws InvalidWordLengthException,
            WordNotFoundInDictionaryException,
            WordAlreadyUsedException {

        // ✅ Проверка: игра уже завершена
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

        // 1. Получаем все слова нужной длины
        List<String> possibleWords = new ArrayList<>(getDictionary().getWordsByLength(getAnswer().length()));

        // 2. Убираем использованные слова и подсказки
        possibleWords.removeAll(getUsedWords());
        possibleWords.removeAll(getUsedHints());

        // 3. Анализируем все использованные слова
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
                    // Запоминаем, что буква НЕ на этой позиции
                    wrongPositions.computeIfAbsent(i, k -> new ArrayList<>()).add(letter);
                } else if (status == '-') {
                    if (!absentLetters.contains(letter)) {
                        absentLetters.add(letter);
                    }
                }
            }
        }

        // 4. Фильтруем слова
        List<String> filteredWords = new ArrayList<>();

        for (String word : possibleWords) {
            boolean isValid = true;

            // Проверка 1: нет неподходящих букв
            for (char c : absentLetters) {
                if (word.indexOf(c) != -1) {
                    isValid = false;
                    break;
                }
            }
            if (!isValid) continue;

            // Проверка 2: есть все необходимые буквы
            for (char c : presentLetters) {
                if (word.indexOf(c) == -1) {
                    isValid = false;
                    break;
                }
            }
            if (!isValid) continue;

            // Проверка 3: буквы на местах совпадают
            for (Map.Entry<Integer, Character> entry : correctPositions.entrySet()) {
                int pos = entry.getKey();
                char letter = entry.getValue();
                if (word.charAt(pos) != letter) {
                    isValid = false;
                    break;
                }
            }
            if (!isValid) continue;

            // Проверка 4: буквы НЕ на неправильных местах
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

        // 5. Выбираем случайное слово
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

    public int getRemainingAttempts() {
        return maxAttempts - steps;
    }
}
