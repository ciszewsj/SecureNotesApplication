package ee.ciszewsj.secureapplication.data;


import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CreateNoteRequest {
	@NotEmpty
	@Size(min = 2, max = 64)
	private String name;
	@NotEmpty
	@Size(min = 1, max = 4096)
	private String note;
	@Size(max = 32)
	private String password;
}
