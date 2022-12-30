package ee.ciszewsj.secureapplication.data;

import lombok.Data;

@Data
public class LoginRequest {
	private String email;
	private String password;
}
