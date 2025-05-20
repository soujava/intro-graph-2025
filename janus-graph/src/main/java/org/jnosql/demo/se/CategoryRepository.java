package org.jnosql.demo.se;


import jakarta.data.repository.BasicRepository;
import jakarta.data.repository.Repository;
import org.eclipse.jnosql.databases.tinkerpop.mapping.Gremlin;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends BasicRepository<Category, Long> {

    Optional<Category> findByName(String name);

    @Gremlin("g.V().hasLabel('Category').where(__.in('is').count().is(gt(1)))")
    List<Category> commonCategories();
}
