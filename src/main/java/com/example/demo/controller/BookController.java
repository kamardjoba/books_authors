package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.model.Author;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.AuthorRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.example.demo.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books") // Базовый URL для книг
public class BookController {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookController(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }


    // Эндпоинт для получения всех книг
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Эндпоинт для создания новой книги
    @PostMapping
    public Book createBook(@RequestBody Book book) {
   book.setTitle("abc");
        return bookRepository.save(book);
    }

    @PostMapping("/{authorId}")
    public ResponseEntity<Book> createBookForAuthor(@PathVariable Long authorId, @RequestBody Book book) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + authorId));

        // Установите связь между книгой и автором
        book.setAuthor(author);

        // Сохраните книгу
        Book savedBook = bookRepository.save(book);

        return ResponseEntity.ok(savedBook);
    }
    // Эндпоинт для получения книг с авторами
    @GetMapping("/with-author")
    public List<Book> getBooksWithAuthors() {
        return bookRepository.findAll().stream()
                .filter(book -> book.getAuthor() != null)
                .toList();
    }

    // Эндпоинт для получения книг без авторов
    @GetMapping("/without-author")
    public List<Book> getBooksWithoutAuthors() {
        return bookRepository.findAll().stream()
                .filter(book -> book.getAuthor() == null)
                .toList();
    }
    @PutMapping("/{bookId}")
    public Book updateBook(@PathVariable Long bookId, @RequestBody Book updatedBook) {
        return bookRepository.findById(bookId)
                .map(book -> {
                    book.setTitle(updatedBook.getTitle());
                    return bookRepository.save(book);
                })
                .orElseThrow(() -> new RuntimeException("Book not found with id " + bookId));
    }
    @DeleteMapping("/api/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // Возвращаем статус 204
        } else {
            return ResponseEntity.notFound().build(); // Возвращаем статус 404, если книга не найдена
        }
    }
}
