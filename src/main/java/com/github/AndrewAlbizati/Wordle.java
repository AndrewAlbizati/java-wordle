package com.github.AndrewAlbizati;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Wordle extends JFrame {
    private final LetterTile[][] guesses = new LetterTile[6][5];

    public Wordle() {
        this.setLayout(new GridLayout(6, 6));
        this.setSize(500,500);
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

                            if (guess.getLetterNum() < guesses[guess.getLetterNum()].length - 1) {
                                SwingUtilities.invokeLater(() -> guesses[guess.getGuess()][guess.getLetterNum() + 1].requestFocus());
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

        System.out.println(builder.toString());
    }


    private static LetterStatus[] checkGuess(String guess, String realWord) {
        LetterStatus[] status = new LetterStatus[5];

        for (int i = 0; i < guess.length(); i++) {
            String guessedLetter = String.valueOf(guess.charAt(i)).toLowerCase();
            String realLetter = String.valueOf(realWord.charAt(i)).toLowerCase();

            if (guessedLetter.equals(realLetter)) {
                status[i] = LetterStatus.CORRECT;
            } else if (realWord.contains(guessedLetter)) {
                status[i] = LetterStatus.WRONG_SPOT;
            } else {
                status[i] = LetterStatus.NOT_IN_WORD;
            }
        }

        return status;
    }
}
