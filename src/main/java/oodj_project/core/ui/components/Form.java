package oodj_project.core.ui.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
public class Form extends JDialog {

    protected Form(Builder builder) {
        super(builder.parentWindow, builder.windowTitle, Dialog.ModalityType.APPLICATION_MODAL);

        setLayout(new BorderLayout());

        var title = new JLabel(builder.formTitle);
        
        add(title, BorderLayout.NORTH);
        add(builder.content, BorderLayout.CENTER);
        add(createActionPanel(builder), BorderLayout.SOUTH);
        
        pack();
    }

    private JPanel createActionPanel(Builder builder) {
        var actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));

        actionPanel.add(Box.createHorizontalGlue());

        var backButton = new JButton("Back");
        backButton.addActionListener(event -> dispose());

        actionPanel.add(backButton);
        actionPanel.add(Box.createHorizontalStrut(5));

        var updateButton = new JButton(builder.confirmText);
        updateButton.addActionListener(builder.onConfirm);

        actionPanel.add(updateButton);

        return actionPanel;
    }

    public static class Builder {
        private final Window parentWindow;
        private final Component content;
        private final ActionListener onConfirm;

        private String windowTitle = "";
        private String formTitle = "";
        private String confirmText = "OK";

        public Builder(Window parentWindow, Component content, ActionListener onConfirm) {
            this.parentWindow = parentWindow;
            this.content = content;
            this.onConfirm = onConfirm;
        }
        
        public Builder windowTitle(String title) {
            windowTitle = title;
            return this;
        }

        public Builder formTitle(String title) {
            formTitle = title;
            return this;
        }

        public Builder confirmText(String label) {
            confirmText = label;
            return this;
        }

        public Form build() {
            return new Form(this);
        }
    }

    public static Builder builder(Window parentWindow, Component content, ActionListener onConfirm) {
        return new Builder(parentWindow, content, onConfirm);
    }
}