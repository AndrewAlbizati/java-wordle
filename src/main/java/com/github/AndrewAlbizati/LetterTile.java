package com.github.AndrewAlbizati;

import javax.swing.*;
import java.awt.*;

public class LetterTile extends JTextField {
    private final int guess;
    public int getGuess() {
        return guess;
    }
    private final int letterNum;
    public int getLetterNum() {
        return letterNum;
    }

    public LetterTile(int guess, int letterNum) {
        this.guess = guess;
        this.letterNum = letterNum;

        this.setFont(new Font("Verdana", Font.PLAIN, 20));
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setBackground(new Color(0, 0, 0));
        this.setForeground(new Color(255, 255, 255));
        this.setDisabledTextColor(new Color(255, 255, 255));
    }
}
