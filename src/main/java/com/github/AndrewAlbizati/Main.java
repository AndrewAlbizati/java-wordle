package com.github.AndrewAlbizati;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        String word = null;
        try {
            InputStream jsonStream = Main.class.getResourceAsStream("/words.json");
            JSONParser parser = new JSONParser();

            JSONArray words = (JSONArray) parser.parse(new InputStreamReader(jsonStream, "UTF-8"));

            // Generate one random word per day
            ZonedDateTime time = Instant.now().atZone(ZoneOffset.UTC);
            int month = time.getMonthValue();
            int day = time.getDayOfMonth();
            int year = time.getYear();
            Random rand = new Random((month * 10000) + (day * 100) + (year % 100)); // Aug 16, 2021 --> 81621, Dec 25, 2022 --> 122522
            word = (String) words.get(rand.nextInt(words.size()));
        } catch (IOException | ParseException e) {
            e.printStackTrace();

            // Open a window that tells the user about the error
            JFrame frame = new JFrame();
            frame.setSize(400, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JLabel label = new JLabel("Word couldn't be generated (" + e.getMessage() + ").");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            frame.add(label);

            frame.setVisible(true);
        }

        // Generate new Wordle game
        if (word != null) {
            Wordle wordle = new Wordle(word);
            wordle.setVisible(true);
        }
    }
}
