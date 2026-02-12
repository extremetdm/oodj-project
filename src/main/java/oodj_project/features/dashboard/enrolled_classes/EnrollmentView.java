package oodj_project.features.dashboard.enrolled_classes;

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
import oodj_project.core.ui.components.management_view.DataList;
import oodj_project.core.ui.components.management_view.ManagementView;
import oodj_project.core.ui.styles.Icons;
import oodj_project.features.dashboard.module_management.ModuleGrid;
import oodj_project.features.dashboard.user_management.UserGrid;

public class EnrollmentView extends ManagementView<Enrollment> {

    private static final double[]
        COLUMN_WEIGHTS_WITH_ACTION = { 1, 6, 6, 4, 5, 2 },
        COLUMN_WEIGHTS_WITHOUT_ACTION = { 1, 7, 7, 4, 5 };

    private final EnrollmentFormFactory formFactory;

    private final boolean hasActions, canUnenroll;

    private final DataList<Enrollment> dataTable;

    public EnrollmentView(
        Session session,
        EnrollmentController controller
    ) {
        super(
            "My Classes",
            controller::index,
            EnrollmentView::buildSearchLogic
        );

        canUnenroll = session.can(Permission.UNENROLL_CLASSES);

        hasActions = canUnenroll;

        formFactory = new EnrollmentFormFactory(
            this,
            controller
        );

        var columnWeight = hasActions ? 
            COLUMN_WEIGHTS_WITH_ACTION : COLUMN_WEIGHTS_WITHOUT_ACTION;

        dataTable = new DataList<>(
            columnWeight,
            createTableHeader(),
            this::createTableRow
        );

        init();
    }

    private static Predicate<Enrollment> buildSearchLogic(String searchQuery) {
        return enrollment -> {
            var classGroup = enrollment.classGroup();
            var module = classGroup.module();
            var lecturer = classGroup.lecturer();

            return classGroup.id().toString().contains(searchQuery)
                || module.id().toString().contains(searchQuery)
                || module.name().toLowerCase().contains(searchQuery)
                || lecturer.id().toString().contains(searchQuery)
                || lecturer.name().toLowerCase().contains(searchQuery);
        };
    }

    @Override
    protected DataList<Enrollment> getContent() {
        return dataTable;
    }

    private Component[] createTableHeader() {
        var components = new ArrayList<>(List.<Component>of(
            DataList.createHeaderText("Class"),
            DataList.createHeaderText("Module"),
            DataList.createHeaderText("Lecturer"),
            DataList.createHeaderText("Status")
        ));

        if (hasActions) {
            var actionLabel = DataList.createHeaderText("Action");
            actionLabel.setHorizontalAlignment(SwingConstants.CENTER);
            components.add(actionLabel);
        }

        return components.toArray(Component[]::new);
    }

    private Component[] createTableRow(Enrollment enrollment) {
        var classGroup = enrollment.classGroup();
        var components = new ArrayList<>(List.<Component>of(
            DataList.createText(classGroup.id().toString()),
            new ModuleGrid(classGroup.module()),
            new UserGrid(classGroup.lecturer()),
            DataList.createText(enrollment.status().name())
        ));

        if (hasActions) {
            components.add(createActionMenu(enrollment));
        }

        return components.toArray(Component[]::new);
    }

    // private JPanel createPeriodSection(Date startDate, Date endDate) {
    //     var startDateLabel = DataList.createText("Start:");
    //     startDateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    //     var endDateLabel = DataList.createText("End:");
    //     endDateLabel.setHorizontalAlignment(SwingConstants.RIGHT);

    //     return new FlexibleGridBuilder(2)
    //         .setInsets(new Insets(0, 5, 0, 5))
    //         .add(
    //             new double[] { 0, 1 },
    //             startDateLabel,
    //             DataList.createText(LineFormatter.formatDate(startDate, "yyyy-MM-dd")),
    //             endDateLabel,
    //             DataList.createText(LineFormatter.formatDate(endDate, "yyyy-MM-dd")))
    //         .build();
    // }

    private JPanel createActionMenu(Enrollment Enrollment) {
        var actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
        actionPanel.setOpaque(false);

        ArrayList<Component> actionList = new ArrayList<>();

        if (canUnenroll)
            actionList.add(createUnenrollButton(Enrollment));

        actionPanel.add(Box.createHorizontalGlue());
        for (int x = 0; x < actionList.size(); x++) {
            if (x > 0)
                actionPanel.add(Box.createHorizontalStrut(5));
            actionPanel.add(actionList.get(x));
        }
        actionPanel.add(Box.createHorizontalGlue());

        return actionPanel;
    }


    private JButton createUnenrollButton(Enrollment enrollment) {
        var unenrollButton = new IconButton(Icons.DELETE);
        unenrollButton.setToolTipText("Unenroll Class");
        unenrollButton.addActionListener(event -> formFactory.getUnenrollForm(enrollment, this::refreshData));
        return unenrollButton;
    }

    @Override
    protected EnrollmentFormFactory getFormFactory() {
        return formFactory;
    }
}
