package oodj_project.features.dashboard.enrollment_management;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import oodj_project.core.security.Session;
import oodj_project.core.ui.components.form.Form;
import oodj_project.core.ui.components.management_view.FormFactory;
import oodj_project.features.dashboard.class_management.ClassDefinition;
import oodj_project.features.dashboard.class_management.ClassGroup;

public class EnrollmentFormFactory extends FormFactory<ClassGroup> {

    private final Session session;
    private final EnrollmentController controller;

    public EnrollmentFormFactory(
        Component component,
        Session session,
        EnrollmentController controller
    ) {
        super(
            component,
            ClassDefinition.SORT_OPTIONS,
            ClassDefinition.FILTER_OPTIONS
        );
        this.session = session;
        this.controller = controller;
    }

    @Override
    protected String getSortWindowTitle() {
        return "Sort Upcoming Classes";
    }

    @Override
    protected String getFilterWindowTitle() {
        return "Filter Upcoming Classes";
    }

    public Form getCreateForm(ClassGroup classGroup, Runnable onCreate) {
        var content = new EnrollmentFormContent(session.currentUser(), classGroup);
        
        var form = Form.builder(getParentWindow(), content, handleCreate(content, onCreate))
            .windowTitle("Enroll class")
            .formTitle("Enroll class")
            .confirmText("Enroll")
            .build();

        form.setVisible(true);

        return form;
    }

    private ActionListener handleCreate(EnrollmentFormContent content, Runnable onCreate) {
        return event -> {
            var window = SwingUtilities.getWindowAncestor((Component) event.getSource());
            try {
                controller.create(content.getFormData());
                if (onCreate != null)
                    onCreate.run();
                window.dispose();

            } catch (IllegalArgumentException | IOException e) {
                JOptionPane.showMessageDialog(window, e.getMessage(), "Error creating user", JOptionPane.ERROR_MESSAGE);
            }
        };
    }
}
