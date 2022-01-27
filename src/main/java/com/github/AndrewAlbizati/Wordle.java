package com.github.AndrewAlbizati;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class Wordle extends JFrame {
    public Wordle() {
        this.setLayout(new GridLayout(6, 6));
        this.setSize(500,500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LetterTile[][] guesses = new LetterTile[6][5];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                LetterTile guess = new LetterTile(i, j);
                guess.getDocument().addDocumentListener(new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        System.out.println("changed update");
                        update();
                    }
                    public void removeUpdate(DocumentEvent e) {
                        System.out.println("remove update");
                        update();
                    }
                    public void insertUpdate(DocumentEvent e) {
                        System.out.println("insert update");
                        update();
                    }

                    public void update() {
                        Runnable runnable = () -> {
                            if (guess.getText().matches("[^a-zA-Z]")) {
                                guess.setText(guess.getText().replaceAll("[^a-zA-Z]", ""));
                            }

                            if (guess.getText().length() > 1) {
                                guess.setText(guess.getText().substring(1));
                            }

                            SwingUtilities.invokeLater(() -> guesses[guess.getGuess()][guess.getLetterNum() + 1].requestFocus());
                        };
                        SwingUtilities.invokeLater(runnable);
                    }
                });
                this.add(guess);
                guesses[i][j] = guess;
            }
            this.add(new JButton());
        }

    }
}
