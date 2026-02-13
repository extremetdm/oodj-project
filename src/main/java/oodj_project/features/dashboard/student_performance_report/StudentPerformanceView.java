package oodj_project.features.dashboard.student_performance_report;

import java.awt.Component;
import java.awt.Insets;
import java.util.function.Predicate;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import oodj_project.core.security.Session;
import oodj_project.core.ui.components.management_view.DataList;
import oodj_project.core.ui.components.management_view.ManagementView;
import oodj_project.core.ui.layout.FlexibleGridBuilder;
import oodj_project.features.dashboard.module_management.ModuleGrid;
import oodj_project.features.dashboard.user_management.UserGrid;

public class StudentPerformanceView extends ManagementView<StudentPerformance> {

    private static final double[] COLUMN_WEIGHTS = { 6, 2, 6, 6, 4 };

    private final StudentPerformanceFormFactory formFactory;
    private final DataList<StudentPerformance> dataTable;
    
    public StudentPerformanceView(Session session, StudentPerformanceController controller) {
        super("Student Performance Report", controller::index, StudentPerformanceView::buildSearchLogic);

        dataTable = new DataList<>(
            COLUMN_WEIGHTS,
            createTableHeader(),
            this::createTableRow,
            70
        );
        formFactory = new StudentPerformanceFormFactory(this);

        init();
    }

    private static Predicate<StudentPerformance> buildSearchLogic(String searchQuery) {
        return report -> {
            var classGroup = report.enrollment().classGroup();
            var module = classGroup.module();
            // var lecturer = classGroup.lecturer();

            return classGroup.id().toString().contains(searchQuery) ||
                module.id().toString().contains(searchQuery) ||
                module.name().toLowerCase().contains(searchQuery);
                // lecturer.id().toString().contains(searchQuery) || 
                // lecturer.name().toLowerCase().contains(searchQuery);
        };
    }

    private Component[] createTableHeader() {
        return new Component[] {
            DataList.createHeaderText("Student"),
            DataList.createHeaderText("Class"),
            DataList.createHeaderText("Module"),
            DataList.createHeaderText("Total Marks"),
            DataList.createHeaderText("Grade")
        };
    }

    private Component[] createTableRow(StudentPerformance report) {

        var enrollment = report.enrollment();
        var classGroup = enrollment.classGroup();
        var grade = report.grade();

        return new Component[] {
            new UserGrid(enrollment.student()),
            DataList.createText(classGroup.id().toString()),
            new ModuleGrid(classGroup.module()),
            createMarksSection(report),
            DataList.createText(
                grade == null? "<html><i>(unknown)</i></html>":
                    grade.name() + " (" + grade.classification().name() + ")"
            )
        };
    }

    private JPanel createMarksSection(StudentPerformance report) {
        int marks = report.marks(),
            maxMarks = report.maxMarks();
            
        var marksBar = new JProgressBar(0, maxMarks);
        marksBar.setValue(marks);
        marksBar.setStringPainted(true);

        var marksSection = new FlexibleGridBuilder(2)
            .setInsets(new Insets(0, 5, 0, 5))
            .add(
                new double[] { 1, 0 },
                marksBar,
                DataList.createText(marks + "/" + maxMarks)
            )
            .build();
            
        // marksSection.setToolTipText("<html>" +
        //     "<b>Occupancy Details:</b><br>" +
        //     "Total occupants: " + totalOccupants + "<br>" +
        //     "Max capacity: " + maxCapacity + "<br>" +
        //     "Total vacancies: " + report.totalVacancies() + "<br>" +
        // "</html>");

        return marksSection;
    }

    @Override
    protected StudentPerformanceFormFactory getFormFactory() {
        return formFactory;
    }

    @Override
    protected DataList<StudentPerformance> getContent() {
        return dataTable;
    }
}
