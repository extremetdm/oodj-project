package oodj_project.features.module_management;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;

import oodj_project.core.ui.components.Form;
import oodj_project.core.ui.components.SortFormContent;
import oodj_project.core.ui.utils.SelectedSortOption;
import oodj_project.core.ui.utils.SortOption;

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

    private static final List<SortOption<Module>> SORT_OPTIONS = List.of(
        new SortOption<>("ID", Module::id),
        new SortOption<>("Name", Module::name),
        new SortOption<>("Description", Module::description)
    );

    public Form getSortForm(List<SelectedSortOption<Module>> selectedSortOptions, Consumer<List<SelectedSortOption<Module>>> onApply) {

        var content = new SortFormContent<>(SORT_OPTIONS, selectedSortOptions);        

        var form = Form.builder(parentWindow, content, applySort(content, onApply))
            .windowTitle("Sort Module")
            .formTitle("Add Sort")
            .confirmText("Apply")
            .build();
            
        form.setMinimumSize(new Dimension(800, 450));
        form.setVisible(true);

        return form;
    }

    private ActionListener applySort(SortFormContent<Module> formContent, Consumer<List<SelectedSortOption<Module>>> onApply) {
        return event -> {        
            if (onApply != null)
                onApply.accept(formContent.getSelectedSortOptionList());
            SwingUtilities.getWindowAncestor((Component) event.getSource())
                .dispose();
        };
    }
}