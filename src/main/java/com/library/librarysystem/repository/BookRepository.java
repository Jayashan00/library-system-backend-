package com.library.librarysystem.repository;

import com.library.librarysystem.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {
    // This is the correct location for this method.
    List<Book> findByUserId(String userId);
}