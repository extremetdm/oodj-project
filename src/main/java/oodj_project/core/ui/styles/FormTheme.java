package oodj_project.core.ui.styles;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class FormTheme {

    public static final Font
        TITLE_FONT = new Font("Swis721 BlkCn BT", Font.BOLD, 50),
        LABEL_FONT = new Font("Arial", Font.BOLD, 15),
        INPUT_FONT = new Font("Arial", Font.PLAIN, 15);

    private static final int
        PADDING_X = 5,
        PADDING_Y = 5,
        OUTLINE_SIZE = 1,
        INSET_X = PADDING_X + OUTLINE_SIZE,
        INSET_Y = PADDING_Y + OUTLINE_SIZE;

    public static final Border
        PADDING = BorderFactory.createEmptyBorder(PADDING_Y, PADDING_X, PADDING_Y, PADDING_X),
        OUTLINE = BorderFactory.createLineBorder(Color.GRAY, OUTLINE_SIZE),
        INPUT_BORDER = BorderFactory.createCompoundBorder(OUTLINE, PADDING),
        FLAT_BORDER = BorderFactory.createEmptyBorder(INSET_Y, INSET_X, INSET_Y, INSET_X);
}
