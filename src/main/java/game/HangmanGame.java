package game;

import auth.ConnectionDB;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HangmanGame {
    private ArrayList<String> words;
    private Map<String, String> hints;
    private String secretWord;
    private String guessedWord;
    private int attemptsLeft;
    private int score;
    private ConnectionDB connectionDB;
    private int userId;


    public HangmanGame() {

        words = readWordsFromFile("D:\\repos\\OOD\\untitled\\src\\main\\java\\txt\\words.txt");
        hints = readHintsFromFile("D:\\repos\\OOD\\untitled\\src\\main\\java\\txt\\hint.txt");
        String[] randomWordAndHint = chooseRandomWordAndHint();
        secretWord = randomWordAndHint[0];
        guessedWord = "*".repeat(secretWord.length());
        attemptsLeft = 6;



    }

    private ArrayList<String> readWordsFromFile(String fileName) {
        ArrayList<String> wordList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                wordList.add(line.toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return wordList;
    }

    private Map<String, String> readHintsFromFile(String fileName) {
        Map<String, String> hintMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    hintMap.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hintMap;
    }

    private String[] chooseRandomWordAndHint() {
        Random random = new Random();
        int index = random.nextInt(words.size());
        String randomWord = words.get(index);
        String randomHint = hints.get(randomWord);
        words.remove(index);
        return new String[]{randomWord, randomHint};
    }

    public boolean guessLetter(char letter) {
        letter = Character.toLowerCase(letter);

        if (secretWord.contains(String.valueOf(letter))) {
            StringBuilder newGuessedWord = new StringBuilder(guessedWord);

            for (int i = 0; i < secretWord.length(); i++) {
                if (secretWord.charAt(i) == letter) {
                    newGuessedWord.setCharAt(i, letter);
                }
            }

            guessedWord = newGuessedWord.toString();
            return true;
        } else {
            attemptsLeft--;
            return false;
        }
    }

    public boolean isGameOver() {
        return attemptsLeft == 0 || guessedWord.equals(secretWord);
    }

    public boolean isGameWon() {
        return guessedWord.equals(secretWord);
    }

    public String getGuessedWord() {
        return guessedWord;
    }

    public String getSecretWord() {
        return secretWord;
    }

    public int getAttemptsLeft() {
        return attemptsLeft;
    }

    public String getHint(String word) {
        return hints.get(word);
    }

    public void incrementScore() {
        score += 10;

    }

    public int getScore() {
        return score;
    }

}
