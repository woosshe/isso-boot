package kr.co.iwi.isso.app.auth.vo.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidRequest {

	@NotBlank
	private String refToken;

	public ValidRequest(String refToken) {
		this.refToken = refToken;
	}

}
