package oodj_project.core.ui.components.sort_editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import oodj_project.core.ui.components.buttons.IconButton;
import oodj_project.core.ui.utils.IconManager;
import oodj_project.core.ui.utils.SelectorRenderer;

public class SortOptionPanel<DataT> extends JPanel {

    private final JComboBox<SortOption<DataT>> optionSelector;

    private static final ImageIcon ASCENDING_ICON = IconManager.getIcon("/icons/sort-asc.png", 30, 30);
    private static final ImageIcon DESCENDING_ICON = IconManager.getIcon("/icons/sort-desc.png", 30, 30);

    private static final ImageIcon DELETE_ICON = IconManager.getIcon("/icons/delete.png", 30, 30);

    private final IconButton directionSelector = new IconButton(ASCENDING_ICON);;
    private final IconButton deleteButton = new IconButton(DELETE_ICON);

    private boolean descendingSelected = false;

    public SortOptionPanel(List<SortOption<DataT>> options, Consumer<SortOptionPanel<DataT>> onRemove) {
        this(options, onRemove, null);
    }

    public SortOptionPanel(List<SortOption<DataT>> options, Consumer<SortOptionPanel<DataT>> onRemove, SelectedSortOption<DataT> selectedOption) {
        super(new BorderLayout());
        setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        DefaultComboBoxModel<SortOption<DataT>> model = new DefaultComboBoxModel<>();
        model.addAll(options);

        optionSelector = new JComboBox<>(model);
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
                if (value instanceof SortOption) {
                    label.setText(((SortOption<?>) value).label());
                }
                return label;
            }
        });

        add(optionSelector, BorderLayout.CENTER);

        var buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        buttonPanel.add(Box.createHorizontalStrut(10));
        
        directionSelector.addActionListener(event -> {
            if (descendingSelected) setAscending();
            else setDescending();
        });
        buttonPanel.add(directionSelector);
        
        buttonPanel.add(Box.createHorizontalStrut(10));

        deleteButton.setToolTipText("Delete");
        deleteButton.addActionListener(event -> {
            onRemove.accept(this);
        });
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.EAST);

        if (selectedOption != null) {
            optionSelector.setSelectedItem(selectedOption.option());
            if (selectedOption.isDescending()) setDescending();
            else setAscending();
        } else {
            optionSelector.setSelectedIndex(0);
            setAscending();
        }
    }

    private void setAscending() {
        descendingSelected = false;
        directionSelector.setToolTipText("Ascending");
        directionSelector.setIcon(ASCENDING_ICON);
    }

    private void setDescending() {
        descendingSelected = true;
        directionSelector.setToolTipText("Descending");
        directionSelector.setIcon(DESCENDING_ICON);
    }

    public void enableDelete(boolean allowDelete) {
        deleteButton.setEnabled(allowDelete);
    }

    public SelectedSortOption<DataT> getSelectedSortOption() {
        int selectedIndex = optionSelector.getSelectedIndex();
        if (selectedIndex == -1) return null;
        
        return new SelectedSortOption<>(optionSelector.getItemAt(selectedIndex), descendingSelected);
    }
}
