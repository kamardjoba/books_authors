package com.example.demo;

import com.example.demo.model.Author;
import com.example.demo.model.Book;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

   // Тест на создание книги
    @Test
    public void testCreateBook() throws Exception {
        String bookJson = "{\"title\": \"New Book\"}";

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Book"));
    }

    // Тест на добавление книги автору
    @Test
    public void testCreateBookForAuthor() throws Exception {
        Author author = new Author();
        author.setName("Test Author");
        authorRepository.save(author);

        mockMvc.perform(post("/api/books/" + author.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Book for Author\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Book for Author"));
    }
    // Тест на получение всех книг
    @Test
    public void testGetAllBooks() throws Exception {
        Book book = new Book();
        book.setTitle("Test Book");
        bookRepository.save(book);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Book"));
    }


    // Тест на удаление книги
    @Test
    public void testDeleteBook() throws Exception {
        // Добавляем новую книгу
        Book book = new Book();
        book.setTitle("Test Book");
        bookRepository.save(book);

        // Получаем ID последней добавленной книги
        Long bookId = book.getId();

        // Выполняем DELETE-запрос
        mockMvc.perform(delete("/api/books/api/books/" + bookId))
                .andExpect(status().isNoContent());
    }


}