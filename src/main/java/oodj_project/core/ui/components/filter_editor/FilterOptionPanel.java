package oodj_project.core.ui.components.filter_editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import oodj_project.core.ui.components.buttons.IconButton;
import oodj_project.core.ui.components.form.FormTextField;
import oodj_project.core.ui.utils.IconManager;
import oodj_project.core.ui.utils.SelectorRenderer;

public class FilterOptionPanel<DataT> extends JPanel {

    private final JComboBox<FilterOption<DataT, ?, ?>> optionSelector;
    private final JComboBox<FilterOperator<?>> operationSelector;

    private static final ImageIcon DELETE_ICON = IconManager.getIcon("/icons/delete.png", 30, 30);

    private final IconButton deleteButton = new IconButton(DELETE_ICON);

    private Component criteriaField = createDummyCriteriaField();

    private final JPanel inputPanel = new JPanel(new GridLayout(1, 3, 10, 0));

    public FilterOptionPanel(List<FilterOption<DataT, ?, ?>> options, Consumer<FilterOptionPanel<DataT>> onRemove) {
        this(options, onRemove, null);
    }

    public FilterOptionPanel(List<FilterOption<DataT, ?, ?>> options, Consumer<FilterOptionPanel<DataT>> onRemove, SelectedFilterOption<DataT, ?, ?> selectedOption) {
        super(new BorderLayout());
        setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        DefaultComboBoxModel<FilterOption<DataT, ?, ?>> optionModel = new DefaultComboBoxModel<>();
        optionModel.addAll(options);
        
        optionSelector = new JComboBox<>(optionModel);
        optionSelector.setRenderer(new SelectorRenderer() {
            @Override
            public Component getListCellRendererComponent(
                JList<?> list,
                Object value, 
                int index,
                boolean isSelected,
                boolean cellHasFocus
            ) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof FilterOption) {
                    label.setText(((FilterOption<?, ?, ?>) value).label());
                }
                return label;
            }
        });
        
        DefaultComboBoxModel<FilterOperator<?>> operationModel = new DefaultComboBoxModel<>();
        operationSelector = new JComboBox<>(operationModel);
        operationSelector.setRenderer(new SelectorRenderer() {
            @Override
            public Component getListCellRendererComponent(
                JList<?> list,
                Object value, 
                int index,
                boolean isSelected,
                boolean cellHasFocus
            ) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof FilterOperator) {
                    label.setText(((FilterOperator<?>) value).label());
                }
                return label;
            }
        });

        add(inputPanel, BorderLayout.CENTER);

        deleteButton.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        deleteButton.setToolTipText("Delete");
        deleteButton.addActionListener(event -> {
            onRemove.accept(this);
        });
        add(deleteButton, BorderLayout.EAST);

        inputPanel.add(optionSelector);
        inputPanel.add(operationSelector);
        inputPanel.add(criteriaField);

        optionSelector.addActionListener(event -> {
            operationModel.removeAllElements();
            var option = getSelectedOption();
            if (option == null) {
                operationSelector.setEnabled(false);
                criteriaField.setEnabled(false);
            } else {
                operationModel.addAll(option.operations());
                operationSelector.setEnabled(true);
                operationSelector.setSelectedIndex(0);
            }
        });

        operationSelector.addActionListener(event -> {
            var option = getSelectedOption();
            var operation = getSelectedOperation(option);

            setCriteriaField(operation == null?
                createDummyCriteriaField():
                option.inputStrategy().createComponent()
            );
        });

        if (selectedOption != null) {
            optionSelector.setSelectedItem(selectedOption.option());
            operationSelector.setSelectedItem(selectedOption.operation());
            loadSelectedCriteria(selectedOption);
        } else {
            optionSelector.setSelectedIndex(0);
        }
    }

    public void enableDelete(boolean allowDelete) {
        deleteButton.setEnabled(allowDelete);
    }

    public SelectedFilterOption<DataT, ?, ?> getFilter() {

        var option = getSelectedOption();
        if (option == null) return null;

        var operator = getSelectedOperation(option);
        if (operator == null) return null; 
        
        return new SelectedFilterOption<>(
            option,
            operator,
            option.inputStrategy().getInput(criteriaField)
        );
    }

    @SuppressWarnings("unchecked")
    private <FieldT, ComponentT extends Component>
    FilterOption<DataT, FieldT, ComponentT> getSelectedOption() {
        return (FilterOption<DataT, FieldT, ComponentT>) optionSelector.getSelectedItem();
    }

    @SuppressWarnings("unchecked")
    private <FieldT>
    FilterOperator<FieldT> getSelectedOperation(FilterOption<DataT, FieldT, ?> selectedOption) {
        if (selectedOption == null) return null;
        return (FilterOperator<FieldT>) operationSelector.getSelectedItem();
    }

    private <FieldT, ComponentT extends Component> 
    void loadSelectedCriteria(SelectedFilterOption<DataT, FieldT, ComponentT> selectedOption) {
        if (selectedOption == null) return;

        var inputStrategy = selectedOption.option().inputStrategy();

        var component = inputStrategy.createComponent();
        inputStrategy.setInput(component, selectedOption.criteria());

        setCriteriaField(component);
    }

    private Component createDummyCriteriaField() {
        var field = new FormTextField();
        field.setEnabled(false);
        return field;
    }

    private void setCriteriaField(Component newCriteriaField) {
        inputPanel.remove(criteriaField);
        criteriaField = newCriteriaField;
        criteriaField.setPreferredSize(new Dimension(0, 0));
        inputPanel.add(criteriaField);
        inputPanel.revalidate();
    }
}
