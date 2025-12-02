package oodj_project.core.model;

public interface Identifiable<DataT extends Record> {
    Integer id();
    DataT withId(int id);
}