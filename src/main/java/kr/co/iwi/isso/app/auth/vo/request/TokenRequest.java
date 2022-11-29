package kr.co.iwi.isso.app.auth.vo.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRequest {

	@JsonProperty("grant_type")
	private String grantType;

}
