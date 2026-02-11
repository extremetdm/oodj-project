package oodj_project.features.dashboard.module_management;

import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import oodj_project.core.ui.components.management_view.DataList;
import oodj_project.core.ui.layout.FlexibleGridBuilder;

public class ModuleGrid extends JPanel {
    public ModuleGrid(Module module) {
        super();
        
        var idLabel = DataList.createText("ID:");
        idLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        new FlexibleGridBuilder(this, 2)
            .setInsets(new Insets(0, 5, 0, 5))
            .add(
                new double[] { 0, 1 },
                idLabel,
                DataList.createText(module.id().toString()),
                DataList.createText("Name:"),
                DataList.createText(module.name())
            )
            .build();
    }
}
