package ru.yandex.practicum;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.yandex.practicum.exception.DictionaryEmptyException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class WordleDictionaryLoaderTest {

    @TempDir
    Path tempDir;


    @Test
    void testLoadWordsFileNotFound() {
        WordleDictionaryLoader loader = new WordleDictionaryLoader();
        assertThrows(RuntimeException.class, () -> loader.loadWords("nonexistent.txt"));
    }

    @Test
    void testLoadWordsEmptyFile() throws IOException {
        Path filePath = tempDir.resolve("empty.txt");
        Files.write(filePath, List.of());

        WordleDictionaryLoader loader = new WordleDictionaryLoader();
        assertThrows(DictionaryEmptyException.class, () -> loader.loadWords(filePath.toString()));
    }
}