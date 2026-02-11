package oodj_project.features.dashboard.user_management;

import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import oodj_project.core.ui.components.management_view.DataList;
import oodj_project.core.ui.layout.FlexibleGridBuilder;

public class UserGrid extends JPanel {
    public UserGrid(User user) {
        super();

        var idLabel = DataList.createText("ID:");
        idLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        new FlexibleGridBuilder(this, 2)
            .setInsets(new Insets(0, 5, 0, 5))
            .add(
                new double[] { 0, 1 },
                idLabel,
                DataList.createText(user.id().toString()),
                DataList.createText("Name:"),
                DataList.createText(user.name())
            )
            .build();
    }
}
