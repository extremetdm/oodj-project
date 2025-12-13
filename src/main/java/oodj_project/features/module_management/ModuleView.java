package oodj_project.features.module_management;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import oodj_project.core.data.repository.Query;
import oodj_project.core.security.Permission;
import oodj_project.core.security.Session;
import oodj_project.core.ui.components.DataList;
import oodj_project.core.ui.components.Paginator;

public class ModuleView extends JPanel {

    private static final double[] COLUMN_WEIGHT_WITH_ACITON = new double[] { 1, 4, 16, 3 };
    private static final double[] COLUMN_WEIGHT_WITHOUT_ACTION = new double[] { 2, 6, 16 };

    private final Session session;
    private final ModuleController controller;

    private ModuleFormFactory formFactory;

    private final DataList<Module> content;
    private final boolean hasActions;

    private final Paginator paginator;

    public ModuleView(Session session, ModuleController controller) {
        super(new BorderLayout());
        this.session = session;
        this.controller = controller;
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        hasActions = session.can(Permission.UPDATE_MODULES)
            || session.can(Permission.DELETE_MODULES);

        // Header
        var title = new JLabel("Module Management");
        title.setFont(new Font("Swis721 BlkCn BT", Font.BOLD, 50));
        title.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // content
        content = new DataList<>(
            getColumnWeight(),
            createTableHeader(),
            this::createTableRow
        );

        add(content, BorderLayout.CENTER);
    
        paginator = new Paginator(this::refreshData);
        add(paginator, BorderLayout.SOUTH);

        refreshData();
    }

    public final void refreshData() {
        var query = Query.<Module>builder()
            .page(paginator.getCurrentPage())
            .limit(paginator.getPerPage())
            .build();

        var result = controller.index(query);

        content.setData(result.data());
        paginator.update(result);
        // content.setData(controller.index(null));

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
            components.add(new JLabel("Action"));
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

            for (int x = 0; x < actionList.size(); x++) {
                if (x > 0) actionPanel.add(Box.createHorizontalStrut(5));
                actionPanel.add(actionList.get(x));
            }

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
        var editButton = new JButton(
            new ImageIcon(
                new ImageIcon("src/main/resources/icons/edit-4-svgrepo-com.png")
                    .getImage()
                    .getScaledInstance(30, 30, Image.SCALE_SMOOTH)
            )
        );
        editButton.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(3,3,3,3)
            )
        );
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
        var deleteButton = new JButton("Delete");
        deleteButton.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(3,3,3,3)
            )
        );
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
