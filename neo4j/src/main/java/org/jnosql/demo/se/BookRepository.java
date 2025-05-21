package org.jnosql.demo.se;


import jakarta.data.repository.Param;
import jakarta.data.repository.Repository;
import org.eclipse.jnosql.databases.neo4j.mapping.Cypher;
import org.eclipse.jnosql.databases.neo4j.mapping.Neo4JRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends Neo4JRepository<Book, Long> {

    Optional<Book> findByName(String name);

    @Cypher("g.V().hasLabel('Book').out('is').hasLabel('Category').has('name','Architecture').in('is').dedup()")
    List<Book> findArchitectureBooks();

    @Cypher("g.E().hasLabel('is').has('relevance', gte(9)).outV().hasLabel('Book').dedup()")
    List<Book> highRelevanceBooks();

    @Cypher("g.V().hasLabel('Book').has('name', @name)")
    List<Book> queryByName(@Param("name") String name);
}
