package kr.co.iwi.isso.app.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.iwi.isso.app.auth.mapper.AuthMapper;
import kr.co.iwi.isso.app.auth.vo.model.User;

@Service
public class AuthService {

	@Autowired
	private AuthMapper authMapper;

	public User getUserInfo(String email) throws Exception {
		return authMapper.selectUserInfo(email);
	}

	public int setUserLoginFail(User req) throws Exception {
		return authMapper.updateUserLoginFail(req);
	}

	public int setUserSignin(User req) throws Exception {
		return authMapper.updateUserSignin(req);
	}

	public User getUserInfoByToken(String refToken) throws Exception {
		return authMapper.selectUserInfoByToken(refToken);
	}

}
