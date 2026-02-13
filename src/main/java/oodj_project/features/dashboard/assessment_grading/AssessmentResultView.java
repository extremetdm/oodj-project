package oodj_project.features.dashboard.assessment_grading;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import oodj_project.core.security.Permission;
import oodj_project.core.security.Session;
import oodj_project.core.ui.components.buttons.IconButton;
import oodj_project.core.ui.components.buttons.IconLabelButton;
import oodj_project.core.ui.components.management_view.DataList;
import oodj_project.core.ui.components.management_view.ManagementView;
import oodj_project.core.ui.styles.Icons;
import oodj_project.features.dashboard.assessment_management.AssessmentGrid;
import oodj_project.features.dashboard.module_management.ModuleGrid;
import oodj_project.features.dashboard.user_management.UserGrid;

public class AssessmentResultView extends ManagementView<GradeBook> {

    private static final double[]
        COLUMN_WEIGHT_WITH_ACTION = { 1, 5, 5, 5, 2, 5, 1 },
        COLUMN_WEIGHT_WITHOUT_ACTION = { 2, 5, 5, 5, 1, 6 };

    private final AssessmentResultFormFactory formFactory;

    private final boolean hasActions, canEdit;

    private final DataList<GradeBook> dataTable;

    public AssessmentResultView(
        Session session,
        AssessmentResultController controller
    ) {
        super(
            "Assessment Results",
            controller::index,
            AssessmentResultView::buildSearchLogic
        );

        canEdit = session.can(Permission.GRADE_ASSESSMENTS);
        var canAdd = session.can(Permission.GRADE_ASSESSMENTS);

        hasActions = canEdit;

        formFactory = new AssessmentResultFormFactory(
            this,
            controller
        );

        if (canAdd) {
            var addButton = new IconLabelButton("Add", Icons.ADD);
            addButton.addActionListener(event -> {
                formFactory.getCreateForm(this::refreshData);
            });
            toolbarComponents.add(addButton);
        }

        var columnWeight = hasActions?
            COLUMN_WEIGHT_WITH_ACTION:
            COLUMN_WEIGHT_WITHOUT_ACTION;

        dataTable = new DataList<>(
            columnWeight,
            createTableHeader(),
            this::createTableRow,
            90
        );

        init();
    }

    private static Predicate<GradeBook> buildSearchLogic(String searchQuery) {
        return gradeBook -> {
            var assessment = gradeBook.assessment();
            var student = gradeBook.enrollment().student();
            return student.id().toString().contains(searchQuery)
                || student.name().toLowerCase().contains(searchQuery)
                || assessment.id().toString().contains(searchQuery)
                || assessment.name().toLowerCase().contains(searchQuery)
                || assessment.type().name().toLowerCase().contains(searchQuery)
                || (gradeBook.result() != null && (
                    gradeBook.result().feedback().toLowerCase().contains(searchQuery)
                ));
        };
    }

    @Override
    protected DataList<GradeBook> getContent() {
        return dataTable;
    }

    private Component[] createTableHeader() {
        var components = new ArrayList<>(List.<Component>of(
            DataList.createHeaderText("Class"),
            DataList.createHeaderText("Module"),
            DataList.createHeaderText("Assessment"),
            DataList.createHeaderText("Student"),
            DataList.createHeaderText("Marks"),
            DataList.createHeaderText("Feedback")
        ));

        if (hasActions) {
            var actionLabel = DataList.createHeaderText("Action");
            actionLabel.setHorizontalAlignment(SwingConstants.CENTER);
            components.add(actionLabel);
        }

        return components.toArray(Component[]::new);
    }

    private Component[] createTableRow(GradeBook gradeBook) {
        var assessment = gradeBook.assessment();
        var student = gradeBook.enrollment().student();
        var result = gradeBook.result();

        String marks, feedback;
        if (result == null) {
            marks = "<html><i>(unmarked)</i></html>";
            feedback = "<html><i>(unmarked)</i></html>";
        } else {
            marks = result.marks() + "/" + assessment.marks();
            feedback = result.feedback();
        }

        var classGroup = assessment.classGroup();

        var components = new ArrayList<>(List.<Component>of(
            DataList.createText(classGroup.id().toString()),
            new ModuleGrid(classGroup.module()),
            new AssessmentGrid(assessment),
            new UserGrid(student),
            DataList.createText(marks),
            DataList.createText(feedback)
        ));

        if (hasActions) {
            components.add(createActionMenu(gradeBook));
        }

        return components.toArray(Component[]::new);
    }

    private JPanel createActionMenu(GradeBook gradeBook) {
        var actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
        actionPanel.setOpaque(false);

        ArrayList<Component> actionList = new ArrayList<>();
        
        if (canEdit) {    
            actionList.add(createEditButton(gradeBook));
        }

        actionPanel.add(Box.createHorizontalGlue());
        for (int x = 0; x < actionList.size(); x++) {
            if (x > 0) actionPanel.add(Box.createHorizontalStrut(5));
            actionPanel.add(actionList.get(x));
        }
        actionPanel.add(Box.createHorizontalGlue());
        
        return actionPanel;
    }

    private JButton createEditButton(GradeBook gradeBook) {
        var editButton = new IconButton(Icons.EDIT);
        editButton.setToolTipText("Edit Team gradeBook");
        editButton.addActionListener(event -> 
            formFactory.getEditForm(gradeBook, this::refreshData)
        );
        return editButton;
    }

    @Override
    protected AssessmentResultFormFactory getFormFactory() {
        return formFactory;
    }
}
