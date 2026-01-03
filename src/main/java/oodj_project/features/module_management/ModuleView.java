package oodj_project.features.module_management;

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
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
import oodj_project.core.ui.utils.IconManager;

public class ModuleView extends ManagementView<Module> {

    private static final double[]
        COLUMN_WEIGHT_WITH_ACITON = new double[] { 1, 5, 16, 2 },
        COLUMN_WEIGHT_WITHOUT_ACTION = new double[] { 1, 7, 16 };

    private static final ImageIcon
        ADD_ICON = IconManager.getIcon("/icons/add.png", 30, 30),
        EDIT_ICON = IconManager.getIcon("/icons/edit.png", 30, 30),
        DELETE_ICON = IconManager.getIcon("/icons/delete.png", 30, 30);

    private final Session session;
    private final ModuleController controller;

    private final ModuleFormFactory formFactory;

    private final boolean hasActions;

    private final DataList<Module> dataTable;

    public ModuleView(Session session, ModuleController controller) {
        super(
            "Module Management",
            controller::index,
            ModuleView::buildSearchLogic
        );

        this.session = session;
        this.controller = controller;

        hasActions = session.can(Permission.UPDATE_MODULES)
            || session.can(Permission.DELETE_MODULES);

        formFactory = new ModuleFormFactory(this, controller);

        if (session.can(Permission.CREATE_MODULES)) {
            var addButton = new IconLabelButton("Add", ADD_ICON);
            addButton.addActionListener(event -> {
                formFactory.getCreateForm(this::refreshData);
            });
            toolbarComponents.add(addButton);
        }

        var columnWeight = hasActions?
            COLUMN_WEIGHT_WITH_ACITON:
            COLUMN_WEIGHT_WITHOUT_ACTION;

        dataTable = new DataList<>(
            columnWeight,
            createTableHeader(),
            getTableRowGenerator()
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

    private Function<Module, Component[]> getTableRowGenerator() {
        return module -> {
            boolean hasDescription = !module.description().isBlank();

            var description = hasDescription?
                module.description():
                "(no description provided)";

            var components = new ArrayList<>(List.<Component>of(
                DataList.createText(module.id().toString()),
                DataList.createText(module.name()),
                DataList.createText(description, !hasDescription)
            ));

            if (hasActions) {
                var actionPanel = new JPanel();
                actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
                actionPanel.setOpaque(false);

                ArrayList<Component> actionList = new ArrayList<>();
                
                if (session.can(Permission.UPDATE_MODULES)) {    
                    actionList.add(createEditButton(module));
                }

                if (session.can(Permission.DELETE_MODULES)) {
                    actionList.add(createDeleteButton(module));
                }

                actionPanel.add(Box.createHorizontalGlue());
                for (int x = 0; x < actionList.size(); x++) {
                    if (x > 0) actionPanel.add(Box.createHorizontalStrut(5));
                    actionPanel.add(actionList.get(x));
                }
                actionPanel.add(Box.createHorizontalGlue());

                components.add(actionPanel);
            }

            return components.toArray(Component[]::new);
        };
    }

    private JButton createEditButton(Module module) {
        var editButton = new IconButton(EDIT_ICON);
        editButton.setToolTipText("Edit Module");
        editButton.addActionListener(event -> 
            formFactory.getEditForm(module, this::refreshData)
        );
        return editButton;

        // var editOptions = new JPopupMenu();
        // editOptions.add(new JMenuItem("1"));
        // editOptions.add(new JMenuItem("2"));
        // editOptions.add(new JMenuItem("3"));

        // editButton.addActionListener(event -> {
        //     editOptions.show(editButton, editButton.getX(), editButton.getY());
        // });
    }

    private JButton createDeleteButton(Module module) {
        var deleteButton = new IconButton(DELETE_ICON);
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
