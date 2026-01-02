package oodj_project.core.ui.components.form;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import oodj_project.core.ui.styles.FormTheme;

public class FormTextArea extends JScrollPane {
    private final JTextArea textArea;

    public FormTextArea(int rows) {
        super();
        textArea = new JTextArea(rows, 0);
        setup();
    }

    public FormTextArea(String text) {
        super();
        textArea = new JTextArea(text);
        setup();
    }

    public FormTextArea(String text, int rows) {
        super();
        textArea = new JTextArea(text, rows, 0);
        setup();
    }

    private void setup() {
        setViewportView(textArea);
        setBorder(FormTheme.OUTLINE);

        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(FormTheme.PADDING);
        textArea.setFont(FormTheme.INPUT_FONT);
        textArea.setBackground(FormTheme.BACKGROUND_COLOR);
    }

    public String getText() {
        return textArea.getText();
    }

    public void setText(String text) {
        textArea.setText(text);
    }

    public JTextArea getTextArea() {
        return textArea;
    }
}
