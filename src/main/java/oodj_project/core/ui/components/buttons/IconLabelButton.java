package oodj_project.core.ui.components.buttons;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;

public class IconLabelButton extends JButton {
    public IconLabelButton(String label, Icon icon) {
        super(label, icon);
        setFont(new Font("SansSerif", Font.PLAIN, 20));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
