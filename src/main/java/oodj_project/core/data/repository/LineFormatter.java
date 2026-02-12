package oodj_project.core.data.repository;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

import oodj_project.core.data.model.Identifiable;

@FunctionalInterface
public interface LineFormatter<DataT> {
    public String[] format(DataT model);

    public static <FieldT extends Record & Identifiable<FieldT>> String formatField(FieldT field) {
        return Optional.ofNullable(field)
                .map(FieldT::id)
                .orElse(0)
                .toString();
    }

    public static String formatDate(Date date) {
        if (date == null)
            return "null";
        return Long.toString(date.getTime());
    }

    public static String formatDate(Date date, String pattern) {
        if (date == null)
            return "null";
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern(pattern));
    }
}
