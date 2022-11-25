package kr.co.iwi.isso.app.auth.vo.response;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidResponse {

	@NotBlank
	private String acsToken;

	public ValidResponse(String acsToken) {
		this.acsToken = acsToken;
	}

}
