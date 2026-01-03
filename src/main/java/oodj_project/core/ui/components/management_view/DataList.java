package oodj_project.core.ui.components.management_view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.util.List;
import java.util.function.Function;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import oodj_project.core.ui.utils.GridBuilder;

public class DataList<DataT> extends JScrollPane {
    protected final double[] columnWeights;
    protected final Function<DataT, Component[]> rowCreator;

    protected final JPanel contentPanel = new JPanel();

    private static final Border BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
        BorderFactory.createEmptyBorder(0, 10, 0, 10)
    );

    private static final Font
        HEADER_FONT = new Font("Arial", Font.BOLD, 20),
        FONT = new Font("SansSerif", Font.PLAIN, 15);

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

        columnHeaderPanel.setBorder(BORDER);

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
                panel.setBorder(BORDER);
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

    public static JLabel createHeaderText(String text) {
        var label = new JLabel(text);
        label.setFont(HEADER_FONT);
        return label;
    }

    public static JLabel createText(String text) {
        return createText(text, false);
    }

    public static JLabel createText(String text, boolean isItalic) {
        var label = new JLabel(text);
        label.setFont(isItalic? FONT.deriveFont(Font.ITALIC): FONT);
        return label;
    }
}
