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

    @Cypher("MATCH (b:Book)-[:is]->(c:Category {name: 'Architecture'}) RETURN DISTINCT b")
    List<Book> findArchitectureBooks();

    @Cypher("MATCH (b:Book)-[r:is]->(:Category) WHERE r.relevance >= 9 RETURN DISTINCT b")
    List<Book> highRelevanceBooks();

    @Cypher("MATCH (b:Book {name: $name}) RETURN b")
    List<Book> queryByName(@Param("name") String name);
}
