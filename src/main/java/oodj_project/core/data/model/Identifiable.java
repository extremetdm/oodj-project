package oodj_project.core.data.model;

public interface Identifiable<DataT extends Record> {
    Integer id();
    DataT withId(int id);
}