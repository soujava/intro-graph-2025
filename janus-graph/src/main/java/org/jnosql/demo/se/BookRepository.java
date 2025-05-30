package org.jnosql.demo.se;


import jakarta.data.repository.Param;
import jakarta.data.repository.Repository;
import org.eclipse.jnosql.databases.tinkerpop.mapping.Gremlin;
import org.eclipse.jnosql.databases.tinkerpop.mapping.TinkerPopRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends TinkerPopRepository<Book, Long> {

    Optional<Book> findByName(String name);

    @Gremlin("g.V().hasLabel('Book').out('is').hasLabel('Category').has('name','Architecture').in('is').dedup()")
    List<Book> findArchitectureBooks();

    @Gremlin("g.E().hasLabel('is').has('relevance', gte(9)).outV().hasLabel('Book').dedup()")
    List<Book> highRelevanceBooks();

    @Gremlin("g.V().hasLabel('Book').has('name', @name)")
    List<Book> queryByName(@Param("name") String name);
}
