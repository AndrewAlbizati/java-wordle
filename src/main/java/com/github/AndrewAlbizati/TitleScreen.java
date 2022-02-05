package com.github.AndrewAlbizati;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Random;

public class TitleScreen extends JFrame {
    public static void main(String[] args) {
        TitleScreen titleScreen = new TitleScreen();
        titleScreen.setVisible(true);
    }

    private int wordSize = 5;

    public TitleScreen() {
        this.setLayout(new GridLayout(3, 3));
        this.setSize(400, 400);
        this.setTitle("Wordle (Title Screen)");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.add(new JLabel(""));

        JLabel label = new JLabel("Wordle");
        label.setFont(new Font("Verdana", Font.BOLD, 30));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label);

        this.add(new JLabel(""));


        Font buttonFont = new Font("Verdana", Font.PLAIN, 20);

        JButton minusButton = new JButton("-");
        minusButton.setFont(buttonFont);
        minusButton.setHorizontalAlignment(SwingConstants.CENTER);

        this.add(minusButton);


        JLabel wordLengthLabel = new JLabel(wordSize + " letters");
        wordLengthLabel.setFont(buttonFont);
        wordLengthLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(wordLengthLabel);


        JButton plusButton = new JButton("+");
        plusButton.setFont(buttonFont);
        plusButton.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(plusButton);

        minusButton.addActionListener(e -> {
            if (wordSize > 3) {
                wordSize--;
                wordLengthLabel.setText(wordSize + " letters");
            }
        });
        plusButton.addActionListener(e -> {
            if (wordSize < 10) {
                wordSize++;
                wordLengthLabel.setText(wordSize + " letters");
            }
        });

        this.add(new JLabel(""));

        JButton startButton = new JButton("Start!");
        startButton.setFont(buttonFont);
        startButton.addActionListener(e -> {
            try {
                String word = randWord(wordSize);

                Wordle wordle = new Wordle(word);
                wordle.setVisible(true);
                this.setVisible(false);
            } catch (IOException | ParseException ex) {
                ex.printStackTrace();

                // Open a window that tells the user about the error
                JFrame frame = new JFrame();
                frame.setSize(400, 400);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JLabel errorLabel = new JLabel("Word couldn't be generated (" + ex.getMessage() + ").");
                label.setHorizontalAlignment(SwingConstants.CENTER);
                frame.add(errorLabel);

                frame.setVisible(true);
            }
        });

        this.add(startButton);
        this.add(new JLabel(""));
    }

    private static String randWord(int length) throws IOException, ParseException {
        InputStream jsonStream = TitleScreen.class.getResourceAsStream("/words.json");
        JSONParser parser = new JSONParser();

        JSONArray words = (JSONArray) parser.parse(new InputStreamReader(jsonStream, "UTF-8"));
        ArrayList<String> wordsWithCorrectLength = new ArrayList<>();

        for (int i = 0; i < words.size(); i++) {
            String word = (String) words.get(i);
            if (word.length() == length) {
                wordsWithCorrectLength.add(word);
            }
        }

        // Generate one random word per day
        ZonedDateTime time = Instant.now().atZone(ZoneOffset.UTC);
        int month = time.getMonthValue();
        int day = time.getDayOfMonth();
        int year = time.getYear();
        Random rand = new Random((month * 10000) + (day * 100) + (year % 100)); // Aug 16, 2021 --> 81621, Dec 25, 2022 --> 122522
        return wordsWithCorrectLength.get(rand.nextInt(wordsWithCorrectLength.size()));
    }
}
