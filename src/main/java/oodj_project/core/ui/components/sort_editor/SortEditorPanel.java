package oodj_project.core.ui.components.sort_editor;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import oodj_project.core.ui.components.buttons.IconLabelButton;
import oodj_project.core.ui.utils.GridBuilder;
import oodj_project.core.ui.utils.IconManager;

public class SortEditorPanel<DataT> extends JPanel {

    private final List<SortOptionPanel<DataT>> sortList;
    private final JPanel sortListPanel = new JPanel();
    private final JScrollPane scrollPane;

    public SortEditorPanel(List<SortOption<DataT>> sortOptions, List<SelectedSortOption<DataT>> selectedOptionList) {
        super(new BorderLayout());   
        setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        sortList = new ArrayList<>();
        
        scrollPane = new JScrollPane(sortListPanel);
        add(scrollPane, BorderLayout.CENTER);
        
        var buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        var deleteButton = new IconLabelButton("Clear All", IconManager.getIcon("/icons/delete.png", 30, 30));
        deleteButton.addActionListener(event -> {
            sortList.removeAll(sortList);
            createSortOption(sortOptions);
        });
        buttonPanel.add(deleteButton);
        
        buttonPanel.add(Box.createHorizontalGlue());

        var addButton = new IconLabelButton("Add Sort", IconManager.getIcon("/icons/add.png", 30, 30));
        addButton.addActionListener(event -> createSortOption(sortOptions));
        buttonPanel.add(addButton);

        add(buttonPanel, BorderLayout.SOUTH);

        if (selectedOptionList == null || selectedOptionList.isEmpty()) {
            createSortOption(sortOptions);
        } else {
            selectedOptionList.forEach(selectedOption -> createSortOption(sortOptions, selectedOption));
        }
    }

    private void updateSortList() {
        var builder = new GridBuilder(sortListPanel);
        sortList.forEach(builder::add);
        builder.setMinWidth(100)
            .addGlue()
            .build();
        sortListPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    private void createSortOption(List<SortOption<DataT>> sortOptions) {
        createSortOption(sortOptions, null);
    }

    private void createSortOption(List<SortOption<DataT>> sortOptions, SelectedSortOption<DataT> selectedOption) {
        sortList.add(new SortOptionPanel<>(
            sortOptions,
            panel -> {
                sortList.remove(panel);
                updateSortList();
            },
            selectedOption
        ));

        updateSortList();

        SwingUtilities.invokeLater(() -> {
            var scrollBar = scrollPane.getVerticalScrollBar();
            scrollBar.setValue(scrollBar.getMaximum());
        });
    }

    public List<SelectedSortOption<DataT>> getSelectedSortOptionList() {
        return sortList.stream()
            .map(SortOptionPanel::getSelectedSortOption)
            .toList();
    }
}
