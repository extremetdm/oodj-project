package oodj_project.features.dashboard.team_management;

import java.awt.Component;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import oodj_project.core.ui.components.form.FormComboBox;
import oodj_project.core.ui.components.form.FormLabel;
import oodj_project.core.ui.components.form.FormTextField;
import oodj_project.core.ui.layout.FlexibleGridBuilder;
import oodj_project.features.dashboard.user_management.User;

public class TeamMemberEditFormContent extends JPanel {

    private static final double[] COLUMN_WEIGHTS = { 0, 1 };

    private final FormComboBox<User> memberField, supervisorField;

    public TeamMemberEditFormContent(
        List<User> lecturers,
        List<User> supervisors
    ) {
        this(lecturers, supervisors, null);
    }

    public TeamMemberEditFormContent(
        List<User> supervisors,
        MemberAssignment relation
    ) {
        this(List.of(relation.member()), supervisors, relation);
    }

    private TeamMemberEditFormContent(
        List<User> lecturers,
        List<User> supervisors,
        MemberAssignment relation
    ) {
        super();

        var builder = new FlexibleGridBuilder(this, 2)
            .setInsets(new Insets(5, 5, 5, 5));

        memberField = new FormComboBox<>(
            User::name,
            lecturers
        );
        supervisorField = new FormComboBox<>(
            supervisor -> supervisor.name(),
            supervisors
        );

        Component memberFieldComponent = memberField;

        if (relation != null) {
            memberField.setSelectedItem(relation.member());
            supervisorField.setSelectedItem(relation.supervisor());
            
            memberFieldComponent = new FormTextField(relation.member().name(), false);
        }

        builder.add(
            COLUMN_WEIGHTS,
            new FormLabel("Lecturer"),
            memberFieldComponent,
            new FormLabel("Academic Leader"),
            supervisorField
        )
            .addStrutGlue(600)
            .build();

        setBorder(BorderFactory.createEmptyBorder(20, 15, 10, 15));
    }

    public TeamMember getFormData() {
        return new TeamMember(
            supervisorField.getSelectedItem(),
            memberField.getSelectedItem()
        );
    }
}