package com.github.AndrewAlbizati;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

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
        }
        System.out.println(word);


        Wordle wordle = new Wordle();
        wordle.setVisible(true);
    }
}
