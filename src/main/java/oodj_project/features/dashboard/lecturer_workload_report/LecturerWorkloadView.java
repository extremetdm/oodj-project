package oodj_project.features.dashboard.lecturer_workload_report;

import java.awt.Component;
import java.util.function.Predicate;

import oodj_project.core.security.Session;
import oodj_project.core.ui.components.management_view.DataList;
import oodj_project.core.ui.components.management_view.ManagementView;
import oodj_project.features.dashboard.class_management.ClassGroup;
import oodj_project.features.dashboard.user_management.UserGrid;

public class LecturerWorkloadView extends ManagementView<LecturerWorkload> {

    private static final double[] COLUMN_WEIGHTS = { 6, 6, 6, 6 };
    
    private final Session session;

    private final DataList<LecturerWorkload> dataTable;
    private final LecturerWorkloadFormFactory formFactory;
    
    public LecturerWorkloadView(Session session, LecturerWorkloadController controller) {
        super(
            "Lecturer Workload Report",
            controller::index,
            LecturerWorkloadView::buildSearchLogic
        );

        this.session = session;

        dataTable = new DataList<>(
            COLUMN_WEIGHTS,
            createTableHeader(),
            this::createTableRow,
            70
        );
        formFactory = new LecturerWorkloadFormFactory(this);

        init();
    }
    
    private static Predicate<LecturerWorkload> buildSearchLogic(String searchQuery) {
        return workload -> {
            var lecturer = workload.lecturer();  
            return lecturer.id().toString().contains(searchQuery) ||
                lecturer.name().toLowerCase().contains(searchQuery);
        };
    }

    private Component[] createTableHeader() {
        return new Component[] {
            DataList.createHeaderText("Lecturer"),
            DataList.createHeaderText("Upcoming classes"),
            DataList.createHeaderText("Ongoing classes"),
            DataList.createHeaderText("Completed classes")
        };
    }

    private Component[] createTableRow(LecturerWorkload workload) {
        var lecturer = workload.lecturer();
        var classTotals = workload.classTotals();
        var studentTotals = workload.studentTotals();

        var upcoming = ClassGroup.Status.UPCOMING;
        var ongoing = ClassGroup.Status.ONGOING;
        var completed = ClassGroup.Status.COMPLETED;

        return new Component[] {
            new UserGrid(lecturer),
            createWorkloadSection(
                classTotals.getOrDefault(upcoming, 0l),
                studentTotals.getOrDefault(upcoming, 0l)
            ),
            createWorkloadSection(
                classTotals.getOrDefault(ongoing, 0l),
                studentTotals.getOrDefault(ongoing, 0l)
            ),
            createWorkloadSection(
                classTotals.getOrDefault(completed, 0l),
                studentTotals.getOrDefault(completed, 0l)
            ),
        };
    } 

    private Component createWorkloadSection(long totalClasses, long totalStudents) {
        return DataList.createText(totalClasses + " Classes, " + totalStudents + " Students");
    }

    @Override
    protected DataList<LecturerWorkload> getContent() {
        return dataTable;
    }
    
    @Override
    protected LecturerWorkloadFormFactory getFormFactory() {
        return formFactory;
    }
}
