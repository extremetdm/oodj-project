package oodj_project.features.dashboard.class_performance_report;

import java.awt.Component;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import oodj_project.core.security.Permission;
import oodj_project.core.security.Session;
import oodj_project.core.ui.components.management_view.DataList;
import oodj_project.core.ui.components.management_view.ManagementView;
import oodj_project.core.ui.layout.FlexibleGridBuilder;
import oodj_project.features.dashboard.grading_system_management.Grade;
import oodj_project.features.dashboard.module_management.ModuleGrid;
import oodj_project.features.dashboard.user_management.UserGrid;

public class ClassPerformanceView extends ManagementView<ClassPerformance> {

    private static final double[] COLUMN_WEIGHTS = { 1, 4, 5, 5, 5, 4 };
    private static final double[] COLUMN_WEIGHTS_WITH_LECTURER = { 1, 4, 4, 4, 4, 4, 3 };

    private final ClassPerformanceFormFactory formFactory;
    private final DataList<ClassPerformance> dataTable;

    private final boolean showLecturer;
    
    public ClassPerformanceView(Session session, ClassPerformanceController controller) {
        super("Class Performance Report", controller::index, ClassPerformanceView::buildSearchLogic);

        showLecturer = session.can(Permission.READ_ALL_CLASS_PERFORMANCE)
            || session.can(Permission.READ_TEAM_CLASS_PERFORMANCE);

        var columnWeights = showLecturer? COLUMN_WEIGHTS_WITH_LECTURER: COLUMN_WEIGHTS;

        dataTable = new DataList<>(
            columnWeights,
            createTableHeader(),
            this::createTableRow,
            70
        );
        formFactory = new ClassPerformanceFormFactory(this);

        init();
    }

    private static Predicate<ClassPerformance> buildSearchLogic(String searchQuery) {
        return report -> {
            var classGroup = report.classGroup();
            var module = classGroup.module();

            return classGroup.id().toString().contains(searchQuery) ||
                module.id().toString().contains(searchQuery) ||
                module.name().toLowerCase().contains(searchQuery);
        };
    }

    private Component[] createTableHeader() {
        var components = new ArrayList<>(List.<Component>of(
            DataList.createHeaderText("Class"),
            DataList.createHeaderText("Module")
        ));

        if (showLecturer) {
            components.add(DataList.createHeaderText("Lecturer"));
        }

        components.addAll(List.of(
            DataList.createHeaderText("Min. Marks"),
            DataList.createHeaderText("Avg. Marks"),
            DataList.createHeaderText("Max. Marks"),
            DataList.createHeaderText("Pass Rate")
        ));
        
        return components.toArray(Component[]::new);
    }

    private Component[] createTableRow(ClassPerformance report) {
        var classGroup = report.classGroup();
        var summary = report.summary();
        var maxMarks = report.maxMarks();

        var components = new ArrayList<>(List.<Component>of(
            DataList.createText(classGroup.id().toString()),
            new ModuleGrid(classGroup.module())
        ));

        if (showLecturer) {
            components.add(new UserGrid(classGroup.lecturer()));
        }

        components.addAll(List.of(
            createMarksSection(summary.getMin(), maxMarks, report.minGrade()),
            createMarksSection((int) summary.getAverage(), maxMarks, report.avgGrade()),
            createMarksSection(summary.getMax(), maxMarks, report.maxGrade()),
            createPassRateSection(report)
        ));

        return components.toArray(Component[]::new);
    }

    private JPanel createMarksSection(int marks, int maxMarks, Grade grade) {        
        var marksBar = new JProgressBar(0, maxMarks);
        marksBar.setValue(marks);
        marksBar.setStringPainted(true);

        var gradeText = grade == null? "":
            " (" + grade.name() + ")";

        var marksSection = new FlexibleGridBuilder(2)
            .setInsets(new Insets(0, 5, 0, 5))
            .add(
                new double[] { 1, 0 },
                marksBar,
                DataList.createText(marks + "/" + maxMarks + gradeText)
            )
            .build();
        
        return marksSection;
    }

    private JPanel createPassRateSection(ClassPerformance report) {        
        var passCount = report.passCount();
        var totalCount = report.totalStudents();

        var passRateBar = new JProgressBar(0, totalCount);
        passRateBar.setValue(passCount);
        passRateBar.setStringPainted(true);

        var passRateSection = new FlexibleGridBuilder(2)
            .setInsets(new Insets(0, 5, 0, 5))
            .add(
                new double[] { 1, 0 },
                passRateBar,
                DataList.createText(passCount + "/" + totalCount)
            )
            .build();
            
        // marksSection.setToolTipText("<html>" +
        //     "<b>Occupancy Details:</b><br>" +
        //     "Total occupants: " + totalOccupants + "<br>" +
        //     "Max capacity: " + maxCapacity + "<br>" +
        //     "Total vacancies: " + report.totalVacancies() + "<br>" +
        // "</html>");

        return passRateSection;
    }

    @Override
    protected ClassPerformanceFormFactory getFormFactory() {
        return formFactory;
    }

    @Override
    protected DataList<ClassPerformance> getContent() {
        return dataTable;
    }
}
