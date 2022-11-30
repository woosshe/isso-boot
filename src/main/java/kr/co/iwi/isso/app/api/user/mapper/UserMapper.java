package kr.co.iwi.isso.app.api.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.iwi.isso.app.api.user.vo.model.User;
import kr.co.iwi.isso.app.api.user.vo.request.UserRequest;

@Mapper
public interface UserMapper {

	public List<User> selectUserList(UserRequest req) throws Exception;

}
