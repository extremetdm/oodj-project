package oodj_project.core.ui.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import oodj_project.core.ui.components.buttons.IconLabelButton;
import oodj_project.core.ui.utils.GridBuilder;
import oodj_project.core.ui.utils.IconManager;

public class ListEditorPanel<RowT extends Component, ResultT> extends JPanel {

    protected final List<RowT> rowList = new ArrayList<>();
    private final JPanel contentPanel = new JPanel();
    private final JScrollPane scrollPane;

    private final BiFunction<ResultT, Consumer<RowT>, RowT> rowPanelCreator;
    private final Function<RowT, ResultT> rowDataExtractor;

    public ListEditorPanel(Builder<RowT, ResultT> builder) {
        super(new BorderLayout());   

        this.rowPanelCreator = builder.rowPanelCreator;
        this.rowDataExtractor = builder.rowDataExtractor;

        setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        scrollPane = new JScrollPane(contentPanel);
        add(scrollPane, BorderLayout.CENTER);
        
        var buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        var deleteButton = new IconLabelButton(builder.deleteButtonText, builder.deleteButtonIcon);
        deleteButton.addActionListener(event -> {
            rowList.removeAll(rowList);
            updateListDisplay();
        });
        buttonPanel.add(deleteButton);
        
        buttonPanel.add(Box.createHorizontalGlue());

        var addButton = new IconLabelButton(builder.addButtonText, builder.addButtonIcon);
        addButton.addActionListener(event -> addRow());
        buttonPanel.add(addButton);

        add(buttonPanel, BorderLayout.SOUTH);

        if (builder.initialData == null || builder.initialData.isEmpty()) {
            addRow();
        } else {
            builder.initialData.forEach(data -> addRow(data));
        }

        var panelInsets = contentPanel.getInsets();
        var scrollPaneInsets = scrollPane.getInsets();
        
        var totalHeight = 5 * rowList.get(0).getPreferredSize().height
            + scrollPaneInsets.top + scrollPaneInsets.bottom
            + panelInsets.top + panelInsets.bottom;

        scrollPane.setPreferredSize(new Dimension(builder.contentWidth, totalHeight));
    }

    private void updateListDisplay() {
        var builder = new GridBuilder(contentPanel)
            .setMinWidth(100);
        rowList.forEach(builder::add);
        builder.addGlue()
            .build();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    private void addRow() {
        addRow(null);
    }

    private void addRow(ResultT initialData) {
        rowList.add(rowPanelCreator.apply(initialData, this::removeRow));
        updateListDisplay();

        SwingUtilities.invokeLater(() -> {
            var scrollBar = scrollPane.getVerticalScrollBar();
            scrollBar.setValue(scrollBar.getMaximum());
        });
    }

    private void removeRow(RowT row) {
        rowList.remove(row);
        updateListDisplay();
    }

    public List<ResultT> getList() {
        return rowList.stream()
            .map(rowDataExtractor)
            .toList();
    }

    public static class Builder<RowT extends Component, ResultT> {
        private Icon
            addButtonIcon = IconManager.getIcon("/icons/add.png", 30, 30),
            deleteButtonIcon = IconManager.getIcon("/icons/delete.png", 30, 30);
        
        private String
            addButtonText = "Add",
            deleteButtonText = "Clear All";

        private int contentWidth = 600;

        private final BiFunction<ResultT, Consumer<RowT>, RowT> rowPanelCreator;
        private final Function<RowT, ResultT> rowDataExtractor;
        private final List<ResultT> initialData;

        private Builder(
            BiFunction<ResultT, Consumer<RowT>, RowT> rowPanelCreator,
            Function<RowT, ResultT> rowDataExtractor,
            List<ResultT> initialData
        ) {
            this.rowPanelCreator = rowPanelCreator;
            this.rowDataExtractor = rowDataExtractor;
            this.initialData = initialData;
        }

        public Builder<RowT, ResultT> addButtonIcon(Icon icon) {
            addButtonIcon = icon;
            return this;
        }

        public Builder<RowT, ResultT> deleteButtonIcon(Icon icon) {
            deleteButtonIcon = icon;
            return this;
        }

        public Builder<RowT, ResultT> addButtonText(String text) {
            addButtonText = text;
            return this;
        }

        public Builder<RowT, ResultT> deleteButtonText(String text) {
            deleteButtonText = text;
            return this;
        }

        public Builder<RowT, ResultT> contentWidth(int width) {
            contentWidth = width;
            return this;
        }

        public ListEditorPanel<RowT, ResultT> build() {
            return new ListEditorPanel<>(this);
        }
    }

    public static <RowT extends Component, ResultT> Builder<RowT, ResultT> builder(
        BiFunction<ResultT, Consumer<RowT>, RowT> rowPanelCreator,
        Function<RowT, ResultT> rowDataExtractor,
        List<ResultT> initialData
    ) {
        return new Builder<>(rowPanelCreator, rowDataExtractor, initialData);
    }
}
