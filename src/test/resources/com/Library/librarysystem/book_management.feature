Feature: Book Management
  As a logged-in user, I want to manage my book collection.

  Scenario: Adding a new book to my collection
    Given I am a logged-in user with email "user@example.com"
    When I add a new book with title "The Lord of the Rings" and author "J.R.R. Tolkien"
    Then the new book should be saved for me