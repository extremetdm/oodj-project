package oodj_project.features.module_management;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import oodj_project.core.data.repository.Query;
import oodj_project.core.security.Permission;
import oodj_project.core.security.Session;
import oodj_project.core.ui.components.DataList;
import oodj_project.core.ui.components.IconButton;
import oodj_project.core.ui.components.IconLabelButton;
import oodj_project.core.ui.components.Paginator;
import oodj_project.core.ui.components.SearchBar;
import oodj_project.core.ui.utils.IconManager;

public class ModuleView extends JPanel {

    private static final double[] COLUMN_WEIGHT_WITH_ACITON = new double[] { 1, 4, 16, 3 };
    private static final double[] COLUMN_WEIGHT_WITHOUT_ACTION = new double[] { 2, 6, 16 };

    private final Session session;
    private final ModuleController controller;

    private ModuleFormFactory formFactory;

    private final SearchBar searchBar;

    private final DataList<Module> content;
    private final boolean hasActions;

    private final Paginator paginator;

    public ModuleView(Session session, ModuleController controller) {
        super(new BorderLayout());
        this.session = session;
        this.controller = controller;
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));

        hasActions = session.can(Permission.UPDATE_MODULES)
            || session.can(Permission.DELETE_MODULES);

        searchBar = new SearchBar(this::refreshData);
        // Header
        add(createHeader(), BorderLayout.NORTH);

        // content
        content = new DataList<>(
            getColumnWeight(),
            createTableHeader(),
            this::createTableRow
        );
        add(content, BorderLayout.CENTER);
    
        // footer
        paginator = new Paginator(this::refreshData);
        add(paginator, BorderLayout.SOUTH);

        refreshData();
    }

    public final void refreshData() {
        var query = Query.<Module>builder()
            .page(paginator.getCurrentPage())
            .limit(paginator.getPerPage());

        Predicate<Module> filter = module -> true;

        var searchQuery = searchBar.getQueryText().toLowerCase();
        if (!searchQuery.isEmpty()) {
            filter = filter.and(module -> 
                module.name().toLowerCase().contains(searchQuery) ||
                module.description().toLowerCase().contains(searchQuery)
            );
        }

        var result = controller.index(query.filterBy(filter).build());

        content.setData(result.data());

        paginator.update(result);
        // content.setData(controller.index(null));
    }

    private JPanel createHeader() {
        var header = new JPanel(new BorderLayout());
        var title = new JLabel("Module Management");
        title.setFont(new Font("Swis721 BlkCn BT", Font.BOLD, 50));
        title.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        header.add(title, BorderLayout.NORTH);

        var panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        var addButton = new IconLabelButton("Add", IconManager.getIcon("/icons/add.png", 30, 30));
        addButton.addActionListener(event -> {
            formFactory().getCreateForm(this::refreshData);
        });

        Component[] components = new Component[] {
            searchBar,
            new IconLabelButton("Filter", IconManager.getIcon("/icons/filter.png", 30, 30)),
            new IconLabelButton("Sort", IconManager.getIcon("/icons/sort.png", 30, 30)),
            addButton,
        };

        for (int index = 0; index < components.length; index++) {
            if (index > 0) panel.add(Box.createHorizontalStrut(10));
            panel.add(components[index]);
        } 

        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        header.add(panel, BorderLayout.SOUTH);

        return header;
    }

    private double[] getColumnWeight() {
        return hasActions? COLUMN_WEIGHT_WITH_ACITON: COLUMN_WEIGHT_WITHOUT_ACTION;
    }

    private Component[] createTableHeader() {
        var components = new ArrayList<>(List.<Component>of(
            new JLabel("ID"),
            new JLabel("Name"),
            new JLabel("Description")
        ));

        if (hasActions) {
            var actionLabel = new JLabel("Action");
            actionLabel.setHorizontalAlignment(SwingConstants.CENTER);
            components.add(actionLabel);
        }

        return components.toArray(Component[]::new);
    }

    private Component[] createTableRow(Module module) {
        var components = new ArrayList<>(List.<Component>of(
            new JLabel(module.id().toString()),
            new JLabel(module.name()),
            new JLabel(module.description())
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
    }

    private ModuleFormFactory formFactory() {
        if (formFactory == null)
            formFactory = new ModuleFormFactory(
                SwingUtilities.getWindowAncestor(this),
                controller
            );
        return formFactory;
    }

    private JButton createEditButton(Module module) {
        var editButton = new IconButton("/icons/edit.png", 30, 30);
        editButton.setToolTipText("Edit");
        editButton.addActionListener(event -> 
            formFactory().getEditForm(module, this::refreshData)
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
        var deleteButton = new IconButton("/icons/delete.png", 30, 30);
        deleteButton.setToolTipText("Delete");
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
}
