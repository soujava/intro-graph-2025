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
import org.eclipse.jnosql.mapping.graph.Edge;
import org.eclipse.jnosql.mapping.graph.GraphTemplate;

public final class BookApp2 {

    private BookApp2() {
    }

    public static void main(String[] args) {

        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            var template = container.select(GraphTemplate.class).get();
            var bookRepository = container.select(BookRepository.class).get();
            var repository = container.select(CategoryRepository.class).get();

            var software = repository.findByName("Software").orElseGet(() -> repository.save(Category.of("Software")));
            var java = repository.findByName("Java").orElseGet(() -> repository.save(Category.of("Java")));
            var architecture = repository.findByName("Architecture").orElseGet(() -> repository.save(Category.of("Architecture")));
            var performance = repository.findByName("Performance").orElseGet(() -> repository.save(Category.of("Performance")));

            var effectiveJava = bookRepository.findByName("Effective Java").orElseGet(() -> bookRepository.save(Book.of("Effective Java")));
            var cleanArchitecture = bookRepository.findByName("Clean Architecture").orElseGet(() -> bookRepository.save(Book.of("Clean Architecture")));
            var systemDesign = bookRepository.findByName("System Design Interview").orElseGet(() -> bookRepository.save(Book.of("System Design Interview")));
            var javaPerformance = bookRepository.findByName("Java Performance").orElseGet(() -> bookRepository.save(Book.of("Java Performance")));

            template.edge(Edge.source(effectiveJava).label("is").target(java).property("relevance", 10).build());
            template.edge(Edge.source(effectiveJava).label("is").target(software).property("relevance", 9).build());
            template.edge(Edge.source(cleanArchitecture).label("is").target(software).property("relevance", 8).build());
            template.edge(Edge.source(cleanArchitecture).label("is").target(architecture).property("relevance", 10).build());
            template.edge(Edge.source(systemDesign).label("is").target(architecture).property("relevance", 9).build());
            template.edge(Edge.source(systemDesign).label("is").target(software).property("relevance", 7).build());
            template.edge(Edge.source(javaPerformance).label("is").target(performance).property("relevance", 8).build());
            template.edge(Edge.source(javaPerformance).label("is").target(java).property("relevance", 9).build());

            System.out.println("\n📚 Books in 'Architecture' category:");
            var architectureBooks = bookRepository.findArchitectureBooks();
            architectureBooks.forEach(doc -> System.out.println(" - " + doc));

            System.out.println("\n🔍 Categories with more than one book:");
            var commonCategories = repository.commonCategories();
            commonCategories.forEach(doc -> System.out.println(" - " + doc));

            var highRelevanceBooks = bookRepository.highRelevanceBooks();

            System.out.println("\n📚 Books with high relevance:");
            highRelevanceBooks.forEach(doc -> System.out.println(" - " + doc));

            var bookByName = bookRepository.queryByName("Effective Java");
            System.out.println("\n📚 Book by name: " + bookByName);
        }
    }
}
