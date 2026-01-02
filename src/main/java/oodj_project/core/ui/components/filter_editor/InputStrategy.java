package oodj_project.core.ui.components.filter_editor;

import java.awt.Component;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.swing.SpinnerNumberModel;

import oodj_project.core.ui.components.form.FormSpinner;
import oodj_project.core.ui.components.form.FormTextField;

public interface InputStrategy<ComponentT extends Component, FieldT> {
    public ComponentT createComponent();
    public FieldT getInput(ComponentT component);
    public void setInput(ComponentT component, FieldT field);

    public static <ComponentT extends Component, FieldT> 
    InputStrategy<ComponentT, FieldT> of(
        Supplier<ComponentT> componentCreator,
        Function<ComponentT, FieldT> fieldExtractor,
        BiConsumer<ComponentT, FieldT> fieldSetter
    ) {
        return new InputStrategy<>() {
            @Override
            public ComponentT createComponent() {                
                return componentCreator.get();
            }

            @Override
            public FieldT getInput(ComponentT component) {
                return fieldExtractor.apply(component);
            }

            @Override
            public void setInput(ComponentT component, FieldT field) {
                fieldSetter.accept(component, field);
            }
        };
    }

    public static InputStrategy<FormTextField, String> textField() {
        return InputStrategy.of(FormTextField::new, FormTextField::getText, FormTextField::setText);
    }

    public static InputStrategy<FormSpinner<Integer>, Integer> integerField(Integer initialValue, Integer minimum, Integer maximum, Integer step) {
        return InputStrategy.of(
            () -> new FormSpinner<>(new SpinnerNumberModel(initialValue, minimum, maximum, step)),
            FormSpinner::getValue,
            FormSpinner::setValue
        );
    }

    public static InputStrategy<FormSpinner<Integer>, Integer> idField() {
        return InputStrategy.integerField(1, 1, null, 1);
    }
}
