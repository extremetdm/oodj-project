package oodj_project.core.data.model;

public class Model {
    public static String normalize(String string) {
        if (string == null) return "";
        return string.trim();
    }
}
