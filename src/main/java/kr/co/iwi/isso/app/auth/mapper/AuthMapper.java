package kr.co.iwi.isso.app.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.iwi.isso.app.auth.vo.model.User;

@Mapper
public interface AuthMapper {

	public User selectUserInfo(@Param("email") String email) throws Exception;

	public int updateUserLoginFail(User req) throws Exception;

	public int updateUserSignin(User req) throws Exception;

	public User selectUserInfoByToken(@Param("refToken") String refToken, @Param("refIssueIp") String refIssueIp)
			throws Exception;

}
