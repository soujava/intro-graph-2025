package org.jnosql.demo.se;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.jnosql.databases.neo4j.mapping.Neo4JTemplate;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class BookService {


    private static final Logger LOGGER = Logger.getLogger(BookService.class.getName());

    @Inject
    private Neo4JTemplate template;

    public Book save(Book book) {
        LOGGER.info("Saving book: " + book);
        Optional<Book> foundBook = template.select(Book.class).where("name").eq(book.getName()).<Book>singleResult();
        return foundBook.orElseGet(() -> {
            LOGGER.info("Book not found, inserting new book: " + book);
            return template.insert(book);
        });
    }

    public Category save(Category category) {
        LOGGER.info("Saving category: " + category);
        Optional<Category> foundCategory = template.select(Category.class).where("name").eq(category.getName()).<Category>singleResult();
        return foundCategory.orElseGet(() -> {
            LOGGER.info("Category not found, inserting new category: " + category);
            return template.insert(category);
        });
    }
}
