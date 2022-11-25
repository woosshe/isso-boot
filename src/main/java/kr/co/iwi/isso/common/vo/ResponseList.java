package kr.co.iwi.isso.common.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResponseList<T> {

	private String code;
	private String message;
	private List<T> data;

	public ResponseList() {

	}

	public ResponseList(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public ResponseList(List<T> data) {
		this.code = "000";
		this.data = data;
	}
}
