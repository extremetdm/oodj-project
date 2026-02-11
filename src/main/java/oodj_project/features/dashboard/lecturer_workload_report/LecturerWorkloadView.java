package oodj_project.features.dashboard.lecturer_workload_report;

import java.awt.Component;
import java.util.function.Predicate;

import oodj_project.core.security.Session;
import oodj_project.core.ui.components.management_view.DataList;
import oodj_project.core.ui.components.management_view.ManagementView;

public class LecturerWorkloadView extends ManagementView<LecturerWorkload> {

    private static final double[] COLUMN_WEIGHTS = { 6, 6, 6, 6 };
    
    private final Session session;
    private final LecturerWorkloadController controller;

    private final DataList<LecturerWorkload> dataTable;
    private final LecturerWorkloadFormFactory formFactory;
    
    public LecturerWorkloadView(Session session, LecturerWorkloadController controller) {
        super(
            "Lecturer Workload Report",
            controller::index,
            LecturerWorkloadView::buildSearchLogic
        );

        this.session = session;
        this.controller = controller;

        dataTable = new DataList<>(
            COLUMN_WEIGHTS,
            createTableHeader(),
            this::createTableRow
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
        return new Component[] {
            DataList.createText(lecturer.id().toString()),
            DataList.createText(String.valueOf(workload.classes().size())),
            DataList.createText("String.valueOf(workload.classes().size())"),
            DataList.createText("String.valueOf(workload.classes().size())")
        };
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
