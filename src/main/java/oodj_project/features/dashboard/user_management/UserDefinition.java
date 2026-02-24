package oodj_project.features.dashboard.user_management;

import java.util.List;

import oodj_project.core.ui.components.filter_editor.FilterOption;
import oodj_project.core.ui.components.filter_editor.InputStrategy;
import oodj_project.core.ui.components.form.FormSpinner;
import oodj_project.core.ui.components.form.FormTextField;
import oodj_project.core.ui.components.sort_editor.SortOption;

public class UserDefinition {
    public static final SortOption<User>
        SORT_ID = SortOption.of("User ID", User::id),
        SORT_NAME = SortOption.of("Name", User::name);

    public static final List<SortOption<User>> SORT_OPTIONS = List.of(
        SORT_ID,
        SortOption.of("Username", User::username),
        SORT_NAME
    );

    public static final FilterOption<User, Integer, FormSpinner<Integer>>
        FILTER_ID = FilterOption.compare("User ID", User::id, InputStrategy.nonNegativeIntegerField());

    public static final FilterOption<User, String, FormTextField>
        FILTER_NAME = FilterOption.text("Name", User::name, InputStrategy.textField());


    // public static final List<FilterOption<User,?,?>> FILTER_OPTIONS = List.of(
    //     FilterOption.compare("User ID", User::id, InputStrategy.nonNegativeIntegerField()),
    //     FilterOption.sameAs("Role", User::role, InputStrategy.selectField(Role::name, roleController.index()), Role::name)
    // )    
}
