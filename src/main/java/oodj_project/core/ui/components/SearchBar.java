package oodj_project.core.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import oodj_project.core.ui.utils.Clickable;
import oodj_project.core.ui.utils.IconManager;

public class SearchBar extends JPanel {
    private final JTextField searchField = new JTextField();

    public SearchBar(Runnable onSearch) {
        super(new BorderLayout());

        var panel = new JPanel(new BorderLayout());

        var searchIcon = new JLabel(IconManager.getIcon("/icons/search.png", 25, 25));
        searchIcon.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
        // searchIcon.setToolTipText("Search");
        
        var searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.PLAIN, 20));
        searchButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        searchButton.addMouseListener(Clickable.INSTANCE);
        searchButton.addActionListener(event -> onSearch.run());

        searchField.setOpaque(false);
        searchField.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        searchField.setFont(new Font("Arial", Font.PLAIN, 20));
        searchField.addActionListener(event -> searchButton.doClick());

        panel.add(searchIcon, BorderLayout.WEST);
        panel.add(searchField, BorderLayout.CENTER);

        add(panel, BorderLayout.CENTER);
        add(searchButton, BorderLayout.EAST);

        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    public String getQueryText() {
        return searchField.getText();
    }
}
