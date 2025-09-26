package com.library.librarysystem;

import com.library.librarysystem.model.Book;
import com.library.librarysystem.model.User;
import com.library.librarysystem.repository.BookRepository;
import com.library.librarysystem.repository.UserRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@CucumberContextConfiguration
@SpringBootTest
public class BookStepDefinitions {

    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private com.library.librarysystem.controller.BookController bookController;

    private Principal mockPrincipal;
    private User mockUser;

    @Given("I am a logged-in user with email {string}")
    public void i_am_a_logged_in_user_with_email(String email) {
        mockUser = new User();
        mockUser.setId("user123");
        mockUser.setEmail(email);

        mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
    }

    @When("I add a new book with title {string} and author {string}")
    public void i_add_a_new_book_with_title_and_author(String title, String author) {
        Book newBook = new Book();
        newBook.setTitle(title);
        newBook.setAuthor(author);
        bookController.createBook(newBook, mockPrincipal);
    }

    @Then("the new book should be saved for me")
    public void the_new_book_should_be_saved_for_me() {
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookArgumentCaptor.capture());

        Book savedBook = bookArgumentCaptor.getValue();
        assertEquals("The Lord of the Rings", savedBook.getTitle());
        assertEquals(mockUser.getId(), savedBook.getUserId());
    }
}