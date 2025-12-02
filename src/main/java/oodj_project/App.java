package oodj_project;

import oodj_project.core.Context;

public class App {
    public static void main(String[] args) {
        try {
            Context.instance().initialize();
        } catch (Exception e) {

        }
    }
}