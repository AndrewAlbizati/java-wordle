# Wordle
This program is a Wordle clone written in Java 17 using Swing.

## Setup
1. Run `java --version` into a terminal/command prompt. Make sure the version is 17+.
2. Start a terminal/command prompt and locate the main folder.
3. If on Windows:
    1. Run `gradlew clean`
    2. Run `gradlew build`
    3. Run the JAR file in `\build\libs\java-wordle-1.0.0.jar`
4. If on macOS or Linux:
    1. Run `chmod +x gradlew`
    2. Run `./gradlew clean`
    3. Run `./gradlew build`
    4. Run the JAR file in `/build/libs/java-wordle-1.0.0.jar`

## How to play
To play Wordle, the player has 6 attempts to guess a hidden 5-letter word.
To make a guess, the player needs to type in the letters for a valid 5-letter word, press the "check" button, and the game will show the player:

- Which letters they've guessed correctly (shown in green)
- Which letters are in the word but were in the wrong position (shown in yellow)
- Which letters aren't in the word at all (shown in gray)

The game will generate a new 5-letter word once per day.

## Dependencies
- JSON-Simple 1.1.1 (https://github.com/fangyidong/json-simple)