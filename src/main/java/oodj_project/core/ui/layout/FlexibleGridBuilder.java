package oodj_project.core.ui.layout;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JPanel;

public class FlexibleGridBuilder {

    private final JPanel mainPanel;

    private final int rowWidth;

    private final GridBagConstraints constraints = new GridBagConstraints();


    public FlexibleGridBuilder(int rowWidth) {
        this(new JPanel(), rowWidth);
    }

    public FlexibleGridBuilder(JPanel mainPanel, int rowWidth) {
        this.mainPanel = mainPanel;
        mainPanel.removeAll();
        mainPanel.setLayout(new GridBagLayout());

        this.rowWidth = rowWidth;

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridheight = 1;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weighty = 0;
    }

    public FlexibleGridBuilder setInsets(Insets insets) {
        constraints.insets = insets;
        return this;
    }

    public FlexibleGridBuilder add(Component... components) {
        return add(null, null, components);
    }

    public FlexibleGridBuilder add(int gridWidths[], Component... components) {
        return add(gridWidths, null, components);
    }

    public FlexibleGridBuilder add(double[] columnWeights, Component... components) {
        return add(null, columnWeights, components);
    }

    public FlexibleGridBuilder add(int gridWidths[], double[] columnWeights, Component... components) {
        if (gridWidths == null) {
            gridWidths = new int[] {1};
        }
        if (columnWeights == null) {
            columnWeights = new double[] {1};
        }
        int columnWeightCycle = columnWeights.length;
        int gridWidthCycle = gridWidths.length;
        for (int x = 0; x < components.length; x++) {
            constraints.weightx = columnWeights[x % columnWeightCycle];
            var gridWidth = gridWidths[x % gridWidthCycle];
            constraints.gridwidth = gridWidth;
            if (constraints.gridx + gridWidth > rowWidth) {
                constraints.gridx = 0;
                constraints.gridy++;
            }
            var component = components[x];
            mainPanel.add(component, constraints);

            constraints.gridx += gridWidth;
        }
        return this;
    }

    public FlexibleGridBuilder addStrutGlue(int width) {
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.weighty = 1;
        constraints.gridwidth = rowWidth;
        mainPanel.add(Box.createHorizontalStrut(width), constraints);
        constraints.gridy++;
        constraints.weighty = 0;
        return this;
    } 

    public JPanel build() {
        mainPanel.revalidate();
        mainPanel.repaint();
        return mainPanel;
    }

    // public FlexibleGridBuilder add(Component component, int width, double weight) {
        
    //     return this;
    // }
}
