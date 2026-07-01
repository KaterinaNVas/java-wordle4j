package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryTest {

    private WordleDictionary dictionary;

    @BeforeEach
    void setUp() {
        List<String> rawWords = Arrays.asList("слово", "река", "гора", "дом", "лес", "солнце");
        dictionary = new WordleDictionary(rawWords);
    }

    @Test
    void testNormalizeString() {
        assertEquals("слово", WordleDictionary.normalizeString("СЛОВО"));
        assertEquals("слово", WordleDictionary.normalizeString("  СЛОВО  "));
        assertEquals("еж", WordleDictionary.normalizeString("ёж"));
        assertEquals("", WordleDictionary.normalizeString(null));
    }

    @Test
    void testContains() {
        assertTrue(dictionary.contains("слово"));
        assertTrue(dictionary.contains("СЛОВО"));
        assertTrue(dictionary.contains("  слОВо  "));
        assertFalse(dictionary.contains("несуществующее"));
        assertFalse(dictionary.contains(null));
    }

    @Test
    void testIsEmpty() {
        assertFalse(dictionary.isEmpty());
        WordleDictionary emptyDict = new WordleDictionary();
        assertTrue(emptyDict.isEmpty());
    }

    @Test
    void testGetRandomWord() {
        String word = dictionary.getRandomWord();
        assertNotNull(word);
        assertEquals(5, word.length());
        assertTrue(dictionary.contains(word));
    }
}