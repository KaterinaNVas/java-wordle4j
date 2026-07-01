package ru.yandex.practicum;

import ru.yandex.practicum.exception.*;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Wordle {

    private static final Logger logger = Logger.getLogger(Wordle.class.getName());
    private static final int WORD_LENGTH = 5;
    private static final int MAX_ATTEMPTS = 6;
    private static final String DICTIONARY_FILE = "words_ru.txt";

    public static void main(String[] args) {
        System.out.println("===== ДОБРО ПОЖАЛОВАТЬ В WORDLE! =====");
        System.out.println("Угадайте слово из " + WORD_LENGTH + " букв за " + MAX_ATTEMPTS + " попыток.");
        System.out.println();

        setupLogger();
        WordleDictionary dictionary = loadDictionary();
        if (dictionary == null) {
            System.out.println("Не удалось загрузить словарь. Программа завершена.");
            return;
        }

        String targetWord = dictionary.getRandomWordByLength(WORD_LENGTH);
        if (targetWord == null) {
            System.out.println("В словаре нет слов длины " + WORD_LENGTH + ". Программа завершена.");
            logger.severe("В словаре нет слов длины " + WORD_LENGTH);
            return;
        }

        WordleGame game = new WordleGame(dictionary, targetWord, MAX_ATTEMPTS);
        logger.info("Игра начата. Загаданное слово: " + targetWord);

        Scanner scanner = new Scanner(System.in);
        playGame(game, scanner);

        scanner.close();
        logger.info("Игра завершена.");
        System.out.println("Спасибо за игру!");
    }

    private static void setupLogger() {
        try {
            Logger rootLogger = Logger.getLogger("");
            Handler[] handlers = rootLogger.getHandlers();
            for (Handler handler : handlers) {
                rootLogger.removeHandler(handler);
            }

            FileHandler fileHandler = new FileHandler("wordle.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);

            logger.info("Логгер инициализирован.");
        } catch (IOException e) {
            System.out.println("Ошибка при создании лог-файла: " + e.getMessage());
        }
    }

    private static WordleDictionary loadDictionary() {
        try {
            WordleDictionaryLoader loader = new WordleDictionaryLoader();
            WordleDictionary dictionary = loader.loadWords(DICTIONARY_FILE);
            logger.info("Словарь загружен. Количество слов: " + dictionary.size());
            System.out.println("Словарь загружен! Найдено слов: " + dictionary.size());
            return dictionary;
        } catch (DictionaryLoadException e) {
            System.out.println("Ошибка загрузки словаря: " + e.getMessage());
            logger.severe("Ошибка загрузки словаря: " + e.getMessage());
            return null;
        }
    }

    private static void playGame(WordleGame game, Scanner scanner) {
        while (!game.isGameFinished()) {
            System.out.println();
            System.out.println("Попытка " + (game.getSteps() + 1) + " из " + game.getMaxAttempts());
            System.out.print("Введите слово из " + WORD_LENGTH + " букв: ");
            String guess = scanner.nextLine().trim().toLowerCase();

            if (guess.equals("hint") || guess.equals("подсказка")) {
                String hint = game.getHint();
                System.out.println(hint);
                logger.info("Игрок запросил подсказку.");
                continue;
            }

            if (guess.equals("exit") || guess.equals("выход")) {
                System.out.println("Загаданное слово: " + game.getAnswer());
                logger.info("Игрок завершил игру досрочно.");
                game.setGameFinished(true);
                break;
            }

            if (guess.length() != WORD_LENGTH) {
                System.out.println("Слово должно содержать " + WORD_LENGTH + " букв.");
                continue;
            }

            try {
                String result = game.makeGuess(guess);
                printResult(result, guess);

                if (game.isGameWon()) {
                    System.out.println();
                    System.out.println("ПОЗДРАВЛЯЮ! Вы угадали слово: " + game.getAnswer());
                    logger.info("Игрок победил! Слово: " + game.getAnswer());
                    break;
                }

                if (game.isGameFinished()) {
                    System.out.println();
                    System.out.println("Вы проиграли. Загаданное слово: " + game.getAnswer());
                    logger.info("Игрок проиграл. Загаданное слово: " + game.getAnswer());
                }

            } catch (InvalidWordLengthException e) {
                System.out.println("Ошибка: " + e.getMessage());
            } catch (WordNotFoundInDictionaryException e) {
                System.out.println("Ошибка: " + e.getMessage());
                System.out.println("Попробуйте другое слово.");
            } catch (WordAlreadyUsedException e) {
                System.out.println("Ошибка: " + e.getMessage());
                System.out.println("Вы уже использовали это слово.");
            } catch (IllegalStateException e) {
                System.out.println("Ошибка: " + e.getMessage());
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
            }
        }
        System.out.println();
    }
}