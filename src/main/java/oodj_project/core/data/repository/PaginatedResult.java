package oodj_project.core.data.repository;

import java.util.List;

public record PaginatedResult<DataT>(List<DataT> data, int page, int itemsPerPage, int totalItems) {

    public PaginatedResult {
        if (page < 1) throw new IllegalArgumentException("Page number must be at least 1.");
        if (itemsPerPage < 1) throw new IllegalArgumentException("Items per page must be at least 1.");
        if (totalItems < 0) throw new IllegalArgumentException("Total items must be greater than 0.");
    }

    public int from() {
        if (data.isEmpty()) return 0;
        return (page - 1) * itemsPerPage + 1;
    }

    public int to() {
        if (data.isEmpty()) return 0;
        return (page - 1) * itemsPerPage + data.size();
    }

    public int totalPages() {
        if (totalItems == 0) return 1;
        return (totalItems + itemsPerPage - 1) / itemsPerPage;
    }
}
