package ru.yandex.practicum;

import ru.yandex.practicum.exception.DictionaryEmptyException;
import ru.yandex.practicum.exception.DictionaryLoadException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {

    public WordleDictionary loadWords(String filename) {
        List<String> words = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filename), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                words.add(line);
            }
            if (words.isEmpty()) {
                throw new DictionaryEmptyException("Файл словаря пуст: " + filename);
            }

            return new WordleDictionary(words);

        } catch (IOException e) {
            throw new DictionaryLoadException("Ошибка загрузки словаря: " + filename, e);
        }
    }
}