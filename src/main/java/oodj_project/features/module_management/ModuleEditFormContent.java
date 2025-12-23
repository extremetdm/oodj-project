package oodj_project.features.module_management;

import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import oodj_project.core.ui.utils.GridBuilder;

public class ModuleEditFormContent extends JPanel {

    private static final double[] LABEL_TO_FIELD_WIDTH_RATIO = { 1, 3 };

    private final JTextField nameField = new JTextField();
    private final JTextField descriptionField = new JTextField();

    public ModuleEditFormContent() {
        this(null);
    }

    public ModuleEditFormContent(Module module) {
        super();

        var builder = new GridBuilder(this)
            .setColumnWeights(LABEL_TO_FIELD_WIDTH_RATIO)
            .setMinWidth(600)
            .setRowHeight(30)
            // .setRowStyler((panel, index) -> {
            //     panel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
            // });
            .setInsets(new Insets(5, 5, 5, 5));

        if (module != null) {
            builder.addRow(
                new JLabel("ID"),
                new JLabel(module.id().toString())
            );
            nameField.setText(module.name());
            descriptionField.setText(module.description());
        }
        
        builder.addRow(
            new JLabel("Name"),
            nameField
        )   .addRow(
            new JLabel("Description"),
            descriptionField
        )   .build();
    }

    public Module getFormData() {
        return new Module(
            nameField.getText(),
            descriptionField.getText()
        );
    }
}