package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public static String normalizeString(String string) {
        if (string == null) return "";
        return string.trim().toLowerCase().replace("ё", "е");
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
        return (c >= 'а' && c <= 'я') || c == 'ё';
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

    public String getRandomWordByLength(int length) {
        List<String> wordsOfLength = getWordsByLength(length);
        if (wordsOfLength.isEmpty()) {
            return null;
        }
        int randomIndex = (int) (Math.random() * wordsOfLength.size());
        return wordsOfLength.get(randomIndex);
    }

    public void setWords(List<String> words) {
        this.words = normalizeWords(words);
    }

    public List<String> getWords() {
        return new ArrayList<>(words);
    }

    public List<String> getWordsByLength(int length) {
        List<String> result = new ArrayList<>();
        for (String word : words) {
            if (word.length() == length) {
                result.add(word);
            }
        }
        return result;
    }

    public static char getLetterStatus(String word, String guess, int position) {
        if (word == null || guess == null) return '-';
        if (position < 0 || position >= word.length() || position >= guess.length()) {
            return '-';
        }

        char guessedChar = guess.charAt(position);
        char wordChar = word.charAt(position);

        if (guessedChar == wordChar) {
            return '+';
        } else if (word.indexOf(guessedChar) != -1) {
            return '^';
        } else {
            return '-';
        }
    }

    public static String getReadyString(String answer, String guess) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < answer.length(); i++) {
            result.append(getLetterStatus(answer, guess, i));
        }
        return result.toString();
    }

    public static boolean isLetterAtPosition(String word, char letter, int position) {
        if (word == null) return false;
        if (position < 0 || position >= word.length()) return false;
        return word.charAt(position) == letter;
    }

    public static boolean isLetterMatch(String word, String guess, int position) {
        if (word == null || guess == null) {
            return false;
        }
        if (position < 0 || position >= word.length() || position >= guess.length()) {
            return false;
        }
        return word.charAt(position) == guess.charAt(position);
    }

    public static boolean isLetterInWord(String word, char letter) {
        if (word == null) return false;
        return word.indexOf(letter) != -1;
    }

    public static int countLetterMatches(String word, String guess) {
        if (word == null || guess == null) return 0;

        String lowerWord = normalizeString(word);
        String lowerGuess = normalizeString(guess);

        Set<Character> wordLetters = new HashSet<>();
        for (char c : lowerWord.toCharArray()) {
            wordLetters.add(c);
        }

        int count = 0;
        for (char c : lowerGuess.toCharArray()) {
            if (wordLetters.contains(c)) {
                count++;
                wordLetters.remove(c);
            }
        }
        return count;
    }
}