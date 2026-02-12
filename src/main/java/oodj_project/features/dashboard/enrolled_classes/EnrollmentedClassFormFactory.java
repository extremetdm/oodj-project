package oodj_project.features.dashboard.enrolled_classes;

import java.awt.Component;
import java.io.IOException;
import java.util.NoSuchElementException;

import javax.swing.JOptionPane;

import oodj_project.core.ui.components.management_view.FormFactory;

public class EnrollmentedClassFormFactory extends FormFactory<Enrollment> {

    private final EnrolledClassController controller;

    public EnrollmentedClassFormFactory(
        Component component,
        EnrolledClassController controller
    ) {
        super(
            component,
            EnrollmentDefinition.SORT_OPTIONS,
            EnrollmentDefinition.FILTER_OPTIONS
        );
        this.controller = controller;
    }

    @Override
    protected String getSortWindowTitle() {
        return "Sort Enrolled Classes";
    }

    @Override
    protected String getFilterWindowTitle() {
        return "Filter Enrolled Classes";
    }

    public void getUnenrollForm(Enrollment enrollment, Runnable onUnenroll) {
        var action = JOptionPane.showConfirmDialog(
            component,
            "Are you sure you want to unenroll from class \"" + enrollment.classGroup().id() + "\"?",
            "Confirm unenroll class",
            JOptionPane.YES_NO_OPTION
        );

        if (action == JOptionPane.YES_OPTION) {
            try {
                controller.unenroll(enrollment);
                onUnenroll.run();
            } catch (IOException | NoSuchElementException e) {
                String message = switch (e) {
                    case IOException _ -> "Failed to save changes.";
                    case NoSuchElementException _ -> "Failed to find class.";
                    default -> "Failed to unenroll class.";
                };
                JOptionPane.showMessageDialog(
                    component, 
                    message,
                    "Error unenrolling class",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
