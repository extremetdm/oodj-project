package oodj_project.features.dashboard.assessment_management;

import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;

import oodj_project.core.ui.components.form.FormComboBox;
import oodj_project.core.ui.components.form.FormLabel;
import oodj_project.core.ui.components.form.FormSpinner;
import oodj_project.core.ui.components.form.FormTextField;
import oodj_project.core.ui.layout.FlexibleGridBuilder;
import oodj_project.features.dashboard.class_management.ClassGroup;

public class AssessmentEditFormContent extends JPanel {

    private static final double[] COLUMN_WEIGHTS = { 0, 1 };

    private final FormTextField nameField = new FormTextField();
    private final FormSpinner<Integer> marksField = new FormSpinner<>(
        new SpinnerNumberModel(0, 0, null, 1)
    );
    private final FormComboBox<Assessment.Type> typeField = new FormComboBox<>(
        Assessment.Type::name, Assessment.Type.values()
    );
    private final FormComboBox<ClassGroup> classField;
    public AssessmentEditFormContent(
        List<ClassGroup> classes
    ) {
        this(classes, null);
    }

    public AssessmentEditFormContent(
        List<ClassGroup> classes,
        Assessment assessment
    ) {
        super();

        classField = new FormComboBox<>(classGroup -> classGroup.id() + " - " + classGroup.module().name(), classes);

        var builder = new FlexibleGridBuilder(this, 2)
            .setInsets(new Insets(5, 5, 5, 5));
        
        if (assessment != null) {
            builder.add(
                COLUMN_WEIGHTS,
                new FormLabel("ID"),
                new FormTextField(assessment.id().toString(), false)
            );
            nameField.setText(assessment.name());
            classField.setSelectedItem(assessment.classGroup());
            typeField.setSelectedItem(assessment.type());
            marksField.setValue(assessment.marks());
        }
        
        builder.add(
            COLUMN_WEIGHTS,
            new FormLabel("Name"),
            nameField,
            new FormLabel("Class"),
            classField,
            new FormLabel("Type"),
            typeField,
            new FormLabel("Marks"),
            marksField
        )   
            .addStrutGlue(600)
            .build();

        setBorder(BorderFactory.createEmptyBorder(20, 15, 10, 15));
    }

    public Assessment getFormData() {
        return new Assessment(
            nameField.getText(),
            classField.getSelectedItem(),
            typeField.getSelectedItem(),
            marksField.getValue()
        );
    }
}