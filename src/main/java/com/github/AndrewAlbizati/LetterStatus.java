package com.github.AndrewAlbizati;

import java.awt.*;

public enum LetterStatus {
    CORRECT(new Color(83, 141, 78)),
    WRONG_SPOT(new Color(181, 159,59)),
    NOT_IN_WORD(new Color(58, 58, 60));

    public final Color color;
    LetterStatus(Color color) {
        this.color = color;
    }
}
