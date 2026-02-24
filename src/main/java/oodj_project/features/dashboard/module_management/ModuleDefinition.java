package oodj_project.features.dashboard.module_management;

import java.util.List;

import oodj_project.core.ui.components.filter_editor.FilterOption;
import oodj_project.core.ui.components.filter_editor.InputStrategy;
import oodj_project.core.ui.components.form.FormSpinner;
import oodj_project.core.ui.components.form.FormTextField;
import oodj_project.core.ui.components.sort_editor.SortOption;

public class ModuleDefinition {
    
    public static final SortOption<Module>
        SORT_ID = SortOption.of("ID", Module::id),
        SORT_NAME = SortOption.text("Name", Module::name),
        SORT_DESCRIPTION = SortOption.text("Description", Module::description);

    public static final List<SortOption<Module>> SORT_OPTIONS = List.of(
        SORT_ID,
        SORT_NAME,
        SORT_DESCRIPTION
    );

    public static final FilterOption<Module, Integer, FormSpinner<Integer>>
        FILTER_ID = FilterOption.compare("ID", Module::id, InputStrategy.positiveIntegerField());

    public static final FilterOption<Module, String, FormTextField>
        FILTER_NAME = FilterOption.text("Name", Module::name, InputStrategy.textField()),
        FILTER_DESCRIPTION = FilterOption.text("Description", Module::description, InputStrategy.textField());

    public static final List<FilterOption<Module, ?, ?>> FILTER_OPTIONS = List.of(
        FILTER_ID,
        FILTER_NAME,
        FILTER_DESCRIPTION
    );
}
