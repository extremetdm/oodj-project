package oodj_project.features.dashboard.class_management;

import java.awt.Component;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;

import com.toedter.calendar.JDateChooser;

import oodj_project.core.data.repository.LineFormatter;
import oodj_project.core.security.Permission;
import oodj_project.core.security.Session;
import oodj_project.core.ui.components.form.FormComboBox;
import oodj_project.core.ui.components.form.FormLabel;
import oodj_project.core.ui.components.form.FormSpinner;
import oodj_project.core.ui.components.form.FormTextField;
import oodj_project.core.ui.layout.FlexibleGridBuilder;
import oodj_project.features.dashboard.module_management.Module;
import oodj_project.features.dashboard.user_management.User;

public class ClassEditFormContent extends JPanel {

    private static final int[] GRID_WIDTHS = { 1, 3 };
    private static final double[] COLUMN_WEIGHTS = { 0, 1 };

    private final FormSpinner<Integer> capacityField = new FormSpinner<>(
        new SpinnerNumberModel(0, 0, 100, 1)
    );
    private final FormComboBox<Module> moduleField;
    private final FormComboBox<User> lecturerField;
    private final JDateChooser
        startDateField = new JDateChooser(),
        endDateField = new JDateChooser();

    public ClassEditFormContent(Session session, List<Module> modules, List<User> lecturers) {
        this(session, modules, lecturers, null);
    }

    public ClassEditFormContent(Session session, List<Module> modules, List<User> lecturers, ClassGroup classGroup) {
        super();

        moduleField = new FormComboBox<>(Module::name, modules);
        var lecturerModel = new DefaultComboBoxModel<User>();
        lecturerModel.addAll(lecturers);
        lecturerField = new FormComboBox<>(User::name, lecturerModel);

        var builder = new FlexibleGridBuilder(this, 4)
            .setInsets(new Insets(5, 5, 5, 5));

        Component moduleFieldComponent = moduleField,
            capacityFieldComponent = capacityField,
            lecturerFieldComponent = lecturerField,
            startDateFieldComponent = startDateField,
            endDateFieldComponent = endDateField;

        boolean canUpdate = session.can(Permission.UPDATE_CLASSES),
            canAssignLecturer = session.can(Permission.ASSIGN_TEACHER);

        if (classGroup != null) {
            builder.add(
                GRID_WIDTHS,
                COLUMN_WEIGHTS,
                new FormLabel("ID"),
                new FormTextField(classGroup.id().toString(), false)
            );

            if (!canUpdate) {
                moduleFieldComponent = new FormTextField(
                    classGroup.module().name(),
                    false
                );
                capacityFieldComponent = new FormTextField(
                    String.valueOf(classGroup.maxCapacity()),
                    false
                );
                startDateFieldComponent = new FormTextField(
                    LineFormatter.formatDate(classGroup.startDate(), "yyyy-MM-dd"),
                    false
                );
                endDateFieldComponent = new FormTextField(
                    LineFormatter.formatDate(classGroup.endDate(), "yyyy-MM-dd"),
                    false
                );
            }

            if (!canAssignLecturer) {
                var lecturerName = classGroup.lecturer() == null?
                    "(unassigned)":
                    classGroup.lecturer().name();
                lecturerFieldComponent = new FormTextField(lecturerName, false);
                
                lecturerModel.addElement(classGroup.lecturer());
            }

            capacityField.setValue(classGroup.maxCapacity());
            moduleField.setSelectedItem(classGroup.module());
            lecturerField.setSelectedItem(classGroup.lecturer());
            startDateField.setDate(classGroup.startDate());
            endDateField.setDate(classGroup.endDate());
        }

        builder.add(
            GRID_WIDTHS,
            COLUMN_WEIGHTS,
            new FormLabel("Module"),
            moduleFieldComponent,
            new FormLabel("Max capacity"),
            capacityFieldComponent
        );

        if (classGroup != null || canAssignLecturer) {
            builder.add(
                GRID_WIDTHS,
                COLUMN_WEIGHTS,
                new FormLabel("Lecturer"),
                lecturerFieldComponent
            );
        }

        builder.add(
            new int[] {1},
            COLUMN_WEIGHTS,
            new FormLabel("Start & End Date"),
            startDateFieldComponent,
            new FormLabel("-"),
            endDateFieldComponent
        )   
            .addStrutGlue(600)
            .build();

        setBorder(BorderFactory.createEmptyBorder(20, 15, 10, 15));
    }

    public ClassGroup getFormData() {
        return new ClassGroup(
            moduleField.getSelectedItem(),
            capacityField.getValue(),
            lecturerField.getSelectedItem(),
            startDateField.getDate(),
            endDateField.getDate()
        );
    }
}