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
import org.eclipse.jnosql.databases.tinkerpop.mapping.TinkerpopTemplate;
import org.eclipse.jnosql.mapping.graph.Edge;

import java.util.List;

public final class BookApp2 {

    private BookApp2() {
    }

    public static void main(String[] args) {

        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            TinkerpopTemplate graph = container.select(TinkerpopTemplate.class).get();
            BookService service = container.select(BookService.class).get();
            BookRepository repository = container.select(BookRepository.class).get();
            repository.findByName("Effective Java");
            Category software = service.save(Category.of("Software"));
            Category java = service.save(Category.of("Java"));
            Category architecture = service.save(Category.of("Architecture"));
            Category performance = service.save(Category.of("Performance"));

            Book effectiveJava = service.save(Book.of("Effective Java"));
            Book cleanArchitecture = service.save(Book.of("Clean Architecture"));
            Book systemDesign = service.save(Book.of("System Design Interview"));
            Book javaPerformance = service.save(Book.of("Java Performance"));


            graph.edge(Edge.source(effectiveJava).label("is").target(java).property("relevance", 10).build());
            graph.edge(Edge.source(effectiveJava).label("is").target(software).property("relevance", 9).build());
            graph.edge(Edge.source(cleanArchitecture).label("is").target(software).property("relevance", 8).build());
            graph.edge(Edge.source(cleanArchitecture).label("is").target(architecture).property("relevance", 10).build());
            graph.edge(Edge.source(systemDesign).label("is").target(architecture).property("relevance", 9).build());
            graph.edge(Edge.source(systemDesign).label("is").target(software).property("relevance", 7).build());
            graph.edge(Edge.source(javaPerformance).label("is").target(performance).property("relevance", 8).build());
            graph.edge(Edge.source(javaPerformance).label("is").target(java).property("relevance", 9).build());


            List<String> softwareCategories = graph.traversalVertex().hasLabel("Category")
                    .has("name", "Software")
                    .in("is").hasLabel("Category").<Category>result()
                    .map(Category::getName)
                    .toList();

            List<String> softwareBooks = graph.traversalVertex().hasLabel("Category")
                    .has("name", "Software")
                    .in("is").hasLabel("Book").<Book>result()
                    .map(Book::getName)
                    .toList();

            List<String> sofwareNoSQLBooks = graph.traversalVertex().hasLabel("Category")
                    .has("name", "Software")
                    .in("is")
                    .has("name", "NoSQL")
                    .in("is").<Book>result()
                    .map(Book::getName)
                    .toList();

            System.out.println("The software categories: " + softwareCategories);
            System.out.println("The software books: " + softwareBooks);
            System.out.println("The software and NoSQL books: " + sofwareNoSQLBooks);


            System.out.println("\n📚 Books in 'Architecture' category:");
            var architectureBooks = graph.gremlin("g.V().hasLabel('Category').has('name','Architecture').in('is')").toList();
            architectureBooks.forEach(doc -> System.out.println(" - " + doc));

            System.out.println("\n🔍 Categories with more than one book:");
            var commonCategories = graph.gremlin("g.V().hasLabel('Category').where(__.in('is').count().is(gt(1)))"
            ).toList();
            commonCategories.forEach(doc -> System.out.println(" - " + doc));

            var highRelevanceBooks = graph.gremlin( "g.E().hasLabel('is').has('relevance', gte(9))" +
                    ".outV().hasLabel('Book').dedup()").toList();

            System.out.println("\n📚 Books with high relevance:");
            highRelevanceBooks.forEach(doc -> System.out.println(" - " + doc));
        }
    }
}
