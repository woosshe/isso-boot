package kr.co.iwi.isso.app.auth.vo.response;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponse {

	@NotBlank
	private String acsToken;

	public TokenResponse(String acsToken) {
		this.acsToken = acsToken;
	}

}
