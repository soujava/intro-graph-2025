package org.jnosql.demo.se;


import jakarta.data.repository.Repository;
import org.eclipse.jnosql.databases.tinkerpop.mapping.Gremlin;
import org.eclipse.jnosql.databases.tinkerpop.mapping.TinkerPopRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends TinkerPopRepository<Category, Long> {

    Optional<Category> findByName(String name);

    @Gremlin("g.V().hasLabel('Category').where(__.in('is').count().is(gt(1)))")
    List<Category> commonCategories();
}
