package oodj_project.features.dashboard.assessment_management;

import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import oodj_project.core.ui.components.management_view.DataList;
import oodj_project.core.ui.layout.FlexibleGridBuilder;

public class AssessmentGrid extends JPanel {
    public AssessmentGrid(Assessment assessment) {
        super();

        var idLabel = DataList.createText("ID:");
        idLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        new FlexibleGridBuilder(this, 2)
            .setInsets(new Insets(0, 5, 0, 5))
            .add(
                new double[] { 0, 1 },
                idLabel,
                DataList.createText(assessment.id().toString()),
                DataList.createText("Name:"),
                DataList.createText(assessment.name()),
                DataList.createText("Type:"),
                DataList.createText(assessment.type().name())
            )
            .build();
    }
}
