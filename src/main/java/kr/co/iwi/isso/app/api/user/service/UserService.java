package kr.co.iwi.isso.app.api.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.iwi.isso.app.api.user.mapper.UserMapper;
import kr.co.iwi.isso.app.api.user.vo.model.User;
import kr.co.iwi.isso.app.api.user.vo.request.UserRequest;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;

	public List<User> getUserList(UserRequest req) throws Exception {
		return userMapper.selectUserList(req);
	}

}
