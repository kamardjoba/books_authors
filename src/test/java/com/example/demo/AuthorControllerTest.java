package com.example.demo;


import com.example.demo.model.Author;
import com.example.demo.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorRepository authorRepository;

    // Тест на создание автора
    @Test
    public void testCreateAuthor() throws Exception {
        String authorJson = "{\"name\": \"John Doe\"}";

        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    // Тест на получение всех авторов
    @Test
    public void testGetAllAuthors() throws Exception {
        Author author = new Author();
        author.setName("John Doe");
        authorRepository.save(author);

        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", hasItem("John Doe")));
    }
    // Тест на обновление автора
    @Test
    public void testUpdateAuthor() throws Exception {
        Author author = new Author();
        author.setName("John Doe");
        author = authorRepository.save(author);

        String updatedAuthorJson = "{\"name\": \"Jane Doe\"}";

        mockMvc.perform(put("/api/authors/" + author.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedAuthorJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Doe"));
    }

    // Тест на удаление автора
    @Test
    public void testDeleteAuthor() throws Exception {
        Author author = new Author();
        author.setName("John Doe");
        author = authorRepository.save(author);

        mockMvc.perform(delete("/api/authors/" + author.getId()))
                .andExpect(status().isOk());
    }
}