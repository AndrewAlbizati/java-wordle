package com.github.AndrewAlbizati;

import javax.swing.*;

public class CheckButton extends JButton {
    private final int guessNumber;

    public int getGuessNumber() {
        return guessNumber;
    }

    public CheckButton(int guessNumber) {
        this.guessNumber = guessNumber;
        this.setText("Check");
    }
}
