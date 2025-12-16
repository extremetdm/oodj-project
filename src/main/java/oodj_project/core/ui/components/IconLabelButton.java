package oodj_project.core.ui.components;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;

import oodj_project.core.ui.utils.Clickable;

public class IconLabelButton extends JButton {
    public IconLabelButton(String label, Icon icon) {
        super(label, icon);
        setFont(new Font("SansSerif", Font.PLAIN, 20));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        addMouseListener(Clickable.INSTANCE);
    }
}
