package oodj_project.features.dashboard.role_management;

import java.util.List;

import oodj_project.core.data.repository.PaginatedResult;
import oodj_project.core.data.repository.Query;

public class RoleController {
    private final RoleRepository repository;

    public RoleController(RoleRepository repository) {
        this.repository = repository;
    }

    public List<Role> index() {
        return repository.all();
    }

    public PaginatedResult<Role> index(Query<Role> query) {
        return repository.get(query);
    }
}
