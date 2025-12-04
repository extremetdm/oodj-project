package oodj_project.features.navigation;

import javax.swing.JPanel;

import oodj_project.core.data.Context;
import oodj_project.core.security.Session;

@FunctionalInterface
public interface ViewGenerator {
    public JPanel create(Context context, Session session);
}
