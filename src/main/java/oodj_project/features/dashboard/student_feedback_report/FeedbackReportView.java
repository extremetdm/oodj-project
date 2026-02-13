package oodj_project.features.dashboard.student_feedback_report;

import java.awt.Component;
import java.awt.Insets;
import java.util.function.Predicate;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import oodj_project.core.security.Session;
import oodj_project.core.ui.components.management_view.DataList;
import oodj_project.core.ui.components.management_view.ManagementView;
import oodj_project.core.ui.layout.FlexibleGridBuilder;
import oodj_project.features.dashboard.module_management.ModuleGrid;
import oodj_project.features.dashboard.user_management.UserGrid;

public class FeedbackReportView extends ManagementView<FeedbackReport> {

    private static final double[] COLUMN_WEIGHT = { 1, 3, 5, 5, 5, 5 };

    private final FeedbackReportFormFactory formFactory;
    private final DataList<FeedbackReport> dataTable;
    
    public FeedbackReportView(Session session, FeedbackReportController controller) {
        super("Enrollment Report", controller::index, FeedbackReportView::buildSearchLogic);

        dataTable = new DataList<>(
            COLUMN_WEIGHT,
            createTableHeader(),
            this::createTableRow,
            70
        );
        formFactory = new FeedbackReportFormFactory(this);

        init();
    }

    private static Predicate<FeedbackReport> buildSearchLogic(String searchQuery) {
        return report -> {
            var classGroup = report.classGroup();
            var module = classGroup.module();
            var lecturer = classGroup.lecturer();

            return classGroup.id().toString().contains(searchQuery) ||
                module.id().toString().contains(searchQuery) ||
                module.name().toLowerCase().contains(searchQuery);
                // lecturer.id().toString().contains(searchQuery) || 
                // lecturer.name().toLowerCase().contains(searchQuery);
        };
    }

    private Component[] createTableHeader() {
        return new Component[] {
            DataList.createHeaderText("Class"),
            DataList.createHeaderText("Module"),
            // DataList.createHeaderText("Lecturer"),
            DataList.createHeaderText("Feedback Rate"),
            DataList.createHeaderText("Min. Score"),
            DataList.createHeaderText("Avg. Score"),
            DataList.createHeaderText("Max. Score")
        };
    }

    private Component[] createTableRow(FeedbackReport report) {

        var classGroup = report.classGroup();
        var summary = report.summary();
        var maxScore = 10;

        return new Component[] {
            DataList.createText(classGroup.id().toString()),
            new ModuleGrid(classGroup.module()),
            // new UserGrid(classGroup.lecturer()),
            createFeedbackRateSection(report),
            createScoreSection(summary.getMin(), maxScore),
            createScoreSection(((int) summary.getAverage()), maxScore),
            createScoreSection(summary.getMax(), maxScore),
        };
    }

    private JPanel createScoreSection(int score, int maxScore) {
        var scoreBar = new JProgressBar(0, maxScore);
        scoreBar.setValue(score);
        scoreBar.setStringPainted(true);

        var scoreSection = new FlexibleGridBuilder(2)
            .setInsets(new Insets(0, 5, 0, 5))
            .add(
                new double[] { 1, 0 },
                scoreBar,
                DataList.createText(score + "/" + maxScore)
            )
            .build();

        return scoreSection;
    }

    private JPanel createFeedbackRateSection(FeedbackReport report) {
        int totalFeedback = report.feedbackCount(),
            totalStudents = report.totalStudents();

        var feedbackRateBar = new JProgressBar(0, totalStudents == 0? 1: totalStudents);
        feedbackRateBar.setValue(totalFeedback);
        feedbackRateBar.setStringPainted(true);

        var feedbackRateSection = new FlexibleGridBuilder(2)
            .setInsets(new Insets(0, 5, 0, 5))
            .add(
                new double[] { 1, 0 },
                feedbackRateBar,
                DataList.createText(totalFeedback + "/" + totalStudents)
            )
            .build();
        feedbackRateSection.setToolTipText("<html>" +
            "<b>Feedback Rate Details:</b><br>" +
            "Total feedbacks: " + totalFeedback + "<br>" +
            "Total students: " + totalStudents + "<br>" +
        "</html>");

        return feedbackRateSection;
    }

    @Override
    protected FeedbackReportFormFactory getFormFactory() {
        return formFactory;
    }

    @Override
    protected DataList<FeedbackReport> getContent() {
        return dataTable;
    }
}
