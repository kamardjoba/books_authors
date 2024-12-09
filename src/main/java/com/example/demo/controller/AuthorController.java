package com.example.demo.controller;

import com.example.demo.model.Author;
import com.example.demo.repository.AuthorRepository;
import org.springframework.web.bind.annotation.*;
import com.example.demo.exception.ResourceNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/authors") // Базовый URL для авторов
public class AuthorController {


    private final AuthorRepository authorRepository;

    @PutMapping("/{id}")
    public Author updateAuthor(@PathVariable Long id, @RequestBody Author authorDetails) {
        return authorRepository.findById(id).map(author -> {
            author.setName(authorDetails.getName());
            return authorRepository.save(author);
        }).orElseThrow(() -> new ResourceNotFoundException("Автор с ID " + id + " не найден."));
    }

    // Удаление автора
    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable Long id) {
        authorRepository.findById(id).ifPresentOrElse(
                authorRepository::delete,
                () -> { throw new ResourceNotFoundException("Автор с ID " + id + " не найден."); }
        );
    }

    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    // Эндпоинт для получения всех авторов
    @GetMapping
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    // Эндпоинт для создания нового автора
    @PostMapping
    public Author createAuthor(@RequestBody Author author) {
        return authorRepository.save(author);
    }

}
