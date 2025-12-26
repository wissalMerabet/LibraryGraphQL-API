package rs.spai.LabFinalQl.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import rs.spai.LabFinalQl.model.Author;


import rs.spai.LabFinalQl.repository.AuthorRepository;
import rs.spai.LabFinalQl.repository.BookRepository;
import rs.spai.LabFinalQl.repository.CategoryRepository;

import rs.spai.LabFinalQl.enums.SearchType;

@Controller
public class AuthorController {

    private  AuthorRepository autRepo;
    private  BookRepository bokRepo;
    private  CategoryRepository catRepo;

    public AuthorController(AuthorRepository autRepo, BookRepository bokRepo, CategoryRepository catRepo) {
        this.autRepo = autRepo;
        this.bokRepo = bokRepo;
        this.catRepo = catRepo;
    }

    @QueryMapping
    public List<Author> authors() {
        return autRepo.findAll();
    }

    @QueryMapping
    public Author author(@Argument int id) {
        return autRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("not found"));
    }

    @QueryMapping
    public List<Object> search(
            @Argument String keyword,
            @Argument SearchType type,
            @Argument Integer offset,
            @Argument Integer limit) {

        String kw = (keyword == null) ? "" : keyword.toLowerCase();
        int safeOffset = (offset != null && offset >= 0) ? offset : 0;
        int safeLimit = (limit != null && limit > 0) ? limit : 10;
        if (type == null) type = SearchType.ALL;

        List<Object> results = new ArrayList<>();

        // Search Books
        if (type == SearchType.ALL || type == SearchType.BOOK) {
            results.addAll(
                bokRepo.findAll().stream()
                       .filter(b -> b.getTitle().toLowerCase().contains(kw))
                       .toList()
            );
        }

        // Search Categories
        if (type == SearchType.ALL || type == SearchType.CATEGORY) {
            results.addAll(
                catRepo.findAll().stream()
                       .filter(c -> c.getCategoryName().toLowerCase().contains(kw))
                       .toList()
            );
        }

        // Search Authors
        if (type == SearchType.ALL || type == SearchType.AUTHOR) {
            results.addAll(
                autRepo.findAll().stream()
                       .filter(a -> a.getName().toLowerCase().contains(kw))
                       .toList()
            );
        }

        // Apply pagination manually
        return results.stream()
                      .skip(safeOffset)
                      .limit(safeLimit)
                      .toList();
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @MutationMapping
    public Boolean deleteAuthor(@Argument int id) {

        Author author = autRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Author not found"));

        autRepo.delete(author);

        return true;
    }


}
