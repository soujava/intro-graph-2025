package org.jnosql.demo.se;


import jakarta.data.repository.BasicRepository;
import jakarta.data.repository.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends BasicRepository<Book, Long> {

    Optional<Book> findByName(String name);
}
