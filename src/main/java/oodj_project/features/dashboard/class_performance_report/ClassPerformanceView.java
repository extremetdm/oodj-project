package oodj_project.features.dashboard.class_performance_report;

import java.awt.Component;
import java.awt.Insets;
import java.util.function.Predicate;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import oodj_project.core.security.Session;
import oodj_project.core.ui.components.management_view.DataList;
import oodj_project.core.ui.components.management_view.ManagementView;
import oodj_project.core.ui.layout.FlexibleGridBuilder;
import oodj_project.features.dashboard.grading_system_management.Grade;
import oodj_project.features.dashboard.module_management.ModuleGrid;

public class ClassPerformanceView extends ManagementView<ClassPerformance> {

    private static final double[] COLUMN_WEIGHTS = { 1, 3, 5, 5, 5, 5 };

    private final ClassPerformanceFormFactory formFactory;
    private final DataList<ClassPerformance> dataTable;
    
    public ClassPerformanceView(Session session, ClassPerformanceController controller) {
        super("Class Performance Report", controller::index, ClassPerformanceView::buildSearchLogic);

        dataTable = new DataList<>(
            COLUMN_WEIGHTS,
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
        return new Component[] {
            DataList.createHeaderText("Class"),
            DataList.createHeaderText("Module"),
            DataList.createHeaderText("Min. Marks"),
            DataList.createHeaderText("Avg. Marks"),
            DataList.createHeaderText("Max. Marks"),
            DataList.createHeaderText("Pass Rate"),            
        };
    }

    private Component[] createTableRow(ClassPerformance report) {
        var classGroup = report.classGroup();
        var summary = report.summary();
        var maxMarks = report.maxMarks();

        return new Component[] {
            DataList.createText(classGroup.id().toString()),
            new ModuleGrid(classGroup.module()),
            createMarksSection(summary.getMin(), maxMarks, report.minGrade()),
            createMarksSection((int) summary.getAverage(), maxMarks, report.avgGrade()),
            createMarksSection(summary.getMax(), maxMarks, report.maxGrade()),
            createPassRateSection(report)
        };
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
