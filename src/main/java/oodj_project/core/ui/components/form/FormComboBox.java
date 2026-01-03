package oodj_project.core.ui.components.form;

import java.util.Collection;
import java.util.function.Function;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import oodj_project.core.ui.styles.FormTheme;

public class FormComboBox<FieldT> extends JComboBox<FieldT> {
    public FormComboBox(Function<FieldT, String> fieldDescriptor) {
        super();
        setup(fieldDescriptor);
    }

    public FormComboBox(Function<FieldT, String> fieldDescriptor, ComboBoxModel<FieldT> model) {
        super(model);
        setup(fieldDescriptor);
    }

    public FormComboBox(Function<FieldT, String> fieldDescriptor, FieldT[] items) {
        super(items);
        setup(fieldDescriptor);
    }

    public FormComboBox(Function<FieldT, String> fieldDescriptor, Collection<FieldT> items) {
        super();
        DefaultComboBoxModel<FieldT> model = new DefaultComboBoxModel<>();
        model.addAll(items);
        setModel(model);
        setup(fieldDescriptor);
    }

    private void setup(Function<FieldT, String> fieldDescriptor) {
        setRenderer(new FormComboBoxRenderer<>(fieldDescriptor));
        setBackground(FormTheme.BACKGROUND_COLOR);
        setFont(FormTheme.INPUT_FONT);

        // var a = (MetalComboBoxEditor) getEditor();
    }

    @Override
    @SuppressWarnings("unchecked")
    public FieldT getSelectedItem() {
        return (FieldT) super.getSelectedItem();
    }
}
