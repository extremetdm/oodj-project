package oodj_project.features.services;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class EmailService {
    private final String
        emailJsApiUrl,
        serviceId,
        templateId,
        resetTemplateId,
        userId;

    public EmailService(
        String emailJsApiUrl,
        String serviceId,
        String templateId,
        String resetTemplateId,
        String userId
    ) {
        this.emailJsApiUrl = emailJsApiUrl;
        this.serviceId = serviceId;
        this.templateId = templateId;
        this.resetTemplateId = resetTemplateId;
        this.userId = userId;
    }

    public void sendWelcomeEmail(String toEmail, String fullName, String username, String password) {
        new Thread(() -> {
            try {
                URL url = new URI(emailJsApiUrl).toURL();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                // Construct JSON payload manually to avoid dependencies
                String jsonInputString = String.format(
                        "{" +
                            "\"service_id\": \"%s\"," +
                                "\"template_id\": \"%s\"," +
                                "\"user_id\": \"%s\"," +
                                "\"template_params\": {" +
                                "\"email\": \"%s\"," +
                                "\"name\": \"%s\"," +
                                "\"username\": \"%s\"," +
                                "\"password\": \"%s\"" +
                                "}" +
                                "}",
                        serviceId, templateId, userId, toEmail, fullName, username, password);

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    System.out.println("Email sent successfully to " + toEmail);
                } else {
                    System.err.println("Failed to send email. Response Code: " + responseCode);
                    try (java.io.BufferedReader br = new java.io.BufferedReader(
                            new java.io.InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        System.err.println("Error details: " + response.toString());
                    }
                }
            } catch (IOException|URISyntaxException e) {
                // e.printStackTrace();
                System.err.println("Error sending email: " + e.getMessage());
            }
        }).start();
    }

    public void sendPasswordResetEmail(String toEmail, String fullName, String newPassword) {
        new Thread(() -> {
            try {
                URL url = new URI(emailJsApiUrl).toURL();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                // Construct JSON payload manually
                // Assuming the reset template takes: name, new_password, reply_to (implied or
                // explicit)
                String jsonInputString = String.format(
                        "{" +
                                "\"service_id\": \"%s\"," +
                                "\"template_id\": \"%s\"," +
                                "\"user_id\": \"%s\"," +
                                "\"template_params\": {" +
                                "\"email\": \"%s\"," +
                                "\"name\": \"%s\"," +
                                "\"new_password\": \"%s\"" + // Parameter name as expected by template
                                "}" +
                                "}",
                        serviceId, resetTemplateId, userId, toEmail, fullName, newPassword);

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    System.out.println("Password reset email sent successfully to " + toEmail);
                } else {
                    System.err.println("Failed to send reset email. Response Code: " + responseCode);
                }
            } catch (IOException|URISyntaxException e) {
                // e.printStackTrace();
                System.err.println("Error sending reset email: " + e.getMessage());
            }
        }).start();
    }
}
