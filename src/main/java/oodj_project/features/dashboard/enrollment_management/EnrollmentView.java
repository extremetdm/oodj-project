package oodj_project.features.dashboard.enrollment_management;

import java.awt.Component;
import java.awt.Insets;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import oodj_project.core.data.repository.LineFormatter;
import oodj_project.core.security.Session;
import oodj_project.core.ui.components.buttons.IconButton;
import oodj_project.core.ui.components.management_view.DataList;
import oodj_project.core.ui.components.management_view.ManagementView;
import oodj_project.core.ui.layout.FlexibleGridBuilder;
import oodj_project.core.ui.styles.Icons;
import oodj_project.features.dashboard.class_management.ClassGroup;
import oodj_project.features.dashboard.module_management.ModuleGrid;
import oodj_project.features.dashboard.user_management.UserGrid;

public class EnrollmentView extends ManagementView<ClassGroup> {

    private static final double[]
        COLUMN_WEIGHTS = { 1, 6, 6, 4, 5, 2 };

    private final EnrollmentFormFactory formFactory;

    private final DataList<ClassGroup> dataTable;

    public EnrollmentView(
        Session session,
        EnrollmentController controller
    ) {
        super(
            "Class Registration",
            controller::upcomingClasses,
            EnrollmentView::buildSearchLogic
        );

        formFactory = new EnrollmentFormFactory(
            this,
            session,
            controller
        );

        dataTable = new DataList<>(
            COLUMN_WEIGHTS,
            createTableHeader(),
            this::createTableRow
        );

        init();
    }

    private static Predicate<ClassGroup> buildSearchLogic(String searchQuery) {
        return classGroup -> {
            var module = classGroup.module();
            var lecturer = classGroup.lecturer();

            return classGroup.id().toString().contains(searchQuery)
                || module.id().toString().contains(searchQuery)
                || module.name().toLowerCase().contains(searchQuery)
                || (lecturer != null && (
                    lecturer.id().toString().contains(searchQuery)
                        || lecturer.name().toLowerCase().contains(searchQuery)
                ));
        };
    }

    @Override
    protected DataList<ClassGroup> getContent() {
        return dataTable;
    }

    private Component[] createTableHeader() {
        var actionLabel = DataList.createHeaderText("Action");
        actionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        return new Component[] {
            DataList.createHeaderText("ID"),
            DataList.createHeaderText("Module"),
            DataList.createHeaderText("Lecturer"),
            DataList.createHeaderText("Max capacity"),
            DataList.createHeaderText("Period"),
            actionLabel
        };
    }

    private Component[] createTableRow(ClassGroup classGroup) {

        return new Component[] {
            DataList.createText(classGroup.id().toString()),
            new ModuleGrid(classGroup.module()),
            new UserGrid(classGroup.lecturer()),
            DataList.createText(String.valueOf(classGroup.maxCapacity())),
            createPeriodSection(classGroup.startDate(), classGroup.endDate()),
            createActionMenu(classGroup)
        };
    }

    private JPanel createPeriodSection(Date startDate, Date endDate) {
        var startDateLabel = DataList.createText("Start:");
        startDateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        var endDateLabel = DataList.createText("End:");
        endDateLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        return new FlexibleGridBuilder(2)
            .setInsets(new Insets(0, 5, 0, 5))
            .add(
                new double[] { 0, 1 },
                startDateLabel,
                DataList.createText(LineFormatter.formatDate(startDate, "yyyy-MM-dd")),
                endDateLabel,
                DataList.createText(LineFormatter.formatDate(endDate, "yyyy-MM-dd"))
            )
            .build();
    }

    private JPanel createActionMenu(ClassGroup classGroup) {
        var actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
        actionPanel.setOpaque(false);

        List<Component> actionList = List.of(
            createEnrollButton(classGroup)
        );

        actionPanel.add(Box.createHorizontalGlue());
        for (int x = 0; x < actionList.size(); x++) {
            if (x > 0)
                actionPanel.add(Box.createHorizontalStrut(5));
            actionPanel.add(actionList.get(x));
        }
        actionPanel.add(Box.createHorizontalGlue());

        return actionPanel;
    }

    private JButton createEnrollButton(ClassGroup classGroup) {
        var editButton = new IconButton(Icons.ADD);
        editButton.setToolTipText("Enroll Class");
        editButton.addActionListener(event -> formFactory.getCreateForm(classGroup, this::refreshData));
        return editButton;
    }

    @Override
    protected EnrollmentFormFactory getFormFactory() {
        return formFactory;
    }
}
