package oodj_project.features.module_management;

import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import oodj_project.core.ui.components.form.FormLabel;
import oodj_project.core.ui.components.form.FormTextArea;
import oodj_project.core.ui.components.form.FormTextField;
import oodj_project.core.ui.layout.FlexibleGridBuilder;

public class ModuleEditFormContent extends JPanel {

    private static final double[] COLUMN_WEIGHTS = { 0, 1 };

    private final FormTextField nameField = new FormTextField();
    private final FormTextArea descriptionField = new FormTextArea(5);

    public ModuleEditFormContent() {
        this(null);
    }

    public ModuleEditFormContent(Module module) {
        super();

        var builder = new FlexibleGridBuilder(this, 2)
            .setInsets(new Insets(5, 5, 5, 5));
        
        if (module != null) {
            builder.add(
                COLUMN_WEIGHTS,
                new FormLabel("ID"),
                new FormTextField(module.id().toString(), false)
            );
            nameField.setText(module.name());
            descriptionField.setText(module.description());
        }
        
        builder.add(
            COLUMN_WEIGHTS,
            new FormLabel("Name"),
            nameField,
            new FormLabel("Description"),
            descriptionField
        )   
            .addStrutGlue(600)
            .build();

        setBorder(BorderFactory.createEmptyBorder(20, 15, 10, 15));
    }

    public Module getFormData() {
        return new Module(
            nameField.getText(),
            descriptionField.getText()
        );
    }
}