package oodj_project.app;

import java.io.IOException;

import javax.swing.SwingUtilities;

import oodj_project.core.data.Context;
import oodj_project.core.security.Session;
import oodj_project.features.login.LoginView;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                var context = Context.instance();
                context.initialize();

                var session = new Session(context);
                showLogin(session, context);

            } catch (IOException e) {
                System.err.println(e);
            }
        });
    }

    private static LoginView showLogin(Session session, Context context) {
        return new LoginView(session, () -> showMainFrame(session, context));
    }

    private static MainFrame showMainFrame(Session session, Context context) {
        return new MainFrame(context, session, () -> showLogin(session, context));
    }
}