package kr.co.iwi.isso.app.auth.vo.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SigninRequest {

	@NotBlank
	private String email;

	@NotBlank
	private String password;

	public SigninRequest() {
	}

	public SigninRequest(String email) {
		this.email = email;
	}

}
