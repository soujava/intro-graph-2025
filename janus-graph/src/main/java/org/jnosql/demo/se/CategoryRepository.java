package org.jnosql.demo.se;


import jakarta.data.repository.BasicRepository;
import jakarta.data.repository.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends BasicRepository<Category, Long> {

    Optional<Category> findByName(String name);
}
