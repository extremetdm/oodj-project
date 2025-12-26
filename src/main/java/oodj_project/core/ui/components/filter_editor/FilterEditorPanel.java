package oodj_project.core.ui.components.filter_editor;

import java.util.List;

import oodj_project.core.ui.components.ListEditorPanel;

public class FilterEditorPanel<DataT> extends ListEditorPanel<FilterOptionPanel<DataT>, SelectedFilterOption<DataT, ?, ?>>{
    
    public FilterEditorPanel(List<FilterOption<DataT, ?, ?>> filterOptions, List<SelectedFilterOption<DataT, ?, ?>> selectedOptionList) {
        super(
            ListEditorPanel.<FilterOptionPanel<DataT>, SelectedFilterOption<DataT, ?, ?>>builder(
                (selectedOption, rowRemover) -> new FilterOptionPanel<>(
                    filterOptions,
                    rowRemover,
                    selectedOption
                ),
                FilterOptionPanel::getFilter,
                selectedOptionList
            )
                .addButtonText("Add Filter")
                .contentWidth(1000)
        );
    }

}
