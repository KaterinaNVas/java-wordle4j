package ru.yandex.practicum;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private List<String> words = new ArrayList<>();

    public WordleDictionary(List<String> words) {
        this.words = words;
    }


    public void setWords(List<String> words) {
        this.words = words;
    }

    public List<String> getWords() {
        return new ArrayList<>(words);
    }
}


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
                if (line.length() == 5) {
                    String cleaned = line.trim().toLowerCase().replace("ё", "е");
                    words.add(cleaned);
                }
            }
            return new WordleDictionary(words);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}



