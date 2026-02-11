package oodj_project.features.dashboard.class_enrollment_report;

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

public class EnrollmentReportView extends ManagementView<EnrollmentReport> {

    private static final double[] COLUMN_WEIGHT = { 2, 5, 5, 6, 6 };

    private final EnrollmentReportFormFactory formFactory;
    private final DataList<EnrollmentReport> dataTable;
    
    public EnrollmentReportView(Session session, EnrollmentReportController controller) {
        super("Enrollment Report", controller::index, EnrollmentReportView::buildSearchLogic);

        dataTable = new DataList<>(
            COLUMN_WEIGHT,
            createTableHeader(),
            this::createTableRow,
            70
        );
        formFactory = new EnrollmentReportFormFactory(this);

        init();
    }

    private static Predicate<EnrollmentReport> buildSearchLogic(String searchQuery) {
        return report -> {
            var classGroup = report.classGroup();
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
            DataList.createHeaderText("Occupancy Rate"),
            DataList.createHeaderText("Dropout Rate")
        };
    }

    private Component[] createTableRow(EnrollmentReport report) {

        var classGroup = report.classGroup();

        return new Component[] {
            DataList.createText(classGroup.id().toString()),
            new ModuleGrid(classGroup.module()),
            new UserGrid(classGroup.lecturer()),
            createOccupancyRateSection(report),
            createDropoutRateSection(report)
        };
    }

    private JPanel createOccupancyRateSection(EnrollmentReport report) {
        int totalOccupants = report.totalOccupants(),
            maxCapacity = report.classGroup().maxCapacity();
            
        var occupancyRateBar = new JProgressBar(0, maxCapacity);
        occupancyRateBar.setValue(totalOccupants);
        occupancyRateBar.setStringPainted(true);

        var occupancyRateSection = new FlexibleGridBuilder(2)
            .setInsets(new Insets(0, 5, 0, 5))
            .add(
                new double[] { 1, 0 },
                occupancyRateBar,
                DataList.createText(totalOccupants + "/" + maxCapacity)
            )
            .build();
        occupancyRateSection.setToolTipText("<html>" +
            "<b>Occupancy Details:</b><br>" +
            "Total occupants: " + totalOccupants + "<br>" +
            "Max capacity: " + maxCapacity + "<br>" +
            "Total vacancies: " + report.totalVacancies() + "<br>" +
        "</html>");

        return occupancyRateSection;
    }

    private JPanel createDropoutRateSection(EnrollmentReport report) {
        int totalDropouts = report.totalDropouts(),
            totalEnrollments = report.enrollments().size();

        var dropoutRateBar = new JProgressBar(0, totalEnrollments == 0? 1: totalEnrollments);
        dropoutRateBar.setValue(totalDropouts);
        dropoutRateBar.setStringPainted(true);

        var dropoutRateSection = new FlexibleGridBuilder(2)
            .setInsets(new Insets(0, 5, 0, 5))
            .add(
                new double[] { 1, 0 },
                dropoutRateBar,
                DataList.createText(totalDropouts + "/" + totalEnrollments)
            )
            .build();
        dropoutRateSection.setToolTipText("<html>" +
            "<b>Enrollment Details:</b><br>" +
            "Total enrollments: " + totalEnrollments + "<br>" +
            "Total dropouts: " + totalDropouts + "<br>" +
        "</html>");

        return dropoutRateSection;
    }

    @Override
    protected EnrollmentReportFormFactory getFormFactory() {
        return formFactory;
    }

    @Override
    protected DataList<EnrollmentReport> getContent() {
        return dataTable;
    }
}
