package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exception.InvalidWordLengthException;
import ru.yandex.practicum.exception.WordAlreadyUsedException;
import ru.yandex.practicum.exception.WordNotFoundInDictionaryException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleGameTest {

    private final String targetWord = "слово";
    private WordleDictionary dictionary;
    private WordleGame game;

    @BeforeEach
    void setUp() {
        List<String> rawWords = Arrays.asList("слово", "смена", "слова", "сатир", "затяг", "кошка");
        dictionary = new WordleDictionary(rawWords);
        game = new WordleGame(dictionary, targetWord, 6);
    }

    @Test
    void testConstructor() {
        assertEquals(targetWord, game.getAnswer());
        assertEquals(6, game.getMaxAttempts());
        assertEquals(0, game.getSteps());
        assertFalse(game.isGameFinished());
        assertFalse(game.isGameWon());
        assertTrue(game.getUsedWords().isEmpty());
        assertTrue(game.getUsedHints().isEmpty());
    }

    @Test
    void testMakeGuessCorrect() throws Exception {
        String result = game.makeGuess("слово");
        assertEquals("+++++", result);
        assertTrue(game.isGameWon());
        assertTrue(game.isGameFinished());
    }

    @Test
    void testMakeGuessIncorrect() throws Exception {
        String result = game.makeGuess("смена");
        assertEquals("+----", result);
        assertFalse(game.isGameWon());
        assertFalse(game.isGameFinished());
        assertEquals(1, game.getSteps());
        assertTrue(game.getUsedWords().contains("смена"));
    }

    @Test
    void testMakeGuessInvalidLength() {
        assertThrows(InvalidWordLengthException.class, () -> game.makeGuess("словарь"));
    }

    @Test
    void testMakeGuessWordNotFound() {
        assertThrows(WordNotFoundInDictionaryException.class, () -> game.makeGuess("несущ"));
    }

    @Test
    void testMakeGuessWordAlreadyUsed() throws Exception {
        game.makeGuess("смена");
        assertThrows(WordAlreadyUsedException.class, () -> game.makeGuess("смена"));
    }

    @Test
    void testCheckAnswer() {
        assertTrue(game.checkAnswer("слово"));
        assertTrue(game.checkAnswer("СЛОВО"));
        assertTrue(game.checkAnswer("  слОВо  "));
        assertFalse(game.checkAnswer("смена"));
        assertFalse(game.checkAnswer(null));
    }

    @Test
    void testGetHint() {
        String hint = game.getHint();
        assertNotNull(hint);
        assertTrue(hint.contains("Подсказка") || hint.contains("Сделайте первый ход"));
    }

    @Test
    void testAddUsedHint() {
        game.addUsedHint("подсказка");
        assertTrue(game.getUsedHints().contains("подсказка"));
    }

    @Test
    void testIncrementSteps() {
        game.incrementSteps();
        assertEquals(1, game.getSteps());
    }

    @Test
    void testSetGameFinished() {
        game.setGameFinished(true);
        assertTrue(game.isGameFinished());
    }

    @Test
    void testSetGameWon() {
        game.setGameWon(true);
        assertTrue(game.isGameWon());
    }

    @Test
    void testGetAnswer() {
        assertEquals("слово", game.getAnswer());
    }

    @Test
    void testGetMaxAttempts() {
        assertEquals(6, game.getMaxAttempts());
    }

    @Test
    void testGetSteps() {
        assertEquals(0, game.getSteps());
    }

    @Test
    void testGetDictionary() {
        assertNotNull(game.getDictionary());
    }
}