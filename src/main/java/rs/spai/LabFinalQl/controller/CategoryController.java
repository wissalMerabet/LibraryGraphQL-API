package rs.spai.LabFinalQl.controller;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;


import rs.spai.LabFinalQl.model.Category;
import rs.spai.LabFinalQl.repository.CategoryRepository;

@Controller

public class CategoryController {

	private CategoryRepository catRepo;

	public CategoryController(CategoryRepository catRepo) {
		super();
		this.catRepo = catRepo;
	}
	
	@QueryMapping
    public List<Category> categories() {
        return catRepo.findAll();
    }
	
	@QueryMapping
    public Category category(@Argument int id) {
        return catRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("not found"));
    }
}
