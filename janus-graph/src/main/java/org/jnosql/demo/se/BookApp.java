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

import java.util.Collections;
import java.util.List;

public final class BookApp {

    private BookApp() {
    }

    public static void main(String[] args) {

        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            var template = container.select(TinkerpopTemplate.class).get();
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


            List<String> softwareCategories = template.traversalVertex().hasLabel("Category")
                    .has("name", "Software")
                    .in("is").hasLabel("Category").<Category>result()
                    .map(Category::getName)
                    .toList();

            List<String> softwareBooks = template.traversalVertex().hasLabel("Category")
                    .has("name", "Software")
                    .in("is").hasLabel("Book").<Book>result()
                    .map(Book::getName)
                    .toList();

            List<String> sofwareNoSQLBooks = template.traversalVertex().hasLabel("Category")
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
            var architectureBooks = template.gremlin("g.V().hasLabel('Category').has('name','Architecture').in('is')").toList();
            architectureBooks.forEach(doc -> System.out.println(" - " + doc));

            System.out.println("\n🔍 Categories with more than one book:");
            var commonCategories = template.gremlin("g.V().hasLabel('Category').where(__.in('is').count().is(gt(1)))"
            ).toList();
            commonCategories.forEach(doc -> System.out.println(" - " + doc));

            var highRelevanceBooks = template.gremlin( "g.E().hasLabel('is').has('relevance', gte(9))" +
                    ".outV().hasLabel('Book').dedup()").toList();

            System.out.println("\n📚 Books with high relevance:");
            highRelevanceBooks.forEach(doc -> System.out.println(" - " + doc));

            System.out.println("\n📚 Books with name: 'Effective Java':");
            var effectiveJavaBooks = template.gremlin("g.V().hasLabel('Book').has('name', @name)", Collections.singletonMap("name", "Effective Java")).toList();
            effectiveJavaBooks.forEach(doc -> System.out.println(" - " + doc));
        }
    }
}
