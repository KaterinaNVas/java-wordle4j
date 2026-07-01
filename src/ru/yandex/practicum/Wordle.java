package ru.yandex.practicum;

import ru.yandex.practicum.exception.DictionaryLoadException;
import ru.yandex.practicum.exception.InvalidWordLengthException;
import ru.yandex.practicum.exception.WordAlreadyUsedException;
import ru.yandex.practicum.exception.WordNotFoundInDictionaryException;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Wordle {

    private static final int WORD_LENGTH = 5;
    private static final int MAX_ATTEMPTS = 6;
    private static final String DICTIONARY_FILE = "words_ru.txt";
    private static PrintWriter log;

    public static void main(String[] args) {
        try {
            // 1. Настройка лог-файла
            log = new PrintWriter(new FileWriter("wordle.log", true));
            log.println("=== Игра начата ===");
            log.flush();

            System.out.println("===== ДОБРО ПОЖАЛОВАТЬ В WORDLE! =====");
            System.out.println("Угадайте слово из " + WORD_LENGTH + " букв за " + MAX_ATTEMPTS + " попыток.");
            System.out.println();

            WordleDictionary dictionary = loadDictionary();
            if (dictionary == null) {
                System.out.println("Не удалось загрузить словарь. Программа завершена.");
                return;
            }

            String targetWord = dictionary.getRandomWordByLength(WORD_LENGTH);
            if (targetWord == null) {
                System.out.println("В словаре нет слов длины " + WORD_LENGTH + ". Программа завершена.");
                log.println("ОШИБКА: В словаре нет слов длины " + WORD_LENGTH);
                log.flush();
                return;
            }

            WordleGame game = new WordleGame(dictionary, targetWord, MAX_ATTEMPTS);
            log.println("Игра начата. Загаданное слово: " + targetWord);
            log.flush();

            Scanner scanner = new Scanner(System.in);
            playGame(game, scanner);

            scanner.close();
            log.println("Игра завершена.");
            log.flush();
            System.out.println("Спасибо за игру!");

        } catch (Exception e) {
            if (log != null) {
                log.println("КРИТИЧЕСКАЯ ОШИБКА: " + e.getMessage());
                e.printStackTrace(log);
                log.flush();
            }
            System.out.println("Произошла ошибка: " + e.getMessage());
            System.out.println("Подробности записаны в лог-файл.");
        } finally {
            if (log != null) {
                log.close();
            }
        }
    }

    private static WordleDictionary loadDictionary() {
        try {
            WordleDictionaryLoader loader = new WordleDictionaryLoader();
            WordleDictionary dictionary = loader.loadWords(DICTIONARY_FILE);
            log.println("Словарь загружен. Количество слов: " + dictionary.size());
            log.flush();
            System.out.println("Словарь загружен! Найдено слов: " + dictionary.size());
            return dictionary;
        } catch (DictionaryLoadException e) {
            System.out.println("Ошибка загрузки словаря: " + e.getMessage());
            log.println("Ошибка загрузки словаря: " + e.getMessage());
            log.flush();
            return null;
        }
    }

    private static void playGame(WordleGame game, Scanner scanner) {
        while (!game.isGameFinished()) {
            try {
                System.out.println();
                System.out.println("Попытка " + (game.getSteps() + 1) + " из " + game.getMaxAttempts());
                System.out.print("Введите слово из " + WORD_LENGTH + " букв: ");
                String guess = scanner.nextLine().trim().toLowerCase();

                if (guess.isEmpty()) {
                    if (game.getUsedWords().isEmpty()) {
                        System.out.println("Сделайте первый ход, чтобы получить подсказку!");
                        log.println("Игрок попытался получить подсказку без ходов.");
                        log.flush();
                    } else {
                        String hint = game.getHint();
                        System.out.println(hint);
                        log.println("Игрок запросил подсказку.");
                        log.flush();
                    }
                    continue;
                }

                if (guess.equals("exit") || guess.equals("выход")) {
                    System.out.println("Загаданное слово: " + game.getAnswer());
                    log.println("Игрок завершил игру досрочно. Загаданное слово: " + game.getAnswer());
                    log.flush();
                    game.setGameFinished(true);
                    break;
                }

                if (guess.length() != WORD_LENGTH) {
                    System.out.println("Слово должно содержать " + WORD_LENGTH + " букв.");
                    log.println("Игрок ввёл слово неправильной длины: " + guess);
                    log.flush();
                    continue;
                }

                String result = game.makeGuess(guess);
                printResult(result, guess);

                if (game.isGameWon()) {
                    System.out.println();
                    System.out.println("ПОЗДРАВЛЯЮ! Вы угадали слово: " + game.getAnswer());
                    log.println("Игрок победил! Слово: " + game.getAnswer());
                    log.flush();
                    break;
                }

                if (game.isGameFinished()) {
                    System.out.println();
                    System.out.println("Вы проиграли. Загаданное слово: " + game.getAnswer());
                    log.println("Игрок проиграл. Загаданное слово: " + game.getAnswer());
                    log.flush();
                }

            } catch (InvalidWordLengthException | WordNotFoundInDictionaryException | WordAlreadyUsedException |
                     IllegalStateException e) {
                System.out.println("Ошибка: " + e.getMessage());
                log.println("Игровая ошибка: " + e.getMessage());
                log.flush();
                if (e instanceof IllegalStateException) {
                    game.setGameFinished(true);
                    break;
                }
            }
        }
    }

    private static void printResult(String result, String guess) {
        System.out.println(guess);
        for (int i = 0; i < result.length(); i++) {
            char status = result.charAt(i);
            switch (status) {
                case '+':
                    System.out.print("+");
                    break;
                case '^':
                    System.out.print("^");
                    break;
                case '-':
                    System.out.print("-");
                    break;
                default:
                    System.out.print("?");
                    break;
            }
        }
        System.out.println();
    }

    public static PrintWriter getLog() {
        return log;
    }

    public static void setLog(PrintWriter log) {
        Wordle.log = log;
    }
}