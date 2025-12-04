package oodj_project.core.ui_components;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DataTable extends JScrollPane {
    
    private DefaultTableModel model;
    private JTable table;
    private String[] columnNames;

    public DataTable(String[] columnNames) {
        this(columnNames, new Object[][] {});
    }

    public DataTable(String[] columnNames, Object[][] data) {
        super();
        this.columnNames = columnNames;
        model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.setColumnSelectionAllowed(false);

        setViewportView(table);
    }

    public void setData(Object[][] data) {
        model.setDataVector(data, columnNames);
    }

    public int getSelectedRow() {
        return table.getSelectedRow();
    }

    public int getSelectedRowCount() {
        return table.getSelectedRowCount();
    }
}
