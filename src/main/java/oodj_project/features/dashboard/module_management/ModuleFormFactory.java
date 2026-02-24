package oodj_project.features.dashboard.module_management;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import oodj_project.core.ui.components.form.Form;
import oodj_project.core.ui.components.management_view.FormFactory;

public class ModuleFormFactory extends FormFactory<Module> {    

    private final ModuleController controller;

    public ModuleFormFactory(Component component, ModuleController controller) {
        super(component, ModuleDefinition.SORT_OPTIONS, ModuleDefinition.FILTER_OPTIONS);
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

            } catch (IllegalStateException|IllegalArgumentException|IOException e) {
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

            } catch (IllegalStateException|IllegalArgumentException|IOException e) {
                JOptionPane.showMessageDialog(window, e.getMessage(), "Error editing module", JOptionPane.ERROR_MESSAGE);
            }
        };
    }
}