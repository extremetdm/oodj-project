package oodj_project.features.dashboard.module_management;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import oodj_project.core.ui.components.filter_editor.FilterOption;
import oodj_project.core.ui.components.filter_editor.InputStrategy;
import oodj_project.core.ui.components.form.Form;
import oodj_project.core.ui.components.management_view.FormFactory;
import oodj_project.core.ui.components.sort_editor.SortOption;

public class ModuleFormFactory extends FormFactory<Module> {

    private static final List<SortOption<Module>> SORT_OPTIONS = List.of(
        SortOption.of("ID", Module::id),
        SortOption.text("Name", Module::name),
        SortOption.text("Description", Module::description)
    );

    private static final List<FilterOption<Module, ?, ?>> FILTER_OPTIONS = List.of(
        FilterOption.compare("ID", Module::id, InputStrategy.positiveIntegerField()),
        FilterOption.text("Name", Module::name, InputStrategy.textField()),
        FilterOption.text("Description", Module::description, InputStrategy.textField())
    );

    private final ModuleController controller;

    public ModuleFormFactory(Component component, ModuleController controller) {
        super(component, SORT_OPTIONS, FILTER_OPTIONS);
        this.controller = controller;
    }

    @Override
    protected String getSortWindowTitle() {
        return "Sort Module";
    }

    @Override
    protected String getFilterWindowTitle() {
        return "Filter Module";
    }

    public Form getCreateForm(Runnable onCreate) {
        var content = new ModuleEditFormContent();

        var form = Form.builder(getParentWindow(), content, handleCreate(content, onCreate))
            .windowTitle("Create Module")
            .formTitle("Create Module")
            .confirmText("Create")
            .build();
        
        form.setVisible(true);
        
        return form;
    }

    private ActionListener handleCreate(ModuleEditFormContent content, Runnable onCreate) {
        return event -> {
            var window = SwingUtilities.getWindowAncestor((Component) event.getSource());
            try {
                controller.create(content.getFormData());
                if (onCreate != null)
                    onCreate.run();
                window.dispose();

            } catch (IllegalArgumentException|IOException e) {
                JOptionPane.showMessageDialog(window, e.getMessage(), "Error creating module", JOptionPane.ERROR_MESSAGE);
            }
        };
    }

    public Form getEditForm(Module module, Runnable onUpdate) {
        var content = new ModuleEditFormContent(module);

        var form = Form.builder(getParentWindow(), content, handleEdit(module, content, onUpdate))
            .windowTitle("Edit Module")
            .formTitle("Edit Module")
            .confirmText("Update")
            .build();
            
        form.setVisible(true);

        return form;
    }

    private ActionListener handleEdit(Module module, ModuleEditFormContent content, Runnable onUpdate) {
        return event -> {
            var window = SwingUtilities.getWindowAncestor((Component) event.getSource());
            try {
                controller.update(module.id(), content.getFormData());
                if (onUpdate != null)
                    onUpdate.run();
                window.dispose();

            } catch (IllegalArgumentException|IOException e) {
                JOptionPane.showMessageDialog(window, e.getMessage(), "Error editing module", JOptionPane.ERROR_MESSAGE);
            }
        };
    }
}