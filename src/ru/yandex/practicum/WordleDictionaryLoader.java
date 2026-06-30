package ru.yandex.practicum;

import ru.yandex.practicum.exception.DictionaryEmptyException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
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
            throw new RuntimeException(e);
        }
    }

}

