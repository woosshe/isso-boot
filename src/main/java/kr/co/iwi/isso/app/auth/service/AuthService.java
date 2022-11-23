package kr.co.iwi.isso.app.auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.iwi.isso.app.auth.mapper.AuthMapper;
import kr.co.iwi.isso.app.auth.vo.model.User;
import kr.co.iwi.isso.app.auth.vo.request.SigninRequest;

@Service
public class AuthService {

	@Autowired
	private AuthMapper authMapper;

	public List<User> getUserList(SigninRequest req) throws Exception {
		return authMapper.selectUserList(req);
	}

}
