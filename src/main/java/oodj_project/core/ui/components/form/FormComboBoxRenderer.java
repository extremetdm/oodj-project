package oodj_project.core.ui.components.form;

import java.awt.Component;
import java.util.function.Function;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingConstants;

import oodj_project.core.ui.styles.FormTheme;

public class FormComboBoxRenderer<FieldT> extends DefaultListCellRenderer {

    private final Function<FieldT, String> fieldDescriptor;

    public FormComboBoxRenderer(Function<FieldT, String> fieldDescriptor) {
        this.fieldDescriptor = fieldDescriptor;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Component getListCellRendererComponent(
        JList<?> list,
        Object value, 
        int index,
        boolean isSelected,
        boolean cellHasFocus
    ) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        label.setBorder(FormTheme.FLAT_BORDER);
        label.setHorizontalAlignment(SwingConstants.LEFT);

        // if (!isSelected) {
            // if (index % 2 == 0) {
            //     label.setBackground(Color.WHITE);
            // } else {
            //     label.setBackground(new Color(245, 245, 250));
            // }
        // }

        label.setText(fieldDescriptor.apply((FieldT) value));

        return label;
    }
}
