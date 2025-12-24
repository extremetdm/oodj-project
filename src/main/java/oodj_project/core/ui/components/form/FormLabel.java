package oodj_project.core.ui.components.form;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import oodj_project.core.ui.styles.FormTheme;

public class FormLabel extends JLabel {

    public FormLabel(String text) {
        super(text);
        setFont(FormTheme.LABEL_FONT);
        setVerticalAlignment(SwingConstants.TOP);
        setHorizontalAlignment(SwingConstants.RIGHT);
        setBorder(FormTheme.FLAT_BORDER);
    }
}
