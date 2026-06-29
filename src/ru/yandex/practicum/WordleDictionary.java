package ru.yandex.practicum;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Set;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private List<String> words = new ArrayList<>();

    public WordleDictionary() {
    }

    public WordleDictionary(List<String> rawWords) {
        this.words = normalizeWords(rawWords);
    }


    private List<String> normalizeWords(List<String> rawWords) {
        List<String> result = new ArrayList<>();
        for (String word : rawWords) {
            if (word == null) {
                continue;
            }
            String cleaned = word.trim().toLowerCase().replace("ё", "е");
            if (cleaned.length() == 5 && isRussianWord(cleaned)) {
                result.add(cleaned);
            }
        }
        return result;
    }

    private boolean isRussianWord(String word) {
        for (char c : word.toCharArray()) {
            if (!isRussianLetter(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean isRussianLetter(char c) {
        return c >= 'а' && c <= 'я';
    }


    public static String normalizeString(String string) {
        if (string == null) return "";
        return string.trim().toLowerCase().replace("ё", "е");
    }

    public boolean contains(String word) {
        if (word == null) return false;
        return words.contains(normalizeString(word));
    }

    public int size() {
        return words.size();
    }

    public boolean isEmpty() {
        return words.isEmpty();
    }

    public String getRandomWord() {
        if (words.isEmpty()) {
            return null;
        }
        int index = (int) (Math.random() * words.size());
        return words.get(index);
    }

    public void setWords(List<String> words) {
        this.words = normalizeWords(words);
    }

    public List<String> getWords() {
        return new ArrayList<>(words);
    }

    public static boolean isLetterAtPosition(String word, char letter, int position) {
        if (word == null) return false;
        if (position < 0 || position >= word.length()) return false;
        return word.indexOf(letter) == position;
    }

    public static boolean isLetterMatch(String word, String guess, int position) {
        // Проверяет, совпадает ли буква в слове и догадке на позиции

        if (word == null || guess == null) {
            return false;
        }

        if (position < 0 || position >= word.length() || position >= guess.length()) {
            return false;
        }
        return word.charAt(position) == guess.charAt(position);
    }

    public static boolean isLetterInWord(String word, char letter) {
        // Проверяет, есть ли буква в слове
        if (word == null) return false;
        return word.indexOf(letter) != -1;
    }

    public static int countLetterMatches(String word, String guess) {
        // Считает количество совпадающих букв
        if (word == null || guess == null) return 0;

        String lowerWord = normalizeString(word);
        String lowerGuess = normalizeString(guess);

        int count = 0;
        for (char c : lowerGuess.toCharArray()) {
            if (lowerWord.indexOf(c) != -1) {
                count++;
            }
        }
        return count;

    }

    public static String getLetterStatus(String word, String guess, int position) {
        // Возвращает статус буквы: "correct", "present", "absent"
        if (word == null || guess == null) return "absent";

        if (position < 0 || position >= word.length() || position >= guess.length()) {
            return "absent";
        }

        char guessedChar = guess.charAt(position);
        char wordChar = word.charAt(position);

        if (guessedChar == wordChar) {
            return "correct";
        } else if (word.indexOf(guessedChar) != -1) {
            return "present";
        } else {
            return "absent";
        }
    }

    public List<String> getWordsByLength(int length) {
        List<String> result = new ArrayList<>();

        for (String word : getWords()) {
            if (word.length() == length) {
                result.add(word);
            }
        }
        return result;
    }
}



