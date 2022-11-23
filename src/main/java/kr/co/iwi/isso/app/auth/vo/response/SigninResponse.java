package kr.co.iwi.isso.app.auth.vo.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SigninResponse {
	private String acsToken;
	private String refToken;
}
