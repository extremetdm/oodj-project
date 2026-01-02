package oodj_project.core.ui.components.management_view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import oodj_project.core.data.repository.PaginatedResult;
import oodj_project.core.data.repository.Query;
import oodj_project.core.ui.components.buttons.IconLabelButton;
import oodj_project.core.ui.components.filter_editor.SelectedFilterOption;
import oodj_project.core.ui.components.sort_editor.SelectedSortOption;
import oodj_project.core.ui.utils.IconManager;

public abstract class ManagementView<DataT> extends JPanel {

    private static final ImageIcon
        FILTER_ICON = IconManager.getIcon("/icons/filter.png", 30, 30),
        SORT_ICON = IconManager.getIcon("/icons/sort.png", 30, 30);

    private final SearchBar searchBar;
    private final Paginator paginator;

    private List<SelectedSortOption<DataT>> sortOptions = List.of();
    private List<SelectedFilterOption<DataT, ?, ?>> filterOptions = List.of();

    private final String title;

    protected final List<Component> toolbarComponents;

    private final Function<String, Predicate<DataT>> searchLogicGenerator;
    private final Function<Query<DataT>, PaginatedResult<DataT>> dataProvider;

    public ManagementView(
        String title,
        Function<Query<DataT>, PaginatedResult<DataT>> dataProvider,
        Function<String, Predicate<DataT>> searchLogicGenerator
    ) {
        super(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));

        this.dataProvider = dataProvider;
        this.searchLogicGenerator = searchLogicGenerator;

        this.title = title;

        searchBar = new SearchBar(this::refreshData);
        paginator = new Paginator(this::refreshData);

        toolbarComponents = new ArrayList<>(List.of(
            searchBar,
            createFilterButton(),
            createSortButton()
        ));
    }

    protected abstract DataList<DataT> getContent();

    protected void init() {
        add(createHeader(title), BorderLayout.NORTH);

        add(getContent(), BorderLayout.CENTER);

        add(paginator, BorderLayout.SOUTH);

        refreshData();
    }

    public void refreshData() {
        var sorter = sortOptions.stream()
            .map(SelectedSortOption::comparator)
            .reduce((combined, next) -> combined.thenComparing(next))
            .orElse(null);

        var filter = filterOptions.stream()
            .map(option -> (Predicate<DataT>) option::test)
            .reduce((combined, next) -> combined.and(next))
            .orElse(model -> true);

        var searchQuery = searchBar.getQueryText();
        if (!searchQuery.isEmpty()) {
            filter = filter.and(searchLogicGenerator.apply(searchQuery));
        }

        var query = Query.<DataT>builder()
            .page(paginator.getCurrentPage())
            .limit(paginator.getPerPage())
            .sortBy(sorter)
            .filterBy(filter)
            .build();

        var result = dataProvider.apply(query);

        getContent().setData(result.data());
        paginator.update(result);
    }

    protected JPanel createHeader(String title) {
        var header = new JPanel(new BorderLayout());
        var titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Swis721 BlkCn BT", Font.BOLD, 50));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        header.add(titleLabel, BorderLayout.NORTH);

        var panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        for (int index = 0; index < toolbarComponents.size(); index++) {
            if (index > 0) panel.add(Box.createHorizontalStrut(10));
            panel.add(toolbarComponents.get(index));
        } 

        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        header.add(panel, BorderLayout.SOUTH);

        return header;
    }

    protected abstract FormFactory<DataT> getFormFactory();

    private IconLabelButton createSortButton() {
        var sortButton = new IconLabelButton("Sort", SORT_ICON);
        sortButton.addActionListener(event -> {
            getFormFactory().getSortForm(sortOptions, appliedSortOption -> {
                sortOptions = appliedSortOption;
                if (sortOptions.isEmpty()) {
                    sortButton.setBackground(null);
                    sortButton.setForeground(null);
                    sortButton.setText("Sort");
                    sortButton.setToolTipText(null);
                } else {
                    sortButton.setBackground(new Color(225, 240, 255));
                    sortButton.setForeground(new Color(50, 100, 200));
                    sortButton.setText("Sort (" + sortOptions.size() + ")");
                    String sortSummary = "<html><b>Active Sorts:</b><br>" + sortOptions.stream()
                        .map(sortOption -> String.format(
                            "- %s (%s)",
                            escapeHTML(sortOption.option().label()),
                            escapeHTML(sortOption.isDescending()? "Descending": "Ascending")
                        ))
                        .collect(Collectors.joining("<br>"))
                        + "</html>";
                    sortButton.setToolTipText(sortSummary);
                }
                refreshData();
            });
        });

        return sortButton;
    }

    private IconLabelButton createFilterButton() {
        var filterButton = new IconLabelButton("Filter", FILTER_ICON);
        filterButton.addActionListener(event -> {
            getFormFactory().getFilterForm(filterOptions, appliedFilters -> {
                filterOptions = appliedFilters;
                if (filterOptions.isEmpty()) {
                    filterButton.setBackground(null);
                    filterButton.setForeground(null);
                    filterButton.setText("Filter");
                    filterButton.setToolTipText(null);
                } else {
                    filterButton.setBackground(new Color(225, 240, 255));
                    filterButton.setForeground(new Color(50, 100, 200));
                    filterButton.setText("Filter (" + filterOptions.size() + ")");
                    String sortSummary = "<html><b>Active Filters:</b><br>" + filterOptions.stream()
                        .map(filterOption -> String.format(
                            "- %s %s %s",
                            escapeHTML(filterOption.option().label()),
                            escapeHTML(filterOption.operation().briefLabel()),
                            escapeHTML(filterOption.criteriaLabel())
                        ))
                        .collect(Collectors.joining("<br>"))
                        + "</html>";
                    filterButton.setToolTipText(sortSummary);
                }
                refreshData();
            });
        });

        return filterButton;
    }

    public static String escapeHTML(String str) {
        if (str == null) return null;
        return str.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }
}