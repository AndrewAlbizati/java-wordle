package com.github.AndrewAlbizati;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        String word = null;
        try {
            InputStream jsonStream = Main.class.getResourceAsStream("/words.json");
            JSONParser parser = new JSONParser();

            JSONArray words = (JSONArray) parser.parse(new InputStreamReader(jsonStream, "UTF-8"));

            word = (String) words.get((int) (Math.random() * words.size()));
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
