package kr.co.iwi.isso.exception.advice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.co.iwi.isso.common.vo.Response;
import lombok.RequiredArgsConstructor;

import kr.co.iwi.isso.exception.IException;
import kr.co.iwi.isso.exception.TokenExpiredException;
import kr.co.iwi.isso.exception.UnauthorizedException;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;

@RequiredArgsConstructor
@RestControllerAdvice(annotations = RestController.class)
public class GlobalApiExceptionAdvice {

	/**
	 * header 누락
	 * 
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 */
	@ExceptionHandler(MissingRequestHeaderException.class)
	public ResponseEntity<Response> MissingRequestHeaderException(HttpServletRequest request,
			HttpServletResponse response, MissingRequestHeaderException e) {
		//e.printStackTrace();
		return new ResponseEntity<>(new Response("700", "헤더 누락"), null, HttpStatus.OK);
	}

	/**
	 * body 누락
	 * 
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Response> HttpMessageNotReadableException(HttpServletRequest request,
			HttpServletResponse response, HttpMessageNotReadableException e) {
		//e.printStackTrace();
		return new ResponseEntity<>(new Response("701", "파라미터 누락"), null, HttpStatus.OK);
	}

	/**
	 * NotBlank 누락
	 * 
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Response> MethodArgumentNotValidException(HttpServletRequest request,
			HttpServletResponse response, MethodArgumentNotValidException e) {
		//e.printStackTrace();
		return new ResponseEntity<>(new Response("702", "필수값 누락"), null, HttpStatus.OK);
	}

	/**
	 * 인증만료(토큰만료)
	 * 
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 */
	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<Response> TokenExpiredException(HttpServletRequest request, HttpServletResponse response,
			TokenExpiredException e) {
		//e.printStackTrace();
		// return new ResponseEntity<>(new Response("801", "인증 만료"), null,
		// HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<>(new Response("800", "인증 만료"), null, HttpStatus.OK);
	}

	/**
	 * 인증실패(권한없음)
	 * 
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 */
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<Response> UnauthorizedException(HttpServletRequest request, HttpServletResponse response,
			UnauthorizedException e) {
		//e.printStackTrace();
		// return new ResponseEntity<>(new Response("800", "인증 실패"), null,
		// HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<>(new Response("801", "인증 실패"), null, HttpStatus.OK);
	}

	/**
	 * 메세지 전달용 IException
	 * 
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 */
	@ExceptionHandler(IException.class)
	public ResponseEntity<Response> IException(HttpServletRequest request, HttpServletResponse response, IException e) {
		//e.printStackTrace();
		return new ResponseEntity<>(new Response("998", e.getMessage()), null, HttpStatus.OK);
	}

	/**
	 * Exception
	 * 
	 * @param request
	 * @param response
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response> Exception(HttpServletRequest request, HttpServletResponse response, Exception e) {
		//e.printStackTrace();
		return new ResponseEntity<>(new Response("999", "오류 발생"), null, HttpStatus.OK);
	}

}
