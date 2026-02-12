package oodj_project.core.ui.components.management_view;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;

import oodj_project.core.ui.components.filter_editor.FilterEditorPanel;
import oodj_project.core.ui.components.filter_editor.FilterOption;
import oodj_project.core.ui.components.filter_editor.SelectedFilterOption;
import oodj_project.core.ui.components.form.Form;
import oodj_project.core.ui.components.sort_editor.SelectedSortOption;
import oodj_project.core.ui.components.sort_editor.SortEditorPanel;
import oodj_project.core.ui.components.sort_editor.SortOption;

public abstract class FormFactory<DataT> {
    protected final Component component;

    private final List<FilterOption<DataT, ?, ?>> filterOptions;
    private final List<SortOption<DataT>> sortOptions;
    
    public FormFactory(
        Component component,
        List<SortOption<DataT>> sortOptions,
        List<FilterOption<DataT, ?, ?>> filterOptions
    ) {
        this.component = component;
        this.sortOptions = sortOptions;
        this.filterOptions = filterOptions;
    }

    protected Window getParentWindow() {
        return SwingUtilities.getWindowAncestor(component);
    }

    protected String getSortWindowTitle() {
        return "Sort Data";
    }

    public Form getSortForm(
        List<SelectedSortOption<DataT>> selectedSortOptions,
        Consumer<List<SelectedSortOption<DataT>>> onApply
    ) {
        var content = new SortEditorPanel<>(sortOptions, selectedSortOptions);        

        var form = Form.builder(
            getParentWindow(),
            content,
            applyChanges(() -> onApply.accept(content.getList()))
        )
            .windowTitle(getSortWindowTitle())
            .formTitle("Add Sort")
            .confirmText("Apply")
            .build();
            
        form.setVisible(true);

        return form;
    }

    protected String getFilterWindowTitle() {
        return "Filter Data";
    }

    public Form getFilterForm(
        List<SelectedFilterOption<DataT, ?, ?>> selectedFilterOptions,
        Consumer<List<SelectedFilterOption<DataT, ?, ?>>> onApply
    ) {
        var content = new FilterEditorPanel<>(filterOptions, selectedFilterOptions);

        var form = Form.builder(
            getParentWindow(),
            content, 
            applyChanges(() -> onApply.accept(content.getList()))
        )
            .windowTitle(getFilterWindowTitle())
            .formTitle("Add Filter")
            .confirmText("Apply")
            .build();
            
        form.setVisible(true);

        return form;
    }

    protected ActionListener applyChanges(Runnable onApply) {
        return event -> {
            if (onApply != null)
                onApply.run();
            SwingUtilities.getWindowAncestor((Component) event.getSource())
                .dispose();
        };
    }
}
