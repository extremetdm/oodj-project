package oodj_project.features.dashboard.grading_system_management;

import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;

import oodj_project.core.ui.components.form.FormComboBox;
import oodj_project.core.ui.components.form.FormLabel;
import oodj_project.core.ui.components.form.FormSpinner;
import oodj_project.core.ui.components.form.FormTextField;
import oodj_project.core.ui.layout.FlexibleGridBuilder;

public class GradeEditFormContent extends JPanel {

    private static final double[] COLUMN_WEIGHTS = { 0, 1 };

    private final FormTextField nameField = new FormTextField();
    private final FormSpinner<Integer> 
        minField = new FormSpinner<>(
            new SpinnerNumberModel(0, 0, 100, 1)
        ),
        maxField = new FormSpinner<>(
            new SpinnerNumberModel(0, 0, 100, 1)
        );
        
    private final FormComboBox<Grade.Classification> classificationField = new FormComboBox<>(
        Grade.Classification::name, Grade.Classification.values()
    );

    public GradeEditFormContent() {
        this(null);
    }

    public GradeEditFormContent(Grade grade) {
        super();

        var builder = new FlexibleGridBuilder(this, 2)
            .setInsets(new Insets(5, 5, 5, 5));
        
        if (grade != null) {
            // builder.add(
            //     COLUMN_WEIGHTS,
            //     new FormLabel("ID"),
            //     new FormTextField(grade.id().toString(), false)
            // );
            nameField.setText(grade.name());
            minField.setValue(grade.min());
            maxField.setValue(grade.max());
            classificationField.setSelectedItem(grade.classification());
        }
        
        builder.add(
            COLUMN_WEIGHTS,
            new FormLabel("Name"),
            nameField,
            new FormLabel("Min mark"),
            minField,
            new FormLabel("Max mark"),
            maxField,
            new FormLabel("Classification"),
            classificationField
        )   
            .addStrutGlue(600)
            .build();

        setBorder(BorderFactory.createEmptyBorder(20, 15, 10, 15));
    }

    public Grade getFormData() {
        return new Grade(
            nameField.getText(),
            minField.getValue(),
            maxField.getValue(),
            classificationField.getSelectedItem()
        );
    }
}