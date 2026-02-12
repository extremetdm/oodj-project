package oodj_project.features.dashboard.module_management;

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
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

public class ModuleView extends ManagementView<Module> {

    private static final double[]
        COLUMN_WEIGHT_WITH_ACTION = { 1, 5, 16, 2 },
        COLUMN_WEIGHT_WITHOUT_ACTION = { 1, 7, 16 };

    private final ModuleController controller;

    private final ModuleFormFactory formFactory;

    private final boolean hasActions, canUpdate, canDelete;

    private final DataList<Module> dataTable;

    public ModuleView(Session session, ModuleController controller) {
        super(
            "Module Management",
            controller::index,
            ModuleView::buildSearchLogic
        );

        this.controller = controller;

        canUpdate = session.can(Permission.UPDATE_MODULES);
        canDelete = session.can(Permission.DELETE_MODULES);

        hasActions = canUpdate || canDelete;

        formFactory = new ModuleFormFactory(this, controller);

        if (session.can(Permission.CREATE_MODULES)) {
            var addButton = new IconLabelButton("Add", Icons.ADD);
            addButton.addActionListener(event -> {
                formFactory.getCreateForm(this::refreshData);
            });
            toolbarComponents.add(addButton);
        }

        var columnWeight = hasActions?
            COLUMN_WEIGHT_WITH_ACTION:
            COLUMN_WEIGHT_WITHOUT_ACTION;

        dataTable = new DataList<>(
            columnWeight,
            createTableHeader(),
            this::createTableRow
        );

        init();
    }

    private static Predicate<Module> buildSearchLogic(String searchQuery) {
        return module -> 
            module.id().toString().contains(searchQuery) ||
            module.name().toLowerCase().contains(searchQuery) ||
            module.description().toLowerCase().contains(searchQuery);
    }

    @Override
    protected DataList<Module> getContent() {
        return dataTable;
    }

    private Component[] createTableHeader() {
        var components = new ArrayList<>(List.<Component>of(
            DataList.createHeaderText("ID"),
            DataList.createHeaderText("Name"),
            DataList.createHeaderText("Description")
        ));

        if (hasActions) {
            var actionLabel = DataList.createHeaderText("Action");
            actionLabel.setHorizontalAlignment(SwingConstants.CENTER);
            components.add(actionLabel);
        }

        return components.toArray(Component[]::new);
    }

    private Component[] createTableRow(Module module) {
        boolean hasDescription = !module.description().isBlank();

        var description = hasDescription?
            module.description():
            "<html><i>(no description provided)<i><html>";

        var components = new ArrayList<>(List.<Component>of(
            DataList.createText(module.id().toString()),
            DataList.createText(module.name()),
            DataList.createText(description)
        ));

        if (hasActions) {
            components.add(createActionMenu(module));
        }

        return components.toArray(Component[]::new);
    }

    private JPanel createActionMenu(Module module) {
        var actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
        actionPanel.setOpaque(false);

        ArrayList<Component> actionList = new ArrayList<>();
        
        if (canUpdate) {    
            actionList.add(createEditButton(module));
        }

        if (canDelete) {
            actionList.add(createDeleteButton(module));
        }

        actionPanel.add(Box.createHorizontalGlue());
        for (int x = 0; x < actionList.size(); x++) {
            if (x > 0) actionPanel.add(Box.createHorizontalStrut(5));
            actionPanel.add(actionList.get(x));
        }
        actionPanel.add(Box.createHorizontalGlue());
        
        return actionPanel;
    }

    private JButton createEditButton(Module module) {
        var editButton = new IconButton(Icons.EDIT);
        editButton.setToolTipText("Edit Module");
        editButton.addActionListener(event -> 
            formFactory.getEditForm(module, this::refreshData)
        );
        return editButton;
    }

    private JButton createDeleteButton(Module module) {
        var deleteButton = new IconButton(Icons.DELETE);
        deleteButton.setToolTipText("Delete Module");
        deleteButton.addActionListener(event -> {
            var action = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete the module \"" + module.name() + "\"?",
                "Confirm delete module",
                JOptionPane.YES_NO_OPTION
            );

            if (action == JOptionPane.YES_OPTION) {
                try {
                    controller.delete(module);
                    refreshData();                
                } catch (IOException | NoSuchElementException e) {
                    String message = switch (e) {
                        case IOException _ -> "Failed to save changes.";
                        case NoSuchElementException _ -> "Failed to find module.";
                        default -> "Failed to delete module.";
                    };
                    JOptionPane.showMessageDialog(this, message, "Error deleting module", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return deleteButton;
    }

    @Override
    protected ModuleFormFactory getFormFactory() {
        return formFactory;
    }
}
