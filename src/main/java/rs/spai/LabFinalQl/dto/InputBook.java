package rs.spai.LabFinalQl.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputBook {
	
	//generated automatically by PostgreSQL

    private String title;
    private Integer publicationYear;
    private String language;
    private Integer nbPages;
    private int idCategory;
    private int idAuthor;
}
