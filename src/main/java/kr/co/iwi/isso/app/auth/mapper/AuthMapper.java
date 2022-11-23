package kr.co.iwi.isso.app.auth.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.iwi.isso.app.auth.vo.model.User;
import kr.co.iwi.isso.app.auth.vo.request.SigninRequest;

@Mapper
public interface AuthMapper {

	public List<User> selectUserList(SigninRequest req) throws Exception;

}
