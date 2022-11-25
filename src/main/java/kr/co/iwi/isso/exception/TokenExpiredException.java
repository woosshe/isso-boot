package kr.co.iwi.isso.exception;

public class TokenExpiredException extends Exception {

	private static final long serialVersionUID = 1L;

	public TokenExpiredException() {
	}

	public TokenExpiredException(String message) {
		super(message);
	}
}
