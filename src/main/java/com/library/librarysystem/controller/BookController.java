package com.library.librarysystem.controller;

import com.library.librarysystem.model.Book;
import com.library.librarysystem.model.User; // Import the User model
import com.library.librarysystem.repository.BookRepository;
import com.library.librarysystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    private String getUserId(Principal principal) {
        // Find the user by email (which is the principal's name) and get their ID
        Optional<User> userOptional = userRepository.findByEmail(principal.getName());
        return userOptional.map(User::getId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + principal.getName()));
    }

    @GetMapping
    public List<Book> getAllBooksForUser(Principal principal) {
        String userId = getUserId(principal);
        return bookRepository.findByUserId(userId);
    }

    @PostMapping
    public Book createBook(@RequestBody Book book, Principal principal) {
        String userId = getUserId(principal);
        book.setUserId(userId); // Set the owner of the book
        return bookRepository.save(book);
    }

    @PutMapping("/{id}")
    // The return type is changed to ResponseEntity<?> to handle both success and error responses.
    public ResponseEntity<?> updateBook(@PathVariable String id, @RequestBody Book bookDetails, Principal principal) {
        String userId = getUserId(principal);
        return bookRepository.findById(id)
                .map(book -> {
                    if (!book.getUserId().equals(userId)) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to edit this book.");
                    }
                    book.setTitle(bookDetails.getTitle());
                    book.setAuthor(bookDetails.getAuthor());
                    book.setIsbn(bookDetails.getIsbn());
                    book.setGenre(bookDetails.getGenre());
                    book.setPublicationYear(bookDetails.getPublicationYear());
                    Book updatedBook = bookRepository.save(book);
                    return ResponseEntity.ok(updatedBook);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable String id, Principal principal) {
        String userId = getUserId(principal);
        return bookRepository.findById(id)
                .map(book -> {
                    if (!book.getUserId().equals(userId)) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to delete this book.");
                    }
                    bookRepository.delete(book);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}