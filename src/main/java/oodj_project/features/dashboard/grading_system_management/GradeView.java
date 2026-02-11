package oodj_project.features.dashboard.grading_system_management;

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import oodj_project.core.security.Permission;
import oodj_project.core.security.Session;
import oodj_project.core.ui.components.buttons.IconButton;
import oodj_project.core.ui.components.buttons.IconLabelButton;
import oodj_project.core.ui.components.management_view.DataList;
import oodj_project.core.ui.components.management_view.ManagementView;
import oodj_project.core.ui.styles.Icons;

public class GradeView extends ManagementView<Grade> {

    private static final double[]
        COLUMN_WEIGHTS_WITH_ACTION = { 7, 5, 5, 7, 2 },
        COLUMN_WEIGHTS_WITHOUT_ACTION = { 6, 6, 6, 6 };

    private final Session session;
    private final GradeController controller;

    private final DataList<Grade> dataTable;
    private final GradeFormFactory formFactory;

    private final boolean hasActions;
    
    public GradeView(
        Session session,
        GradeController controller
    ) {
        super(
            "Grading System Management",
            controller::index,
            GradeView::buildSearchLogic
        );

        this.session = session;
        this.controller = controller;

        hasActions = session.can(Permission.UPDATE_GRADES)
            || session.can(Permission.DELETE_GRADES);

        var columnWeight = hasActions?
            COLUMN_WEIGHTS_WITH_ACTION:
            COLUMN_WEIGHTS_WITHOUT_ACTION;

        dataTable = new DataList<>(
            columnWeight,
            createTableHeader(),
            this::createTableRow
        );

        formFactory = new GradeFormFactory(this, controller);
    
        if (session.can(Permission.CREATE_GRADES)) {
            var addButton = new IconLabelButton("Add", Icons.ADD);
            addButton.addActionListener(event -> {
                formFactory.getCreateForm(this::refreshData);
            });
            toolbarComponents.add(addButton);
        }    

        init();
    }
    
    private static Predicate<Grade> buildSearchLogic(String searchQuery) {
        return grade -> {
            return grade.name().toLowerCase().contains(searchQuery);
        };
    }

    private Component[] createTableHeader() {
        var components = new ArrayList<>(List.<Component>of(
            DataList.createHeaderText("Name"),
            DataList.createHeaderText("Min Mark"),
            DataList.createHeaderText("Max Mark"),
            DataList.createHeaderText("Classification")
        ));

        if (hasActions) {
            var actionLabel = DataList.createHeaderText("Action");
            actionLabel.setHorizontalAlignment(SwingConstants.CENTER);
            components.add(actionLabel);
        }
        
        return components.toArray(Component[]::new);
    }

    private Component[] createTableRow(Grade grade) {
        var components = new ArrayList<>(List.<Component>of(
            DataList.createText(grade.name()),
            DataList.createText(String.valueOf(grade.min())),
            DataList.createText(String.valueOf(grade.max())),
            DataList.createText(grade.classification().name())
        ));

        if (hasActions) {
            components.add(createActionMenu(grade));
        }

        return components.toArray(Component[]::new);
    }

    private JPanel createActionMenu(Grade grade) {
        var actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
        actionPanel.setOpaque(false);

        ArrayList<Component> actionList = new ArrayList<>();
        
        if (session.can(Permission.UPDATE_GRADES)) {    
            actionList.add(createEditOption(grade));
        }

        if (session.can(Permission.DELETE_GRADES)) {
            actionList.add(createDeleteOption(grade));
        }

        actionPanel.add(Box.createHorizontalGlue());
        for (int x = 0; x < actionList.size(); x++) {
            if (x > 0) actionPanel.add(Box.createHorizontalStrut(5));
            actionPanel.add(actionList.get(x));
        }
        actionPanel.add(Box.createHorizontalGlue());

        return actionPanel;
    }

    private IconButton createEditOption(Grade grade) {
        var editOption = new IconButton(Icons.EDIT);
        editOption.setToolTipText("Edit grade");
        editOption.addActionListener(event -> 
            formFactory.getEditForm(grade, this::refreshData)
        );
        return editOption;
    }

    private IconButton createDeleteOption(Grade grade) {
        var deleteOption = new IconButton(Icons.DELETE);
        deleteOption.setToolTipText("Delete grade");
        deleteOption.addActionListener(event -> {
            var action = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete grade \"" + grade.name() + "\"?",
                "Confirm delete grade",
                JOptionPane.YES_NO_OPTION
            );

            if (action == JOptionPane.YES_OPTION) {
                try {
                    controller.delete(grade);
                    refreshData();                
                } catch (IOException | NoSuchElementException e) {
                    String message = switch (e) {
                        case IOException _ -> "Failed to save changes.";
                        case NoSuchElementException _ -> "Failed to find grade.";
                        default -> "Failed to delete grade.";
                    };
                    JOptionPane.showMessageDialog(this, message, "Error deleting grade", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return deleteOption;
    }

    @Override
    protected DataList<Grade> getContent() {
        return dataTable;
    }
    @Override
    protected GradeFormFactory getFormFactory() {
        return formFactory;
    }
}
