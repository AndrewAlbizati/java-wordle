package com.github.AndrewAlbizati;

import javax.swing.*;

public class LetterTile extends JTextField {
    private final int guess;
    public int getGuess() {
        return guess;
    }
    private final int letterNum;
    public int getLetterNum() {
        return letterNum;
    }

    public LetterTile (int guess, int letterNum) {
        this.guess = guess;
        this.letterNum = letterNum;
    }
}
