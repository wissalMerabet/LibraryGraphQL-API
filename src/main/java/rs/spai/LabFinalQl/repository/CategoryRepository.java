package rs.spai.LabFinalQl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.spai.LabFinalQl.model.Category;



public interface CategoryRepository extends JpaRepository<Category,Integer>{
	
	List<Category> findByParentCategory_IdC(Integer parentId);


}
