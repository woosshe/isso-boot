package kr.co.iwi.isso.app.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.iwi.isso.app.auth.service.AuthService;
import kr.co.iwi.isso.app.auth.vo.model.User;
import kr.co.iwi.isso.app.auth.vo.request.SigninRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/signin")
	public ResponseEntity<List<User>> signin(SigninRequest req) throws Exception {
		List<User> list = authService.getUserList(req);
		return new ResponseEntity<>(list, null, HttpStatus.OK);
	}

}
