package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        try (FileReader reader = new FileReader(filename);
             BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                    words.add(line);
            }
            return new WordleDictionary(words);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

