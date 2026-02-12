package oodj_project.features.dashboard.student_assessment_result;

import java.awt.Component;
import java.awt.Insets;
import java.util.function.Predicate;

import javax.swing.JProgressBar;

import oodj_project.core.security.Session;
import oodj_project.core.ui.components.management_view.DataList;
import oodj_project.core.ui.components.management_view.ManagementView;
import oodj_project.core.ui.layout.FlexibleGridBuilder;
import oodj_project.features.dashboard.assessment_grading.AssessmentResult;
import oodj_project.features.dashboard.assessment_management.Assessment;
import oodj_project.features.dashboard.assessment_management.AssessmentGrid;
import oodj_project.features.dashboard.module_management.ModuleGrid;

public class StudentResultView extends ManagementView<StudentResult> {

    private static final double[] COLUMN_WEIGHT = { 1, 5, 5, 5, 2, 6 };

    private final StudentResultFormFactory formFactory;
    private final DataList<StudentResult> dataTable;
    
    public StudentResultView(Session session, StudentResultController controller) {
        super("My Assessment Results", controller::index, StudentResultView::buildSearchLogic);

        dataTable = new DataList<>(
            COLUMN_WEIGHT,
            createTableHeader(),
            this::createTableRow,
            70
        );
        formFactory = new StudentResultFormFactory(this);

        init();
    }

    private static Predicate<StudentResult> buildSearchLogic(String searchQuery) {
        return result -> {
            var assessment = result.gradeBook().assessment();
            var classGroup = assessment.classGroup();
            var module = classGroup.module();

            return classGroup.id().toString().contains(searchQuery)
                || module.id().toString().contains(searchQuery)
                || module.name().toLowerCase().contains(searchQuery)
                || assessment.name().toLowerCase().contains(searchQuery)
                || assessment.type().name().toLowerCase().contains(searchQuery);
        };
    }

    private Component[] createTableHeader() {
        return new Component[] {
            DataList.createHeaderText("Class"),
            DataList.createHeaderText("Module"),
            DataList.createHeaderText("Assessment"),
            DataList.createHeaderText("Marks"),
            DataList.createHeaderText("Grade"),
            DataList.createHeaderText("Feedback")
        };
    }

    private Component[] createTableRow(StudentResult studentResult) {
        var pendingText = "<html><i>(pending)</i></html>";

        var gradeBook = studentResult.gradeBook();
        var assessment = gradeBook.assessment();
        var classGroup = assessment.classGroup();
        var result = gradeBook.result();
        var grade = studentResult.grade();

        return new Component[] {
            DataList.createText(classGroup.id().toString()),
            new ModuleGrid(classGroup.module()),
            new AssessmentGrid(assessment),
            createMarksSection(assessment, result),
            DataList.createText(
                grade == null? pendingText:
                    grade.name() + " (" + grade.classification().name() + ")"
            ),
            DataList.createText(result == null? pendingText: result.feedback())
        };
    }

    private Component createMarksSection(Assessment assessment, AssessmentResult result) {
        if (result == null) {
            return DataList.createText("<html><i>(pending)</i></html>");
        }
        int marks = result.marks(),
            totalMarks = assessment.marks();

        var marksBar = new JProgressBar(0, totalMarks);
        marksBar.setValue(marks);
        marksBar.setStringPainted(true);

        var marksSection = new FlexibleGridBuilder(2)
            .setInsets(new Insets(0, 5, 0, 5))
            .add(
                new double[] { 1, 0 },
                marksBar,
                DataList.createText(marks + "/" + totalMarks)
            )
            .build();

        return marksSection;
    }

    @Override
    protected StudentResultFormFactory getFormFactory() {
        return formFactory;
    }

    @Override
    protected DataList<StudentResult> getContent() {
        return dataTable;
    }
}
