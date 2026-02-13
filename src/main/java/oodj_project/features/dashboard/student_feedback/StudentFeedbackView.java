package oodj_project.features.dashboard.student_feedback;

import java.awt.Component;
import java.awt.Insets;
import java.util.List;
import java.util.function.Predicate;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import oodj_project.core.ui.components.buttons.IconButton;
import oodj_project.core.ui.components.management_view.DataList;
import oodj_project.core.ui.components.management_view.ManagementView;
import oodj_project.core.ui.layout.FlexibleGridBuilder;
import oodj_project.core.ui.styles.Icons;
import oodj_project.features.dashboard.feedback_management.Feedback;
import oodj_project.features.dashboard.module_management.ModuleGrid;
import oodj_project.features.dashboard.user_management.UserGrid;

public class StudentFeedbackView extends ManagementView<StudentFeedback> {

    private static final double[] COLUMN_WEIGHT = { 1, 6, 6, 4, 6, 1 };

    private final StudentFeedbackFormFactory formFactory;
    private final DataList<StudentFeedback> dataTable;
    
    public StudentFeedbackView(
        StudentFeedbackController controller
    ) {
        super("My Feedbacks", controller::index, StudentFeedbackView::buildSearchLogic);

        dataTable = new DataList<>(
            COLUMN_WEIGHT,
            createTableHeader(),
            this::createTableRow,
            70
        );
        
        formFactory = new StudentFeedbackFormFactory(this, controller);

        init();
    }

    private static Predicate<StudentFeedback> buildSearchLogic(String searchQuery) {
        return studentFeedback -> {
            var enrollment = studentFeedback.enrollment();
            var classGroup = enrollment.classGroup();
            var module = classGroup.module();
            var lecturer = classGroup.lecturer();

            return classGroup.id().toString().contains(searchQuery) ||
                module.id().toString().contains(searchQuery) ||
                module.name().toLowerCase().contains(searchQuery) ||
                lecturer.id().toString().contains(searchQuery) || 
                lecturer.name().toLowerCase().contains(searchQuery);
        };
    }

    private Component[] createTableHeader() {
        return new Component[] {
            DataList.createHeaderText("Class"),
            DataList.createHeaderText("Module"),
            DataList.createHeaderText("Lecturer"),
            DataList.createHeaderText("Score"),
            DataList.createHeaderText("Comment"),
            DataList.createHeaderText("Action"),
        };
    }

    private Component[] createTableRow(StudentFeedback studentFeedback) {
        var enrollment = studentFeedback.enrollment();
        var classGroup = enrollment.classGroup();
        var feedback = studentFeedback.feedback();

        return new Component[] {
            DataList.createText(classGroup.id().toString()),
            new ModuleGrid(classGroup.module()),
            new UserGrid(classGroup.lecturer()),
            createScoreSection(feedback),
            createCommentSection(feedback),
            createActionMenu(studentFeedback)
        };
    }

    private Component createScoreSection(Feedback feedback) {
        if (feedback == null) {
            return DataList.createText("<html><i>(none)</i></html>");
        }

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

    private Component createCommentSection(Feedback feedback) {
        String text;
        if (feedback == null || feedback.comment() == null) {
            text = "<html><i>(none)</i></html>";
        } else {
            text = feedback.comment();
        }
            
        return DataList.createText(text);
    }

    private JPanel createActionMenu(StudentFeedback studentFeedback) {
        var actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
        actionPanel.setOpaque(false);

        List<Component> actionList = List.of(
            createEditButton(studentFeedback)
        );

        actionPanel.add(Box.createHorizontalGlue());
        for (int x = 0; x < actionList.size(); x++) {
            if (x > 0) actionPanel.add(Box.createHorizontalStrut(5));
            actionPanel.add(actionList.get(x));
        }
        actionPanel.add(Box.createHorizontalGlue());
        
        return actionPanel;
    }

    private IconButton createEditButton(StudentFeedback studentFeedback) {
        var editButton = new IconButton(Icons.EDIT);
        editButton.setToolTipText("Edit Feedback");
        editButton.addActionListener(event -> 
            formFactory.getEditForm(studentFeedback, this::refreshData)
        );
        return editButton;
    }

    @Override
    protected StudentFeedbackFormFactory getFormFactory() {
        return formFactory;
    }

    @Override
    protected DataList<StudentFeedback> getContent() {
        return dataTable;
    }
}
