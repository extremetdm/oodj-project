package oodj_project.core.ui.components.sort_editor;

import java.util.List;

import oodj_project.core.ui.components.ListEditorPanel;

public class SortEditorPanel<DataT> extends ListEditorPanel<SortOptionPanel<DataT>, SelectedSortOption<DataT>> {

    public SortEditorPanel(List<SortOption<DataT>> sortOptions, List<SelectedSortOption<DataT>> selectedOptionList) {
        super(
            ListEditorPanel.<SortOptionPanel<DataT>, SelectedSortOption<DataT>>builder(
                (selectedOption, rowRemover) -> new SortOptionPanel<>(
                    sortOptions,
                    rowRemover,
                    selectedOption
                ),
                SortOptionPanel::getSelectedSortOption,
                selectedOptionList
            )
                .addButtonText("Add Sort")
                .contentWidth(600)
        );
    }
}
