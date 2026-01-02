package oodj_project.core.ui.components.management_view;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import oodj_project.core.data.repository.PaginatedResult;
import oodj_project.core.ui.components.buttons.IconButton;
import oodj_project.core.ui.utils.Clickable;
import oodj_project.core.ui.utils.SelectorRenderer;

public class Paginator extends JPanel {

    private final JComboBox<Integer> perPageSelector = new JComboBox<>(
        new Integer[] { 10, 15, 20, 25, 50, 100 }
    );

    private static final Dimension SELECTOR_SIZE = new Dimension(60, 30);

    private final JComboBox<Integer> pageSelector = new JComboBox<>(
        new Integer[] { 1 }
    );

    private final JLabel resultStatusLabel = new JLabel("Showing X - Y of Z results");

    private final JButton firstPageButton = new IconButton("/icons/first.png", 30,30);
    private final JButton previousPageButton = new IconButton("/icons/previous.png", 40,40);
    private final IconButton nextPageButton = new IconButton("/icons/next.png", 40,40);
    private final IconButton lastPageButton = new IconButton("/icons/last.png", 30,30);

    private int totalPages = 1;

    public Paginator(Runnable onUpdate) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        var perPageLabel = new JLabel("Rows per page");
        perPageLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        perPageLabel.setBorder(
            BorderFactory.createEmptyBorder(0, 10, 0, 10)
        );
        add(perPageLabel);
        
        perPageSelector.setPreferredSize(SELECTOR_SIZE);
        perPageSelector.setMaximumSize(SELECTOR_SIZE);
        perPageSelector.setRenderer(new SelectorRenderer());
        perPageSelector.addMouseListener(Clickable.INSTANCE);
        perPageSelector.addActionListener(event -> {
            pageSelector.setSelectedItem(1);
        });
        add(perPageSelector);

        add(Box.createHorizontalGlue());

        resultStatusLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        add(resultStatusLabel);

        add(Box.createHorizontalGlue());

        firstPageButton.setToolTipText("First Page");
        firstPageButton.addActionListener(event -> {
            pageSelector.setSelectedItem(1);
        });
        add(firstPageButton);
        
        previousPageButton.setToolTipText("Previous Page");
        previousPageButton.addActionListener(event -> {
            pageSelector.setSelectedItem(getCurrentPage() - 1);
        });
        add(previousPageButton);
        
        pageSelector.setToolTipText("Page");
        pageSelector.setPreferredSize(SELECTOR_SIZE);
        pageSelector.setMaximumSize(SELECTOR_SIZE);
        pageSelector.setRenderer(new SelectorRenderer());
        pageSelector.addMouseListener(Clickable.INSTANCE);
        pageSelector.addActionListener(event -> {
            onUpdate.run();
            updateButtonState();
        });
        add(pageSelector);
        
        nextPageButton.setToolTipText("Next Page");
        nextPageButton.addActionListener(event -> {
            pageSelector.setSelectedItem(getCurrentPage() + 1);
        });
        add(nextPageButton);
        
        lastPageButton.setToolTipText("Last Page");
        lastPageButton.addActionListener(event -> {
            pageSelector.setSelectedItem(totalPages);
        });
        add(lastPageButton);
    }

    private void updateButtonState() {
        int currentPage = getCurrentPage();
        
        boolean canGoPrevious = currentPage != 1;
        firstPageButton.setEnabled(canGoPrevious);
        previousPageButton.setEnabled(canGoPrevious);

        boolean canGoNext = currentPage != totalPages;
        nextPageButton.setEnabled(canGoNext);
        lastPageButton.setEnabled(canGoNext);

        pageSelector.setEnabled(totalPages != 1);
    }
    
    public <DataT> void update(PaginatedResult<DataT> result) {

        var listeners = pageSelector.getActionListeners();
        for (var listener: listeners) {
            pageSelector.removeActionListener(listener);
        }

        totalPages = result.totalPages();

        var pages = new Integer[totalPages];
        for (int index = 0; index < totalPages; index++) {
            pages[index] = index + 1;
        }

        pageSelector.setModel(new DefaultComboBoxModel<>(pages));
        pageSelector.setSelectedItem(result.page());

        for (var listener: listeners) {
            pageSelector.addActionListener(listener);
        }

        updateButtonState();

        resultStatusLabel.setText("Showing " + result.from() + " - " + result.to() + " of " + result.totalItems() + " results");
    }

    public Integer getPerPage() {
        return (Integer) perPageSelector.getSelectedItem();
    }

    public Integer getCurrentPage() {
        return (Integer) pageSelector.getSelectedItem();
    }

    public void goToFirstPage() {
        pageSelector.setSelectedItem(1);
    }
}
