package oodj_project.features.module_management;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import oodj_project.core.ui.components.form.Form;
import oodj_project.core.ui.components.sort_editor.SelectedSortOption;
import oodj_project.core.ui.components.sort_editor.SortEditorPanel;
import oodj_project.core.ui.components.sort_editor.SortOption;

public class ModuleFormFactory {

    private final Window parentWindow;
    private final ModuleController controller;

    public ModuleFormFactory(Window parentWindow, ModuleController controller) {
        this.parentWindow = parentWindow;
        this.controller = controller;
    }

    public Form getCreateForm(Runnable onCreate) {
        var content = new ModuleEditFormContent();

        var form = Form.builder(parentWindow, content, handleCreate(content, onCreate))
            .windowTitle("Create Module")
            .formTitle("Create Module")
            .confirmText("Create")
            .build();
        
        // form.setMinimumSize(new Dimension(600, 900));
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

        var form = Form.builder(parentWindow, content, handleEdit(module, content, onUpdate))
            .windowTitle("Edit Module")
            .formTitle("Edit Module")
            .confirmText("Update")
            .build();
            
        // form.setMinimumSize(new Dimension(600, 900));
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

    private static final List<SortOption<Module>> SORT_OPTIONS = List.of(
        SortOption.of("ID", Module::id),
        SortOption.text("Name", Module::name),
        SortOption.text("Description", Module::description)
    );

    public Form getSortForm(List<SelectedSortOption<Module>> selectedSortOptions, Consumer<List<SelectedSortOption<Module>>> onApply) {

        var content = new SortEditorPanel<>(SORT_OPTIONS, selectedSortOptions);        

        var form = Form.builder(parentWindow, content, applySort(content, onApply))
            .windowTitle("Sort Module")
            .formTitle("Add Sort")
            .confirmText("Apply")
            .build();
            
        form.setMinimumSize(new Dimension(800, 450));
        form.setVisible(true);

        return form;
    }

    private ActionListener applySort(SortEditorPanel<Module> formContent, Consumer<List<SelectedSortOption<Module>>> onApply) {
        return event -> {        
            if (onApply != null)
                onApply.accept(formContent.getSelectedSortOptionList());
            SwingUtilities.getWindowAncestor((Component) event.getSource())
                .dispose();
        };
    }
}