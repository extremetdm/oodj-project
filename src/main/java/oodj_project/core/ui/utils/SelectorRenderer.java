package oodj_project.core.ui.utils;

import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingConstants;

public class SelectorRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(
        JList<?> list,
        Object value, 
        int index,
        boolean isSelected,
        boolean cellHasFocus
    ) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setHorizontalAlignment(SwingConstants.LEFT);

        // if (!isSelected) {
        //     if (index % 2 == 0) {
        //         label.setBackground(Color.WHITE);
        //     } else {
        //         label.setBackground(new Color(245, 245, 250));
        //     }
        // }

        return label;
    }
}
