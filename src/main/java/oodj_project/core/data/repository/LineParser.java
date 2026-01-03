package oodj_project.core.data.repository;

@FunctionalInterface
public interface LineParser<DataT> {
    public DataT parse(String... line) throws IllegalArgumentException;
}
