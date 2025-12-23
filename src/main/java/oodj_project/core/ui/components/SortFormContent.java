package oodj_project.core.ui.components;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import oodj_project.core.ui.utils.GridBuilder;
import oodj_project.core.ui.utils.IconManager;
import oodj_project.core.ui.utils.SelectedSortOption;
import oodj_project.core.ui.utils.SortOption;

public class SortFormContent<DataT> extends JPanel {

    private final List<SortOptionPanel<DataT>> sortList;
    private final JPanel sortListPanel = new JPanel();
    private final JScrollPane scrollPane;

    public SortFormContent(List<SortOption<DataT>> sortOptions, List<SelectedSortOption<DataT>> selectedOptionList) {
        super(new BorderLayout());   
        
        sortList = new ArrayList<>();
        
        scrollPane = new JScrollPane(sortListPanel);
        add(scrollPane, BorderLayout.CENTER);
        
        var buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

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
