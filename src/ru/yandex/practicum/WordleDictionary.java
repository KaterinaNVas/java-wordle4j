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

//    public List<String> loadWords(String filename) {
//        try (FileReader reader = new FileReader(filename);
//             BufferedReader br = new BufferedReader(reader)) {
//            String line;
//                if (line.length() == 5) {
//                    String cleaned = line.trim().toLowerCase().replace("ё", "е");
//                    words.add(cleaned);
//                }
//            }
//            return new ArrayList<>(words);
//        } catch (IOException e) {
//                System.out.println("Ошибка загрузки слов: " + e.getMessage());
//                return new ArrayList<>();
//            }
//        }
//
//    }

