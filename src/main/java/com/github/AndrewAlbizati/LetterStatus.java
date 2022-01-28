package com.github.AndrewAlbizati;

import java.awt.*;

public enum LetterStatus {
    CORRECT(new Color(83, 141, 78)), // GREEN
    WRONG_SPOT(new Color(181, 159,59)), // YELLOW
    NOT_IN_WORD(new Color(58, 58, 60)); // GRAY

    public final Color color;
    LetterStatus(Color color) {
        this.color = color;
    }
}
