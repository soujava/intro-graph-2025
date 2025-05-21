/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 * You may elect to redistribute this code under either of these licenses.
 */
package org.jnosql.demo.se;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import org.eclipse.jnosql.databases.neo4j.mapping.Neo4JTemplate;
import org.eclipse.jnosql.mapping.graph.Edge;
import org.eclipse.jnosql.mapping.graph.GraphTemplate;

import java.util.Collections;

public final class BookApp {

    private BookApp() {
    }

    public static void main(String[] args) {

        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            var template = container.select(Neo4JTemplate.class).get();
            var service = container.select(BookService.class).get();

            var software = service.save(Category.of("Software"));
            var java = service.save(Category.of("Java"));
            var architecture = service.save(Category.of("Architecture"));
            var performance = service.save(Category.of("Performance"));

            var effectiveJava = service.save(Book.of("Effective Java"));
            var cleanArchitecture = service.save(Book.of("Clean Architecture"));
            var systemDesign = service.save(Book.of("System Design Interview"));
            var javaPerformance = service.save(Book.of("Java Performance"));

            template.edge(Edge.source(effectiveJava).label("is").target(java).property("relevance", 10).build());
            template.edge(Edge.source(effectiveJava).label("is").target(software).property("relevance", 9).build());
            template.edge(Edge.source(cleanArchitecture).label("is").target(software).property("relevance", 8).build());
            template.edge(Edge.source(cleanArchitecture).label("is").target(architecture).property("relevance", 10).build());
            template.edge(Edge.source(systemDesign).label("is").target(architecture).property("relevance", 9).build());
            template.edge(Edge.source(systemDesign).label("is").target(software).property("relevance", 7).build());
            template.edge(Edge.source(javaPerformance).label("is").target(performance).property("relevance", 8).build());
            template.edge(Edge.source(javaPerformance).label("is").target(java).property("relevance", 9).build());

            System.out.println("\n📚 Books in 'Architecture' category:");
            var architectureBooks = template.cypher(  "MATCH (b:Book)-[:is]->(c:Category {name: 'Architecture'}) RETURN b AS book", Collections.emptyMap()).toList();
            architectureBooks.forEach(doc -> System.out.println(" - " + doc));

            System.out.println("\n🔍 Categories with more than one book:");
            var commonCategories = template.cypher( "MATCH (b:Book)-[:is]->(c:Category) " +
                            "WITH c, count(b) AS total " +
                            "WHERE total > 1 " +
                            "RETURN c"
                    , Collections.emptyMap()).toList();
            commonCategories.forEach(doc -> System.out.println(" - " + doc));

            var highRelevanceBooks = template.cypher(   "MATCH (b:Book)-[r:is]->(:Category) " +
                    "WHERE r.relevance >= 9 " +
                    "RETURN DISTINCT b", Collections.emptyMap()).toList();

            System.out.println("\n📚 Books with high relevance:");
            highRelevanceBooks.forEach(doc -> System.out.println(" - " + doc));

            System.out.println("\n📚 Books with name: 'Effective Java':");
            var effectiveJavaBooks = template.cypher("MATCH (b:Book {name: $name}) RETURN b", Collections.singletonMap("name", "Effective Java")
            ).toList();
            effectiveJavaBooks.forEach(doc -> System.out.println(" - " + doc));
        }
    }

}
