package org.jnosql.demo.se;


import jakarta.data.repository.Repository;
import org.eclipse.jnosql.databases.neo4j.mapping.Cypher;
import org.eclipse.jnosql.databases.neo4j.mapping.Neo4JRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends Neo4JRepository<Category, Long> {

    Optional<Category> findByName(String name);

    @Cypher("g.V().hasLabel('Category').where(__.in('is').count().is(gt(1)))")
    List<Category> commonCategories();
}
