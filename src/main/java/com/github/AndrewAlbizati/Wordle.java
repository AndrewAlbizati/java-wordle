package com.github.AndrewAlbizati;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Wordle extends JFrame {
    private final LetterTile[][] guesses = new LetterTile[6][5];
    private final CheckButton[] checkButtons = new CheckButton[6];

    private final String word;

    public Wordle(String word) {
        this.word = word;

        this.setLayout(new GridLayout(6, 6));
        this.setSize(500,500);
        this.setTitle("Wordle");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                LetterTile guess = new LetterTile(i, j);
                guess.getDocument().addDocumentListener(new DocumentListener() {
                    public void changedUpdate(DocumentEvent e) {
                        update();
                    }
                    public void removeUpdate(DocumentEvent e) {
                        update();
                    }
                    public void insertUpdate(DocumentEvent e) {
                        update();
                    }

                    public void update() {
                        Runnable runnable = () -> {
                            if (guess.getText().matches("[^a-zA-Z]")) {
                                guess.setText(guess.getText().replaceAll("[^a-zA-Z]", ""));
                                return;
                            }

                            if (guess.getText().length() > 1) {
                                guess.setText(guess.getText().substring(1));
                            }

                            if (guess.getText().length() > 0) {
                                if (Character.isLowerCase(guess.getText().charAt(0))) {
                                    guess.setText(guess.getText().toUpperCase());
                                }

                                if (guess.getLetterNum() < guesses[guess.getLetterNum()].length - 1) {
                                    SwingUtilities.invokeLater(() -> guesses[guess.getGuess()][guess.getLetterNum() + 1].requestFocus());
                                }
                            }
                        };
                        SwingUtilities.invokeLater(runnable);
                    }
                });
                this.add(guess);
                guesses[i][j] = guess;
                if (i > 0) {
                    guess.setEnabled(false);
                }
            }
            CheckButton button = new CheckButton(i);
            button.addMouseListener(new MouseAdapter() {
                boolean pressed;

                @Override
                public void mousePressed(MouseEvent e) {
                    pressed = true;
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (pressed) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            onLeftClick(button);
                        }
                    }
                    pressed = false;
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    pressed = false;
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    pressed = true;
                }
            });

            if (i > 0) {
                button.setEnabled(false);
            }
            checkButtons[i] = button;

            this.add(button);
        }
    }

    private void onLeftClick(CheckButton button) {
        LetterTile[] guess = guesses[button.getGuessNumber()];
        StringBuilder builder = new StringBuilder();

        for (LetterTile letterTile : guess) {
            if (letterTile != null && !letterTile.getText().isEmpty()) {
                builder.append(letterTile.getText());
                continue;
            }
            return;
        }

        LetterStatus[] guessStatus = checkGuess(builder.toString());

        int correctLetters = 0;

        for (int i = 0; i < guess.length; i++) {
            if (guessStatus[i].equals(LetterStatus.CORRECT)) {
                correctLetters++;
            }
            LetterTile tile = guesses[button.getGuessNumber()][i];
            tile.setBackground(guessStatus[i].color);
            tile.setEnabled(false);
            tile.setEditable(false);
        }
        button.setEnabled(false);

        if (correctLetters == 5) {
            onWin();
            return;
        }

        if (button.getGuessNumber() + 1 > 5) {
            onLose();
            return;
        }

        for (int i = 0; i < guess.length; i++) {
            guesses[button.getGuessNumber() + 1][i].setEnabled(true);
            checkButtons[button.getGuessNumber() + 1].setEnabled(true);
        }

        SwingUtilities.invokeLater(() -> guesses[button.getGuessNumber() + 1][0].requestFocus());
    }

    private LetterStatus[] checkGuess(String guess) {
        LetterStatus[] status = new LetterStatus[5];

        for (int i = 0; i < guess.length(); i++) {
            String guessedLetter = String.valueOf(guess.charAt(i)).toLowerCase();
            String realLetter = String.valueOf(word.charAt(i)).toLowerCase();

            if (guessedLetter.equals(realLetter)) {
                status[i] = LetterStatus.CORRECT;
            } else if (word.contains(guessedLetter)) {
                status[i] = LetterStatus.WRONG_SPOT;
            } else {
                status[i] = LetterStatus.NOT_IN_WORD;
            }
        }

        return status;
    }

    private void onWin() {
        JOptionPane.showMessageDialog(null, "You win! The word was " + word + ".");
    }

    private void onLose() {
        JOptionPane.showMessageDialog(null, "You lose! The word was " + word + ".");
    }
}
