package oodj_project.core.ui.components;

import java.awt.event.ActionListener;

import javax.swing.JButton;

public class AddButton extends JButton {
    public AddButton(ActionListener action) {
        this("Add", action);
    }

    public AddButton(String label, ActionListener action) {
        super("Add");
        // setIcon(defaultIcon);
        addActionListener(action);
    }
}
