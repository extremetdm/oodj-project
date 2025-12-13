package oodj_project.core.ui.utils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.function.BiConsumer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class GridBuilder {
    private final JPanel mainPanel;
    
    private Insets insets = new Insets(5, 5, 5, 5);
    private double[] defaultColumnWeights = null;
    private int defaultRowHeight = 50;
    private int minWidth = 600;

    private int rowIndex = 0;
    private BiConsumer<JPanel, Integer> rowStyler;
   
    public GridBuilder() {
        this(new JPanel());
    }
    
    public GridBuilder(JPanel mainPanel) {
        this.mainPanel = mainPanel;
        mainPanel.removeAll();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    }

    public GridBuilder setMinWidth(int width) {
        minWidth = width;
        return this;
    }

    public GridBuilder setInsets(Insets insets) {
        this.insets = insets;
        return this;
    }

    public GridBuilder setColumnWeights(double... columnWeights) {
        defaultColumnWeights = columnWeights;
        return this;
    }

    public GridBuilder setRowHeight(int rowHeight) {
        defaultRowHeight = rowHeight;
        return this;
    }

    public GridBuilder setRowStyler(BiConsumer<JPanel, Integer> rowStyler) {
        this.rowStyler = rowStyler;
        return this;
    }

    public GridBuilder addRow(Component... components) {
        return addRow(defaultRowHeight, defaultColumnWeights, components);
    }

    public GridBuilder addRow(int rowHeight, Component... components) {
        return addRow(rowHeight, defaultColumnWeights, components);
    }

    public GridBuilder addRow(double[] columnWeight, Component... components) {
        return addRow(defaultRowHeight, columnWeight, components);
    }

    public GridBuilder addRow(int rowHeight, double[] columnWeight, Component... components) {
        var rowPanel = GridBuilder.createRow(rowHeight, columnWeight, insets, components);
        if (rowStyler != null)
            rowStyler.accept(rowPanel, rowIndex);
        mainPanel.add(rowPanel);
        rowIndex++;
        return this;
    }

    public static JPanel createRow(int rowHeight, double[] columnWeight, Insets insets, Component... components) {
        if (columnWeight == null) {
            columnWeight = new double[components.length];
            for (int index = 0; index < components.length; index++) {
                columnWeight[index] = 1;
            }
        }

        var rowPanel = new JPanel(new GridBagLayout());
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 0));
        
        var gridConstraints = new GridBagConstraints();
        gridConstraints.fill = GridBagConstraints.BOTH;
        gridConstraints.gridheight = 1;
        gridConstraints.insets = insets;

        for (int x = 0; x < components.length; x++) {
            gridConstraints.gridx = x;
            gridConstraints.weightx = columnWeight[x];
            var component = components[x];
            component.setPreferredSize(new Dimension(0, rowHeight));
            component.setMinimumSize(new Dimension(0, rowHeight));
            rowPanel.add(components[x], gridConstraints);
        }

        return rowPanel;
    }

    public GridBuilder addGlue() {
        mainPanel.add(Box.createVerticalGlue());
        return this;
    }

    public GridBuilder addGap(int height) {
        mainPanel.add(Box.createVerticalStrut(height));
        return this;
    }

    public GridBuilder add(Component component) {
        mainPanel.add(component);
        return this;
    }

    public JPanel build() {
        mainPanel.add(Box.createHorizontalStrut(minWidth));
        mainPanel.revalidate();
        mainPanel.repaint();

        return mainPanel;
    }
}
