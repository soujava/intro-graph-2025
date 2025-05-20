package org.jnosql.demo.se;


import jakarta.data.repository.Repository;
import org.eclipse.jnosql.databases.tinkerpop.mapping.Gremlin;
import org.eclipse.jnosql.databases.tinkerpop.mapping.TinkerPopRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends TinkerPopRepository<Book, Long> {

    Optional<Book> findByName(String name);

    @Gremlin("g.V().hasLabel('Book').out('is').hasLabel('Category').has('name','Architecture').in('is')")
    List<Book> findArchitectureBooks();

    @Gremlin("\"g.E().hasLabel('is').has('relevance', gte(9))\" +\n" +
            "                    \".outV().hasLabel('Book').dedup()\"")
    List<Book> highRelevanceBooks();
}
