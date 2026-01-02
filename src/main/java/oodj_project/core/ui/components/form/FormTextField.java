package oodj_project.core.ui.components.form;

import java.awt.Color;

import javax.swing.JTextField;

import oodj_project.core.ui.styles.FormTheme;

public class FormTextField extends JTextField {

    public FormTextField() {
        this(null, true);
    }

    public FormTextField(String text) {
        this(text, true);
    }

    public FormTextField(boolean isEditable) {
        this(null, isEditable);
    }

    public FormTextField(String text, boolean isEditable) {
        super(text);
        
        if (isEditable) {
            setBorder(FormTheme.INPUT_BORDER);
            setFont(FormTheme.INPUT_FONT);
            setBackground(FormTheme.BACKGROUND_COLOR);
        } else {
            setBorder(FormTheme.FLAT_BORDER);
            setEditable(false);
            setOpaque(false);
            setCaretColor(new Color(0, 0, 0, 0));
        }
    }
}
