package oodj_project.core.ui.components.form;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import oodj_project.core.ui.styles.FormTheme;

public class FormSpinner<FieldT> extends JSpinner {

    public FormSpinner() {
        super();
        setup();
    }
    
    public FormSpinner(SpinnerModel model) {
        super(model);
        setup();
    }

    public FormSpinner(SpinnerNumberModel model, String decimalFormatPattern) {
        super(model);
        var editor = (NumberEditor) getEditor();
        editor.getFormat().applyPattern(decimalFormatPattern);
        editor.getTextField().setValue(model.getValue());
        setup();
    }

    private void setup() {
        setBorder(FormTheme.INPUT_BORDER);
        setFont(FormTheme.INPUT_FONT);
        // setAlignmentX(LEFT_ALIGNMENT);
        setBackground(FormTheme.BACKGROUND_COLOR);

        var editor = (DefaultEditor) getEditor();
        editor.getTextField().setHorizontalAlignment(SwingConstants.LEFT);
        editor.getTextField().setBackground(FormTheme.BACKGROUND_COLOR);
    }

    @Override
    @SuppressWarnings("unchecked")
    public FieldT getValue() {
        return (FieldT) super.getValue();
    }
}
