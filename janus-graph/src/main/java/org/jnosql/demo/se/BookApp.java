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

import java.util.List;

public final class BookApp {

    private BookApp() {
    }

    public static void main(String[] args) {

        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            TinkerpopTemplate graph = container.select(TinkerpopTemplate.class).get();
            BookService service = container.select(BookService.class).get();

            Category software = service.save(Category.of("Software"));
            Category romance = service.save(Category.of("Romance"));

            Category java = service.save(Category.of("Java"));
            Category nosql = service.save(Category.of("NoSQL"));
            Category microService = service.save(Category.of("Micro Service"));

            Book effectiveJava = service.save(Book.of("Effective Java"));
            Book nosqlDistilled = service.save(Book.of("NoSQL Distilled"));
            Book migratingMicroservice = service.save(Book.of("Migrating to Microservice Databases"));
            Book shack = service.save(Book.of("The Shack"));


            graph.edge(java, "is", software);
            graph.edge(nosql, "is", software);
            graph.edge(microService, "is", software);

            graph.edge(effectiveJava, "is", software);
            graph.edge(nosqlDistilled, "is", software);
            graph.edge(migratingMicroservice, "is", software);

            graph.edge(effectiveJava, "is", java);
            graph.edge(nosqlDistilled, "is", nosql);
            graph.edge(migratingMicroservice, "is", microService);


            graph.edge(shack, "is", romance);


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


        }
    }
}
