package oodj_project.features.module_management;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.SwingUtilities;

import oodj_project.core.ui.components.Form;

public class ModuleFormFactory {

    private final Window parentWindow;
    private final ModuleController controller;

    public ModuleFormFactory(Window parentWindow, ModuleController controller) {
        this.parentWindow = parentWindow;
        this.controller = controller;
    }

    public Form getCreateForm(Runnable onCreate) {
        var content = new ModuleFormContent();

        var form = Form.builder(parentWindow, content, handleCreate(content, onCreate))
            .windowTitle("Create Module")
            .formTitle("Create Module")
            .confirmText("Create")
            .build();
        
        // form.setMinimumSize(new Dimension(600, 900));
        form.setVisible(true);
        
        return form;
    }

    private ActionListener handleCreate(ModuleFormContent content, Runnable onCreate) {
        return event -> {
            try {
                controller.create(content.getFormData());
                if (onCreate != null)
                    onCreate.run();
                SwingUtilities.getWindowAncestor((Component) event.getSource())
                    .dispose();

            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        };
    }

    public Form getEditForm(Module module, Runnable onUpdate) {
        var content = new ModuleFormContent(module);

        var form = Form.builder(parentWindow, content, handleEdit(module, content, onUpdate))
            .windowTitle("Edit Module")
            .formTitle("Edit Module")
            .confirmText("Update")
            .build();
            
        // form.setMinimumSize(new Dimension(600, 900));
        form.setVisible(true);

        return form;
    }

    private ActionListener handleEdit(Module module, ModuleFormContent content, Runnable onUpdate) {
        return event -> {
            try {
                controller.update(module.id(), content.getFormData());
                if (onUpdate != null)
                    onUpdate.run();
                SwingUtilities.getWindowAncestor((Component) event.getSource())
                    .dispose();

            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        };
    }
}