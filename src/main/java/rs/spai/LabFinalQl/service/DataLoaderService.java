package rs.spai.LabFinalQl.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import rs.spai.LabFinalQl.model.Category;
import rs.spai.LabFinalQl.repository.CategoryRepository;

@Service
public class DataLoaderService {

	private CategoryRepository catRepo;
	
	

    public DataLoaderService(CategoryRepository catRepo) {
		super();
		this.catRepo = catRepo;
	}

	public List<Integer> getRecursiveIds(Integer categoryId) {

        List<Integer> result = new ArrayList<>();
        result.add(categoryId);   

        collectChildren(categoryId, result);

        return result;
    }

    private void collectChildren(Integer parentId, List<Integer> result) {

        List<Category> children = catRepo.findByParentCategory_IdC(parentId);

        for (Category child : children) {
            result.add(child.getIdC());
            collectChildren(child.getIdC(), result);
        }
    }
}
