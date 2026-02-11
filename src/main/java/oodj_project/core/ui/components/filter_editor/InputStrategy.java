package oodj_project.core.ui.components.filter_editor;

import java.awt.Component;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.swing.SpinnerNumberModel;

import oodj_project.core.ui.components.form.FormComboBox;
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
        return of(FormTextField::new, FormTextField::getText, FormTextField::setText);
    }

    public static <FieldT extends Number & Comparable<FieldT>>
    InputStrategy<FormSpinner<FieldT>, FieldT> numberField(
        FieldT initialValue, Comparable<FieldT> minimum, Comparable<FieldT> maximum, FieldT step
    ) {
        return of(
            () -> new FormSpinner<>(new SpinnerNumberModel(initialValue, minimum, maximum, step)),
            FormSpinner::getValue,
            FormSpinner::setValue
        );
    }

    public static InputStrategy<FormSpinner<Integer>, Integer> integerField(
        Integer initialValue, Integer minimum, Integer maximum, Integer step
    ) {
        return numberField(initialValue, minimum, maximum, step);
    }

    public static InputStrategy<FormSpinner<Integer>, Integer> positiveIntegerField() {
        return integerField(1, 1, null, 1);
    }

    public static InputStrategy<FormSpinner<Integer>, Integer> nonNegativeIntegerField() {
        return integerField(0, 0, null, 1);
    }

    public static InputStrategy<FormSpinner<Double>, Double> doubleField(
        Double initialValue, Double minimum, Double maximum, Double step
    ) {
        return numberField(initialValue, minimum, maximum, step);
    }

    public static InputStrategy<FormSpinner<Double>, Double> percentageField() {
        return of(
            () -> new FormSpinner<Double>(new SpinnerNumberModel(0, 0, 1, 0.001), "##0.0%"),
            FormSpinner::getValue,
            FormSpinner::setValue
        );
    }

    public static <FieldT> InputStrategy<FormComboBox<FieldT>, FieldT>
    selectField(Function<FieldT, String> fieldDescriptor, List<FieldT> options) {
        return of(
            () -> new FormComboBox<>(fieldDescriptor, options),
            FormComboBox::getSelectedItem,
            FormComboBox::setSelectedItem
        );
    }
}
