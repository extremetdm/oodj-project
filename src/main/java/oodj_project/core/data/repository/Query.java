package oodj_project.core.data.repository;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Query<DataT> {
    private int page = 1;
    private Integer limit = null;
    private Predicate<DataT> filter = null;
    private Comparator<DataT> sorter = null;

    public int page() {
        return page;
    }

    public Integer limit() {
        return limit;
    }

    public Predicate<DataT> filter() {
        return filter;
    }

    public Comparator<DataT> sorter() {
        return sorter;
    }

    public PaginatedResult<DataT> apply(Collection<DataT> data) {
        int totalItems = (int) createFilteredStream(data).count(),
            perPage = totalItems;

        var stream = createFilteredStream(data);
        
        if (sorter != null)
            stream = stream.sorted(sorter);
        
        if (limit != null) {
            perPage = limit;
            stream = stream.skip((page - 1) * limit)
                .limit(limit);
        }

        return new PaginatedResult<>(stream.toList(), page, perPage, totalItems);
    }

    private Stream<DataT> createFilteredStream(Collection<DataT> data) {
        var stream = data.stream();
        if (filter != null) {
            stream = stream.filter(filter);
        }
        return stream;
    }

    public static class Builder<DataT> {
        private final Query<DataT> query = new Query<>();

        public Builder<DataT> page(int page) {
            if (page < 1) page = 1;
            query.page = page;
            return this;
        }

        public Builder<DataT> limit(int limit) {
            if (limit < 1) limit = 1;
            query.limit = limit;
            return this;
        }

        public Builder<DataT> filterBy(Predicate<DataT> filter) {
            query.filter = filter;
            return this;
        }

        public Builder<DataT> sortBy(Comparator<DataT> sorter) {
            query.sorter = sorter;
            return this;
        }

        public Query<DataT> build() {
            return query;
        }
    }

    public static <DataT> Builder<DataT> builder() {
        return new Builder<>();
    }
}
