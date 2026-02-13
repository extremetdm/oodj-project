package oodj_project.features.dashboard.feedback_management;

import java.awt.Component;

import oodj_project.core.ui.components.management_view.FormFactory;

public class FeedbackFormFactory extends FormFactory<Feedback> {

    private final FeedbackController controller;

    public FeedbackFormFactory(
        Component component,
        FeedbackController controller
    ) {
        super(
            component,
            FeedbackDefinition.SORT_OPTIONS,
            FeedbackDefinition.FILTER_OPTIONS
        );
        this.controller = controller;
    }

    @Override
    protected String getSortWindowTitle() {
        return "Sort Feedback";
    }

    @Override
    protected String getFilterWindowTitle() {
        return "Filter Feedback";
    }

    // public Form getEditForm(Feedback Feedback, Runnable onUpdate) {
    //     var content = new FeedbackFormContent(Feedback);

    //     var form = Form.builder(
    //         getParentWindow(), content, handleEdit(Feedback, content, onUpdate)
    //     )
    //         .windowTitle("Edit Feedback")
    //         .formTitle("Edit Feedback")
    //         .confirmText("Update")
    //         .build();
            
    //     form.setVisible(true);

    //     return form;
    // }

    // private ActionListener handleEdit(
    //     Feedback Feedback,
    //     FeedbackFormContent content,
    //     Runnable onUpdate
    // ) {
    //     return event -> {
    //         var window = SwingUtilities.getWindowAncestor((Component) event.getSource());
    //         try {
    //             var feedback = Feedback.feedback();
    //             if (feedback == null) {
    //                 controller.create(content.getFormData());
    //             } else {
    //                 controller.update(feedback.id(), content.getFormData());
    //             }
    //             if (onUpdate != null)
    //                 onUpdate.run();
    //             window.dispose();

    //         } catch (IllegalStateException|IllegalArgumentException|IOException e) {
    //             JOptionPane.showMessageDialog(window, e.getMessage(), "Error editing feedback", JOptionPane.ERROR_MESSAGE);
    //         }
    //     };
    // }
}
