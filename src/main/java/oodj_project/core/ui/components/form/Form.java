package oodj_project.core.ui.components.form;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutFocusTraversalPolicy;
import javax.swing.text.JTextComponent;

import oodj_project.core.ui.styles.FormTheme;

public class Form extends JDialog {

    protected Form(Builder builder) {
        super(builder.parentWindow, builder.windowTitle, Dialog.ModalityType.APPLICATION_MODAL);

        setLayout(new BorderLayout());

        var title = new JLabel(builder.formTitle);
        title.setFont(FormTheme.TITLE_FONT);
        title.setBorder(BorderFactory.createEmptyBorder(10, 30, 0, 30));
        
        add(title, BorderLayout.NORTH);
        add(builder.content, BorderLayout.CENTER);
        add(createActionPanel(builder), BorderLayout.SOUTH);
        
        pack();

        setFocusTraversalPolicy(new LayoutFocusTraversalPolicy() {
            @Override
            protected boolean accept(Component component) {
                if (!super.accept(component)) return false;

                return switch(component) {
                    case JTextComponent textComponent -> textComponent.isEditable();
                    default -> true;
                };
            }
        });

        setFocusTraversalPolicyProvider(true);
    }

    private JPanel createActionPanel(Builder builder) {
        var actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        actionPanel.add(Box.createHorizontalGlue());

        var cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(event -> dispose());

        actionPanel.add(cancelButton);
        actionPanel.add(Box.createHorizontalStrut(15));

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