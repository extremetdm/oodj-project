package oodj_project.core.ui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.util.List;
import java.util.function.Function;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import oodj_project.core.ui.utils.GridBuilder;

public class DataList<DataT> extends JScrollPane {
    protected final double[] columnWeights;
    protected final Function<DataT, Component[]> rowCreator;

    protected final JPanel contentPanel = new JPanel();

    public DataList(
        double[] columnWeights,
        Component[] columnHeaders,
        Function<DataT, Component[]> rowCreator
    ) {
        this.columnWeights = columnWeights;
        this.rowCreator = rowCreator;

        setViewportView(contentPanel);
        // setBorder(BorderFactory.createEmptyBorder());

        var columnHeaderPanel = GridBuilder.createRow(
            50,
            columnWeights, 
            new Insets(5, 5, 5, 5),
            columnHeaders
        );

        columnHeaderPanel.setBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY)
        );

        setColumnHeaderView(columnHeaderPanel);

        var corner = new JPanel();
        corner.setBackground(Color.LIGHT_GRAY);
        setCorner(UPPER_RIGHT_CORNER, corner);
    }

    public final void setData(List<DataT> data) {
        var builder = new GridBuilder(contentPanel)
            .setColumnWeights(columnWeights)
            .setRowHeight(50)
            .setInsets(new Insets(0, 5, 0, 5))
            .setMinWidth(600)
            .setRowStyler((panel, index) -> {
                // panel.setBackground((index & 1) == 0? Color.WHITE: Color.LIGHT_GRAY);
                panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
            });
        if (data.isEmpty()) {
            builder.add(new JLabel("No results found."));
        } else {
            for (var model: data) {
                builder.addRow(rowCreator.apply(model));
            }
        }
        
        builder.addGlue()
            .build();
    }
}
