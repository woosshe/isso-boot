package kr.co.iwi.isso.app.auth.vo.response;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SigninResponse {

	@NotBlank
	private String acsToken;

	@NotBlank
	private String refToken;

	public SigninResponse(String acsToken, String refToken) {
		this.acsToken = acsToken;
		this.refToken = refToken;
	}

}
