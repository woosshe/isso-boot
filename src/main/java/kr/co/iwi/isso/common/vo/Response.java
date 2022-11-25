package kr.co.iwi.isso.common.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Response {

	private String code;
	private String message;

	public static Response success() {
		return new Response("000", null);
	}

	public static Response failure(Exception e) {
		return new Response("999", e.getMessage());
	}

	public static Response failure(String code) {
		return new Response(code, null);
	}

	public static Response failure(String code, String message) {
		return new Response(code, message);
	}
}
