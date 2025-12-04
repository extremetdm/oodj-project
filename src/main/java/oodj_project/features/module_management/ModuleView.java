package oodj_project.features.module_management;

import javax.swing.JPanel;

import oodj_project.core.security.Session;
import oodj_project.core.ui_components.DataTable;

public class ModuleView extends JPanel {

    public ModuleView(Session session, ModuleController controller) {
        super();
        add(new DataTable(
            new String[] {"walai", "kusalam"},
            new Object[][] {{1,3},{2,4},{1,3},{2,4},{1,3},{2,4},{1,3},{2,4},{1,3},{2,4},{1,3},{2,4}}
        ));
    }
}
