package oodj_project.features.dashboard.enrolled_classes;

import java.awt.Color;
import java.awt.Component;
import java.util.List;
import java.util.function.Predicate;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

import oodj_project.core.security.Permission;
import oodj_project.core.security.Session;
import oodj_project.core.ui.components.buttons.IconButton;
import oodj_project.core.ui.components.filter_editor.FilterOperator;
import oodj_project.core.ui.components.filter_editor.SelectedFilterOption;
import oodj_project.core.ui.components.management_view.DataList;
import oodj_project.core.ui.components.management_view.ManagementView;
import oodj_project.core.ui.styles.Icons;
import oodj_project.features.dashboard.class_management.ClassGroup;
import oodj_project.features.dashboard.module_management.ModuleGrid;
import oodj_project.features.dashboard.student_assessment_result.StudentResultDefinition;
import oodj_project.features.dashboard.user_management.UserGrid;
import oodj_project.features.navigation.NavigationItem;
import oodj_project.features.navigation.Navigator;

public class EnrolledClassView extends ManagementView<Enrollment> {

    private static final double[] COLUMN_WEIGHTS = { 1, 6, 6, 4, 5, 2 };

    private final EnrollmentedClassFormFactory formFactory;

    private final boolean canUnenroll;

    private final DataList<Enrollment> dataTable;

    private final Navigator navigator;

    public EnrolledClassView(
        Session session,
        Navigator navigator,
        EnrolledClassController controller
    ) {
        super(
            "My Classes",
            controller::index,
            EnrolledClassView::buildSearchLogic
        );

        this.navigator = navigator;

        canUnenroll = session.can(Permission.UNENROLL_CLASSES);

        formFactory = new EnrollmentedClassFormFactory(
            this,
            controller
        );

        dataTable = new DataList<>(
            COLUMN_WEIGHTS,
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
        
        var actionLabel = DataList.createHeaderText("Action");
        actionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        return new Component[] {
            DataList.createHeaderText("Class"),
            DataList.createHeaderText("Module"),
            DataList.createHeaderText("Lecturer"),
            DataList.createHeaderText("Status"),
            actionLabel
        };
    }

    private Component[] createTableRow(Enrollment enrollment) {
        var classGroup = enrollment.classGroup();

        return new Component[] {
            DataList.createText(classGroup.id().toString()),
            new ModuleGrid(classGroup.module()),
            new UserGrid(classGroup.lecturer()),
            DataList.createText(enrollment.status().name()),
            createActionMenu(enrollment)
        };
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

    private IconButton createActionMenu(Enrollment enrollment) {
        var actionMenu = new JPopupMenu();
        actionMenu.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        actionMenu.add(createViewResultOption(enrollment.classGroup()));

        if (canUnenroll) {
            switch (enrollment.status()) {
                case UPCOMING, ONGOING -> 
                    actionMenu.add(createUnenrollOption(enrollment));
                default -> {}
            }
        }

        var menuButton = new IconButton(Icons.EDIT);
        menuButton.setToolTipText("Show actions");
        menuButton.addActionListener(event -> {
            actionMenu.show(menuButton, menuButton.getWidth() / 2, menuButton.getHeight() / 2);
        });

        return menuButton;
    }

    private JMenuItem createViewResultOption(ClassGroup classGroup) {
        var viewResultOption = new JMenuItem("View Assessment Results");
        viewResultOption.addActionListener(event -> navigator.goTo(
            NavigationItem.STUDENT_ASSESSMENT_RESULTS,
            new ManagementView.State<>(null, List.of(
                new SelectedFilterOption<>(
                    StudentResultDefinition.FILTER_CLASS_ID,
                    FilterOperator.getCompareEqual(),
                    classGroup.id()                    
                )
            ))
        ));
        return viewResultOption;
    }

    private JMenuItem createUnenrollOption(Enrollment enrollment) {
        var unenrollOption = new JMenuItem("Unenroll Class");
        unenrollOption.addActionListener(event -> formFactory.getUnenrollForm(enrollment, this::refreshData));
        return unenrollOption;
    }    

    @Override
    protected EnrollmentedClassFormFactory getFormFactory() {
        return formFactory;
    }
}
