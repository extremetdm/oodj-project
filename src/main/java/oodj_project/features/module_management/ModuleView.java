package oodj_project.features.module_management;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
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
import oodj_project.core.ui.components.Paginator;
import oodj_project.core.ui.components.SearchBar;
import oodj_project.core.ui.components.buttons.IconButton;
import oodj_project.core.ui.components.buttons.IconLabelButton;
import oodj_project.core.ui.components.sort_editor.SelectedSortOption;
import oodj_project.core.ui.utils.IconManager;

public class ModuleView extends JPanel {

    private static final double[]
        COLUMN_WEIGHT_WITH_ACITON = new double[] { 1, 5, 16, 2 },
        COLUMN_WEIGHT_WITHOUT_ACTION = new double[] { 1, 7, 16 };

    private static final ImageIcon
        FILTER_ICON = IconManager.getIcon("/icons/filter.png", 30, 30),
        SORT_ICON = IconManager.getIcon("/icons/sort.png", 30, 30),    
        ADD_ICON = IconManager.getIcon("/icons/add.png", 30, 30),
        EDIT_ICON = IconManager.getIcon("/icons/edit.png", 30, 30),
        DELETE_ICON = IconManager.getIcon("/icons/delete.png", 30, 30);

    private final Session session;
    private final ModuleController controller;

    private final SearchBar searchBar;
    private final DataList<Module> content;
    private final Paginator paginator;

    private final boolean hasActions;

    private List<SelectedSortOption<Module>> sortOptions = null;

    private ModuleFormFactory formFactory;

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

        var sorter = sortOptions == null? null:
            sortOptions.stream()
                .map(SelectedSortOption::comparator)
                .reduce((combined, next) -> combined.thenComparing(next))
                .orElse(null);

        Predicate<Module> filter = model -> true;

        var searchQuery = searchBar.getQueryText().toLowerCase();
        if (!searchQuery.isEmpty()) {
            filter = filter.and(module -> 
                module.id().toString().contains(searchQuery) ||
                module.name().toLowerCase().contains(searchQuery) ||
                module.description().toLowerCase().contains(searchQuery)
            );
        }

        var query = Query.<Module>builder()
            .page(paginator.getCurrentPage())
            .limit(paginator.getPerPage())
            .sortBy(sorter)
            .filterBy(filter)
            .build();

        var result = controller.index(query);

        content.setData(result.data());

        paginator.update(result);
    }

    private JPanel createHeader() {
        var header = new JPanel(new BorderLayout());
        var title = new JLabel("Module Management");
        title.setFont(new Font("Swis721 BlkCn BT", Font.BOLD, 50));
        title.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        header.add(title, BorderLayout.NORTH);

        var panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        var sortButton = new IconLabelButton("Sort", SORT_ICON);
        sortButton.addActionListener(event -> {
            formFactory().getSortForm(sortOptions, appliedSortOption -> {
                sortOptions = appliedSortOption;
                if (sortOptions.isEmpty()) {
                    sortButton.setBackground(null);
                    sortButton.setForeground(null);
                    sortButton.setText("Sort");
                    sortButton.setToolTipText(null);
                } else {
                    sortButton.setBackground(new Color(225, 240, 255));
                    sortButton.setForeground(new Color(50, 100, 200));
                    sortButton.setText("Sort (" + sortOptions.size() + ")");
                    String sortSummary = "<html><b>Active Sorts:</b><br>" + sortOptions.stream()
                        .map(sortOption -> String.format(
                            "- %s (%s)",
                            sortOption.option().label(),
                            sortOption.isDescending()? "Descending": "Ascending"
                        ))
                        .collect(Collectors.joining("<br>"))
                        + "</html>";
                    sortButton.setToolTipText(sortSummary);
                }
                refreshData();
            });
        });

        var components = new ArrayList<>(List.of(
            searchBar,
            new IconLabelButton("Filter", FILTER_ICON),
            sortButton
        ));

        if (session.can(Permission.CREATE_MODULES)) {
            var addButton = new IconLabelButton("Add", ADD_ICON);
            addButton.addActionListener(event -> {
                formFactory().getCreateForm(this::refreshData);
            });
            components.add(addButton);
        }

        for (int index = 0; index < components.size(); index++) {
            if (index > 0) panel.add(Box.createHorizontalStrut(10));
            panel.add(components.get(index));
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
        var components = new ArrayList<>(List.<Component>of(
            DataList.createText(module.id().toString()),
            DataList.createText(module.name()),
            DataList.createText(module.description())
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
        var editButton = new IconButton(EDIT_ICON);
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
        var deleteButton = new IconButton(DELETE_ICON);
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
