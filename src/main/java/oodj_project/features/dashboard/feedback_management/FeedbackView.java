package oodj_project.features.dashboard.feedback_management;

import java.awt.Component;
import java.awt.Insets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import oodj_project.core.security.Permission;
import oodj_project.core.security.Session;
import oodj_project.core.ui.components.buttons.IconButton;
import oodj_project.core.ui.components.management_view.DataList;
import oodj_project.core.ui.components.management_view.ManagementView;
import oodj_project.core.ui.layout.FlexibleGridBuilder;
import oodj_project.core.ui.styles.Icons;
import oodj_project.features.dashboard.module_management.ModuleGrid;
import oodj_project.features.dashboard.user_management.UserGrid;

public class FeedbackView extends ManagementView<Feedback> {

    private static final double[]
        COLUMN_WEIGHT_WITH_ACTION = { 1, 5, 16, 2 },
        COLUMN_WEIGHT_WITHOUT_ACTION = { 1, 4, 4, 4, 11 };

    private final FeedbackController controller;

    private final FeedbackFormFactory formFactory;

    private final boolean hasActions, canUpdate, canDelete;

    private final DataList<Feedback> dataTable;

    public FeedbackView(Session session, FeedbackController controller) {
        super(
            "Feedback Management",
            controller::index,
            FeedbackView::buildSearchLogic
        );

        this.controller = controller;

        var canCreate = session.can(Permission.CREATE_FEEDBACKS);
        canUpdate = session.can(Permission.UPDATE_FEEDBACKS);
        canDelete = session.can(Permission.DELETE_FEEDBACKS);

        hasActions = canUpdate || canDelete;

        formFactory = new FeedbackFormFactory(this, controller);

        // if (canCreate) {
        //     var addButton = new IconLabelButton("Add", Icons.ADD);
        //     addButton.addActionListener(event -> {
        //         formFactory.getCreateForm(this::refreshData);
        //     });
        //     toolbarComponents.add(addButton);
        // }

        var columnWeight = hasActions?
            COLUMN_WEIGHT_WITH_ACTION:
            COLUMN_WEIGHT_WITHOUT_ACTION;

        dataTable = new DataList<>(
            columnWeight,
            createTableHeader(),
            this::createTableRow
        );

        init();
    }

    private static Predicate<Feedback> buildSearchLogic(String searchQuery) {
        return feedback -> {
            var enrollment = feedback.enrollment();
            var classGroup = enrollment.classGroup();
            var module = classGroup.module();
            var student = enrollment.student();

            return classGroup.id().toString().contains(searchQuery) ||
                module.id().toString().contains(searchQuery) ||
                module.name().toLowerCase().contains(searchQuery) ||
                student.id().toString().contains(searchQuery) || 
                student.name().toLowerCase().contains(searchQuery);
        };
    }

    @Override
    protected DataList<Feedback> getContent() {
        return dataTable;
    }

    private Component[] createTableHeader() {
        var components = new ArrayList<>(List.<Component>of(

            DataList.createHeaderText("Class"),
            DataList.createHeaderText("Module"),
            DataList.createHeaderText("Student"),
            DataList.createHeaderText("Score"),
            DataList.createHeaderText("Comment")
        ));

        // if (hasActions) {
        //     var actionLabel = DataList.createHeaderText("Action");
        //     actionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        //     components.add(actionLabel);
        // }

        return components.toArray(Component[]::new);
    }

    private Component[] createTableRow(Feedback feedback) {
        var enrollment = feedback.enrollment();
        var classGroup = enrollment.classGroup();

        var components = new ArrayList<>(List.<Component>of(
            DataList.createText(classGroup.id().toString()),
            new ModuleGrid(classGroup.module()),
            new UserGrid(enrollment.student()),
            createScoreSection(feedback),
            DataList.createText(feedback.comment())
        ));

        // if (hasActions) {
        //     components.add(createActionMenu(feedback));
        // }

        return components.toArray(Component[]::new);
    }

    private Component createScoreSection(Feedback feedback) {
        int score = feedback.score(),
            maxScore = 10;
            
        var scoreBar = new JProgressBar(0, maxScore);
        scoreBar.setValue(score);
        scoreBar.setStringPainted(true);

        var scoreSection = new FlexibleGridBuilder(2)
            .setInsets(new Insets(0, 5, 0, 5))
            .add(
                new double[] { 1, 0 },
                scoreBar,
                DataList.createText(score + "/" + maxScore)
            )
            .build();

        return scoreSection;
    }

    // private JPanel createActionMenu(Feedback feedback) {
    //     var actionPanel = new JPanel();
    //     actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
    //     actionPanel.setOpaque(false);

    //     ArrayList<Component> actionList = new ArrayList<>();
        
    //     if (canUpdate) {    
    //         actionList.add(createEditButton(feedback));
    //     }

    //     if (canDelete) {
    //         actionList.add(createDeleteButton(feedback));
    //     }

    //     actionPanel.add(Box.createHorizontalGlue());
    //     for (int x = 0; x < actionList.size(); x++) {
    //         if (x > 0) actionPanel.add(Box.createHorizontalStrut(5));
    //         actionPanel.add(actionList.get(x));
    //     }
    //     actionPanel.add(Box.createHorizontalGlue());
        
    //     return actionPanel;
    // }

    // private JButton createEditButton(Feedback feedback) {
    //     var editButton = new IconButton(Icons.EDIT);
    //     editButton.setToolTipText("Edit Feedback");
    //     editButton.addActionListener(event -> 
    //         formFactory.getEditForm(feedback, this::refreshData)
    //     );
    //     return editButton;
    // }

    // private JButton createDeleteButton(Feedback feedback) {
    //     var deleteButton = new IconButton(Icons.DELETE);
    //     deleteButton.setToolTipText("Delete Feedback");
    //     deleteButton.addActionListener(event -> {
    //         var action = JOptionPane.showConfirmDialog(
    //             this,
    //             "Are you sure you want to delete the feedback \"" + feedback.name() + "\"?",
    //             "Confirm delete feedback",
    //             JOptionPane.YES_NO_OPTION
    //         );

    //         if (action == JOptionPane.YES_OPTION) {
    //             try {
    //                 controller.delete(feedback);
    //                 refreshData();                
    //             } catch (IOException | NoSuchElementException e) {
    //                 String message = switch (e) {
    //                     case IOException _ -> "Failed to save changes.";
    //                     case NoSuchElementException _ -> "Failed to find feedback.";
    //                     default -> "Failed to delete feedback.";
    //                 };
    //                 JOptionPane.showMessageDialog(this, message, "Error deleting feedback", JOptionPane.ERROR_MESSAGE);
    //             }
    //         }
    //     });
    //     return deleteButton;
    // }

    @Override
    protected FeedbackFormFactory getFormFactory() {
        return formFactory;
    }
}
