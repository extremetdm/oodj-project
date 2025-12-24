package oodj_project.features.module_management;

import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import oodj_project.core.ui.components.form.FormLabel;
import oodj_project.core.ui.components.form.FormTextArea;
import oodj_project.core.ui.components.form.FormTextField;
import oodj_project.core.ui.utils.GridBuilder;

public class ModuleEditFormContent extends JPanel {

    private static final double[] LABEL_TO_FIELD_WIDTH_RATIO = { 1, 4 };

    private final FormTextField nameField = new FormTextField();
    private final FormTextArea descriptionField = new FormTextArea(5);

    public ModuleEditFormContent() {
        this(null);
    }

    public ModuleEditFormContent(Module module) {
        super();

        var builder = new GridBuilder(this)
            .setColumnWeights(LABEL_TO_FIELD_WIDTH_RATIO)
            .setMinWidth(600)
            .setRowHeight(30)
            .setInsets(new Insets(5, 5, 5, 5));

        if (module != null) {
            builder.addRow(
                new FormLabel("ID"),
                new FormTextField(module.id().toString(), false)
            );
            nameField.setText(module.name());
            descriptionField.setText(module.description());
        }
        
        builder.addRow(
            new FormLabel("Name"),
            nameField
        )   .addRow(
            descriptionField.getPreferredSize().height, // IDK why 3
            new FormLabel("Description"),
            descriptionField
        )   .build();

        setBorder(BorderFactory.createEmptyBorder(20, 15, 10, 15));
    }

    public Module getFormData() {
        return new Module(
            nameField.getText(),
            descriptionField.getText()
        );
    }
}