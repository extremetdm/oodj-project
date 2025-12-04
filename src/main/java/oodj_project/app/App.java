package oodj_project.app;

import javax.swing.SwingUtilities;

import oodj_project.core.data.Context;
import oodj_project.core.security.Session;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                var context = Context.instance();
                context.initialize();

                var session = new Session(context);
                session.login("", "");

                var frame = new MainFrame(context, session);

            } catch (Exception e) {
                System.err.println(e);
            }
        });
    }
}