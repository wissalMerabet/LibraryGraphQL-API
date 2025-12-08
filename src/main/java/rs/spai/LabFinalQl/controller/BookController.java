package rs.spai.LabFinalQl.controller;

import java.util.ArrayList;

import java.util.List;


import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import rs.spai.LabFinalQl.model.Book;
import rs.spai.LabFinalQl.repository.BookRepository;
import rs.spai.LabFinalQl.service.DataLoaderService;



@Controller

public class BookController {

	private BookRepository bokRepo;
	private DataLoaderService categoryTree;

	public BookController(BookRepository bokRepo, DataLoaderService categoryTree) {
		super();
		this.bokRepo = bokRepo;
		this.categoryTree = categoryTree;
	}

	@QueryMapping
	public List<Book> books() {
		return bokRepo.findAll();
	}

	@QueryMapping
	public Book book(@Argument int id) {
		return bokRepo.findById(id).orElseThrow(() -> new RuntimeException("not found"));
	}

	@QueryMapping
	public ListWrapper listBooks(
	        @Argument Integer publicationYear,
	        @Argument String language,
	        @Argument Integer categoryId,
	        @Argument Boolean recursive,
	        @Argument Integer offset,
	        @Argument Integer limit) {

	    int safeOffset = (offset != null && offset >= 0) ? offset : 0;
	    int safeLimit = (limit != null && limit > 0) ? limit : 10;

	    List<Integer> categories = (categoryId != null)
	            ? ((recursive != null && recursive)
	                    ? categoryTree.getRecursiveIds(categoryId)
	                    : List.of(categoryId))
	            : List.of();

	    // FIX: Collect into a mutable list
	    List<Book> books = bokRepo.findAll().stream()
	            .filter(b -> publicationYear == null || b.getPublicationYear().equals(publicationYear))
	            .filter(b -> language == null || b.getLanguage().equalsIgnoreCase(language))
	            .filter(b -> categories.isEmpty() ||
	                    (b.getCategory() != null && categories.contains(b.getCategory().getIdC())))
	            .skip(safeOffset)
	            .limit(safeLimit + 1)
	            .collect(java.util.stream.Collectors.toList()); // <-- FIXED

	    boolean hasMore = books.size() > safeLimit;

	    if (hasMore) {
	        books.remove(books.size() - 1); // safe now
	    }

	    return new ListWrapper(books, hasMore);
	}
	
	@QueryMapping
	public ListWrapper listBooksByAuthor(
	        @Argument Integer authorId,
	        @Argument Integer offset,
	        @Argument Integer limit) {

	    int safeOffset = (offset != null && offset >= 0) ? offset : 0;
	    int safeLimit = (limit != null && limit > 0) ? limit : 10;

	    List<Book> books = bokRepo.findAll().stream()
	            .filter(b -> authorId == null || b.getAuthor().getId().equals(Long.valueOf(authorId)))
	            .skip(safeOffset)
	            .limit(safeLimit + 1) 
	            .toList();

	    boolean hasMore = books.size() > safeLimit;

	    if (hasMore) {
	        books = new ArrayList<>(books);
	        books.remove(books.size() - 1);
	    }

	    return new ListWrapper(books, hasMore);
	}



	

	public static class ListWrapper {

		private final List<Book> list;
		private final boolean hasMore;

		public ListWrapper(List<Book> list, boolean hasMore) {
			this.list = list;
			this.hasMore = hasMore;
		}

		public List<Book> getList() {
			return list;
		}

		public boolean isHasMore() {
			return hasMore;
		}
	}
}
