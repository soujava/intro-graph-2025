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

public final class BookApp {

    private BookApp() {
    }

    public static void main(String[] args) {

        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            GraphTemplate graph = container.select(GraphTemplate.class).get();
            BookService service = container.select(BookService.class).get();

            service.save(Book.of("Effective Java"));
            service.save(Book.of("NoSQL Distilled"));
            service.save(Book.of("Migrating to Microservice Databases"));
            service.save(Book.of("The Shack"));


            Category software = service.save(Category.of("Software"));
            Category romance = service.save(Category.of("Romance"));

            Category java = service.save(Category.of("Java"));
            Category nosql = service.save(Category.of("NoSQL"));;
            Category microService = service.save(Category.of("Micro Service"));

            Book effectiveJava = service.save(Book.of("Effective Java"));
            Book nosqlDistilled = service.save(Book.of("NoSQL Distilled"));
            Book migratingMicroservice = service.save(Book.of("Migrating to Microservice Databases"));
            Book shack = service.save(Book.of("The Shack"));



            graph.edge(Edge.source(java).label("is").target(software).build());
            graph.edge(Edge.source(nosql).label("is").target(software).build());
            graph.edge(Edge.source(microService).label("is").target(software).build());

            graph.edge(Edge.source(effectiveJava).label("is").target(software).build());
            graph.edge(Edge.source(nosqlDistilled).label("is").target(software).build());
            graph.edge(Edge.source(migratingMicroservice).label("is").target(software).build());

            graph.edge(Edge.source(effectiveJava).label("is").target(java).build());
            graph.edge(Edge.source(nosqlDistilled).label("is").target(nosql).build());
            graph.edge(Edge.source(migratingMicroservice).label("is").target(microService).build());


            graph.edge(Edge.source(shack).label("is").target(romance).build());

        }
    }

}
