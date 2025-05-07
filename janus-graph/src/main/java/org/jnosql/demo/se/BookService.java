package org.jnosql.demo.se;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.jnosql.databases.tinkerpop.mapping.TinkerpopTemplate;

import java.util.Optional;

@ApplicationScoped
public class BookService {

    @Inject
    private TinkerpopTemplate template;

    public Book save(Book book) {
        Optional<Book> foundBook = template.select(Book.class).where("name").eq(book.getName()).<Book>singleResult();
        return foundBook.orElseGet(() -> template.insert(book));
    }

    public Category save(Category category) {
        Optional<Category> foundCategory = template.select(Category.class).where("name").eq(category.getName()).<Category>singleResult();
        return foundCategory.orElseGet(() -> template.insert(category));
    }
}
