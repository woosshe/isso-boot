package kr.co.iwi.isso.app.api.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.iwi.isso.app.api.user.service.UserService;
import kr.co.iwi.isso.app.api.user.vo.model.User;
import kr.co.iwi.isso.app.api.user.vo.request.UserRequest;
import kr.co.iwi.isso.common.vo.ResponseList;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/list")
	public ResponseList<User> list(@RequestBody(required = false) UserRequest req) throws Exception {
		return new ResponseList<>(userService.getUserList(req));
	}
}
