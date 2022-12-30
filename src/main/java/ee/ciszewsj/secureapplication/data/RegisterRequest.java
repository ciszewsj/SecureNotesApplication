package ee.ciszewsj.secureapplication.data;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class RegisterRequest {
	@NotEmpty
	@Size(min = 4, max = 32)
	private String username;
	@NotEmpty
	@Email
	private String email;
	@NotEmpty
	@Size(min = 4, max = 32)
	private String password;
}
