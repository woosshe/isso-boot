package kr.co.iwi.isso.exception;

public class UnauthorizedException extends Exception {

	private static final long serialVersionUID = 1L;

	public UnauthorizedException() {
	}

	public UnauthorizedException(String message) {
		super(message);
	}
}
