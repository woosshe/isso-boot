package kr.co.iwi.isso.common.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResponseData<T> {

	private String code;
	private String message;
	private T data;

	public ResponseData() {

	}

	public ResponseData(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public ResponseData(T data) {
		this.code = "000";
		this.data = data;
	}
}
