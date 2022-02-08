package com.github.AndrewAlbizati;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Wordle extends JFrame {
    private final int wordSize;

    private final LetterTile[][] guesses;
    private final CheckButton[] checkButtons;

    private LetterTile tileSelected;

    private final String word;

    /**
     * Creates a new Wordle game that can be started by the setVisible() method.
     * @param word The word that the game will be generated for.
     */
    public Wordle(String word) {
        this.word = word;
        wordSize = word.length();

        guesses = new LetterTile[wordSize + 1][wordSize];
        checkButtons = new CheckButton[wordSize + 1];

        this.setLayout(new GridLayout(wordSize + 1, wordSize + 1));
        this.setSize(wordSize * 100, wordSize * 100);
        this.setTitle("Wordle (" + word.length() + " letters)");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Try to add Wordle logo
        try {
            InputStream logoStream = TitleScreen.class.getResourceAsStream("/logo.png");
            if (logoStream == null)
                throw new NullPointerException("logo.png not found");

            Image image = ImageIO.read(logoStream);

            this.setIconImage(image);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace(); // Ignore, use default Java logo for icon
        }

        // Add guessing text tiles and "check" button tiles
        for (int i = 0; i < wordSize + 1; i++) {
            for (int j = 0; j < wordSize; j++) {
                LetterTile guess = new LetterTile(i, j);

                // Add event handlers for when user types
                guess.getDocument().addDocumentListener(new DocumentListener() {

                    public void changedUpdate(DocumentEvent e) {
                        //update();
                    }

                    public void removeUpdate(DocumentEvent e) {
                        //update();
                    }

                    public void insertUpdate(DocumentEvent e) {
                        update();
                    }

                    public void update() {
                        Runnable runnable = () -> {
                            // Remove any non-letters
                            if (guess.getText().matches("[^a-zA-Z]")) {
                                guess.setText(guess.getText().replaceAll("[^a-zA-Z]", ""));
                                return;
                            }

                            // Remove extra letters if a guess is 2 or more letters
                            if (guess.getText().length() > 1) {
                                guess.setText(guess.getText().substring(1));
                            }

                            if (guess.getText().length() > 0) {
                                // Set guess to UPPER CASE
                                if (Character.isLowerCase(guess.getText().charAt(0))) {
                                    guess.setText(guess.getText().toUpperCase());
                                }

                                // Set user's focus to the next tile
                                if (guess.getLetterNum() < guesses[guess.getLetterNum()].length - 1) {
                                    SwingUtilities.invokeLater(() -> guesses[guess.getGuess()][guess.getLetterNum() + 1].requestFocus());
                                    tileSelected = guesses[guess.getGuess()][guess.getLetterNum() + 1];
                                }
                            }
                        };
                        SwingUtilities.invokeLater(runnable);
                    }
                });

                // Add keystroke action for backspace
                guess.getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"), "backspaceAction");
                guess.getActionMap().put("backspaceAction", new BackspaceAction());

                // Add keystroke action for enter
                guess.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "enterAction");
                guess.getActionMap().put("enterAction", new EnterAction());

                this.add(guess);
                guesses[i][j] = guess;
                if (i > 0) {
                    guess.setEnabled(false);
                }
            }

            // Add "check" buttons
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

        tileSelected = guesses[0][0];
        SwingUtilities.invokeLater(() -> tileSelected.requestFocus());
    }

    private class BackspaceAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!tileSelected.getText().isEmpty()) {
                tileSelected.setText("");
            } else if (tileSelected.getLetterNum() > 0){
                tileSelected = guesses[tileSelected.getGuess()][tileSelected.getLetterNum() - 1];
                SwingUtilities.invokeLater(() -> tileSelected.requestFocus());
            }
        }
    }

    private class EnterAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            onLeftClick(checkButtons[tileSelected.getGuess()]);
        }
    }

    /**
     * Handles whenever a player left-clicks on a "check" button
     * @param button The button that the player clicked on.
     */
    private void onLeftClick(CheckButton button) {
        LetterTile[] guess = guesses[button.getGuessNumber()];
        StringBuilder builder = new StringBuilder();

        // Create a guess from the letters stored in the letter tiles
        for (LetterTile letterTile : guess) {
            if (letterTile != null && !letterTile.getText().isEmpty()) {
                builder.append(letterTile.getText());
                continue;
            }
            return;
        }

        // Word submitted isn't found in the eligible-words.json file
        if (!isValidWord(builder.toString())) {
            JOptionPane.showMessageDialog(null, "That is not a valid word! Guess again.");
            return;
        }

        LetterStatus[] guessStatus = checkGuess(builder.toString(), word);

        int correctLetters = 0;

        // Set each letter to its status color
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

        // Player wins
        if (correctLetters == wordSize) {
            onWin();
            return;
        }

        // Player runs out of guesses
        if (button.getGuessNumber() + 1 > wordSize) {
            onLose();
            return;
        }

        // Disable current guess, enable next guess
        for (int i = 0; i < guess.length; i++) {
            guesses[button.getGuessNumber() + 1][i].setEnabled(true);
            checkButtons[button.getGuessNumber() + 1].setEnabled(true);
        }

        SwingUtilities.invokeLater(() -> guesses[button.getGuessNumber() + 1][0].requestFocus());
    }

    /**
     * Compares a guess to the real hidden word and determines:
     * which letters are in the correct place,
     * which letters are in the wrong position but in the word,
     * which letters aren't in the word at all.
     *
     * @param guess The guess that the user has submitted.
     * @param word The hidden word that the user is trying to guess.
     * @return An array of LetterStatuses that correspond to the letters in the guess.
     */
    private static LetterStatus[] checkGuess(String guess, String word) {
        LetterStatus[] status = new LetterStatus[guess.length()];

        // Go through each letter
        // If the guessed letter matches the real letter, it is CORRECT
        // If the guessed letter is in the real word, it is WRONG_SPOT
        // If the guessed letter isn't in the real word, it is NOT_IN_WORD
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

    /**
     * Runs when the player wins the game.
     * Shows a pop-up menu telling them that they guessed the word correctly.
     */
    private void onWin() {
        JOptionPane.showMessageDialog(null, "You guessed it! The word was " + word + ".");
    }

    /**
     * Runs when the player runs out of guesses.
     * Shows a pop-up memu telling them what the word was.
     */
    private void onLose() {
        JOptionPane.showMessageDialog(null, "You ran out of guesses! The word was " + word + ".");
    }

    /**
     * Determines if a word that a user has guessed is a valid dictionary word.
     *
     * @param word The word that the user inputted.
     * @return If the word can be found in the list of eligible words.
     */
    private static boolean isValidWord(String word) {
        try {
            InputStream jsonStream = Wordle.class.getResourceAsStream("/eligible-words.json");
            JSONParser parser = new JSONParser();

            if (jsonStream == null) {
                throw new NullPointerException("eligible-words.json was not found");
            }

            JSONArray words = (JSONArray) parser.parse(new InputStreamReader(jsonStream, "UTF-8"));

            return words.contains(word.toLowerCase());
        } catch (IOException | ParseException | NullPointerException e) {
            e.printStackTrace();
        }

        return false;
    }
}
