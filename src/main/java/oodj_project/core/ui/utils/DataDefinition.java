package oodj_project.core.ui.utils;

import java.util.function.Function;

public class DataDefinition {
    protected static <DataT, FieldRecordT, FieldT>
    Function<DataT, FieldT> combineExtractor(
        Function<DataT, FieldRecordT> dataRecordExtractor,
        Function<FieldRecordT, FieldT> fieldExtractor
    ) {
        return data -> fieldExtractor.apply(
            dataRecordExtractor.apply(data)
        );
    }
}
