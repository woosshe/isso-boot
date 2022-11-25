package kr.co.iwi.isso.util;

import java.math.BigInteger;
import java.security.MessageDigest;

import org.apache.commons.lang3.StringUtils;

import kr.co.iwi.isso.app.auth.vo.request.SigninRequest;

public class SecureUtil {

	/**
	 * 암호화된 비밀번호 코드 반환
	 * 
	 * @param imap
	 * @return
	 * @throws Exception
	 */
	public static String getEncPassword(SigninRequest req) throws Exception {
		if (req == null) {
			return null;
		}
		return getEncPassword(req.getEmail(), req.getPassword());
	}

	/**
	 * 암호화된 비밀번호 코드 반환
	 * 
	 * @param email
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static String getEncPassword(String email, String password) throws Exception {
		if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
			return null;
		}

		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(email.getBytes());
		md.update(password.getBytes());
		String hex = String.format("%0128x", new BigInteger(1, md.digest()));

		return hex;
	}

}
