package kr.co.iwi.isso.app.auth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.iwi.isso.app.auth.service.AuthService;
import kr.co.iwi.isso.app.auth.vo.model.User;
import kr.co.iwi.isso.app.auth.vo.request.SigninRequest;
import kr.co.iwi.isso.app.auth.vo.response.SigninResponse;
import kr.co.iwi.isso.common.Const;
import kr.co.iwi.isso.common.vo.ResponseData;
import kr.co.iwi.isso.exception.IException;
import kr.co.iwi.isso.exception.UnauthorizedException;
import kr.co.iwi.isso.util.RequestUtil;
import kr.co.iwi.isso.util.SecureUtil;
import kr.co.iwi.isso.util.TokenUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/signin")
	public ResponseData<SigninResponse> signin(@Valid @RequestBody SigninRequest req, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		User user = authService.getUserInfo(req.getEmail());
		if (user == null) {
			throw new IException("존재하지 않는 사용자");
		}

		if ("N".equals(user.getUseYn())) {
			throw new IException("사용 중지된 사용자");
		}

		if ("Y".equals(user.getDelYn())) {
			throw new IException("삭제된 사용자");
		}

		if (user.getLoginFailCnt() >= 5) {
			throw new IException("로그인 실패 5회");
		}

		String bodyPassword = SecureUtil.getEncPassword(req);
		String userPassword = user.getPassword();
		if (bodyPassword != null && !bodyPassword.equals(userPassword)) {
			authService.setUserLoginFail(user);
			throw new IException("비밀번호 틀림");
		}

		String acsToken = TokenUtil.createAccessToken(user.getEmail());
		String refToken = TokenUtil.createRefreshToken();

		user.setRefToken(refToken);
		user.setLastLoginIp(RequestUtil.getRemoteIp(request));

		authService.setUserSignin(user);

		response.addCookie(TokenUtil.makeCookie(Const.COOKIE_NAME_ACS, acsToken));
		response.addCookie(TokenUtil.makeCookie(Const.COOKIE_NAME_REF, refToken));

		request.setAttribute(Const.COOKIE_NAME_ACS, acsToken);
		request.setAttribute(Const.COOKIE_NAME_REF, refToken);

		return new ResponseData<>(new SigninResponse(acsToken, refToken));
	}

	// @PostMapping("/valid")
	// public ResponseData<ValidResponse> valid(
	// @CookieValue(required = false, name = Const.COOKIE_NAME_ACS) String acsToken,
	// @CookieValue(required = false, name = Const.COOKIE_NAME_REF) String refToken, HttpServletResponse response)
	// throws Exception {
	//
	// System.out.println("===== autchCheck =========================");
	// System.out.println(acsToken);
	// System.out.println(refToken);
	// System.out.println("==========================================");
	//
	// boolean isEmptyAcsToken = StringUtils.isEmpty(acsToken);
	// boolean isEmptyRefToken = StringUtils.isEmpty(refToken);
	// boolean isExpiredAcsToken = true;
	// boolean isExpiredRefToken = true;
	//
	// if (isEmptyAcsToken && isEmptyRefToken) {
	// throw new IException("토큰 없음");
	// }
	//
	// if (!isEmptyAcsToken) {
	// isExpiredAcsToken = TokenUtil.isExpired(acsToken);
	// }
	//
	// if (!isEmptyRefToken) {
	// isExpiredRefToken = TokenUtil.isExpired(refToken);
	// }
	//
	// if (isExpiredAcsToken && isExpiredRefToken) {
	// throw new IException("인증 만료");
	// }
	//
	// String email = null;
	// if (isExpiredAcsToken) {
	// // 엑세스 토큰 만료, 리프레시 토큰 검증
	//
	// if (isEmptyRefToken) {
	// // 리프레시 토큰 누락
	// throw new IException("인증 만료");
	// }
	//
	// if (isExpiredRefToken) {
	// // 리프레시 토큰 만료
	// throw new IException("인증 만료");
	// }
	//
	// User user = authService.getUserInfoByToken(refToken);
	// if (user == null) {
	// // 리프레시 토큰 검증 실패
	// throw new IException("인증 실패");
	// }
	//
	// email = user.getEmail();
	// } else {
	// // 엑세스 토큰 갱신
	//
	// email = TokenUtil.getSubject(acsToken);
	// }
	//
	// if (StringUtils.isEmpty(email)) {
	// throw new IException("인증 실패");
	// }
	//
	// String newAcsToken = TokenUtil.createAccessToken(email);
	// response.addCookie(TokenUtil.makeCookie(Const.COOKIE_NAME_ACS, newAcsToken));
	//
	// return new ResponseData<>(new ValidResponse(newAcsToken));
	// }

	@PostMapping("/me")
	public ResponseData<User> me(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String acsToken = (String) request.getAttribute(Const.COOKIE_NAME_ACS);

		String email = TokenUtil.getSubject(acsToken);
		if (StringUtils.isEmpty(email)) {
			throw new UnauthorizedException();
		}

		User user = authService.getUserInfo(email);
		if (user == null) {
			throw new UnauthorizedException();
		}

		return new ResponseData<>(user);
	}
}
