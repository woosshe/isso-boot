package kr.co.iwi.isso.app.auth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.iwi.isso.app.auth.service.AuthService;
import kr.co.iwi.isso.app.auth.vo.model.User;
import kr.co.iwi.isso.app.auth.vo.request.SigninRequest;
import kr.co.iwi.isso.app.auth.vo.request.TokenRequest;
import kr.co.iwi.isso.app.auth.vo.response.SigninResponse;
import kr.co.iwi.isso.app.auth.vo.response.TokenResponse;
import kr.co.iwi.isso.common.Const;
import kr.co.iwi.isso.common.vo.ResponseData;
import kr.co.iwi.isso.exception.IException;
import kr.co.iwi.isso.exception.TokenExpiredException;
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
	public ResponseData<SigninResponse> signin(HttpServletRequest request, @Valid @RequestBody SigninRequest req)
			throws Exception {

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
		user.setRefIssueIp(String.valueOf(RequestUtil.getRemoteIp(request)));
		user.setLastLoginIp(String.valueOf(RequestUtil.getRemoteIp(request)));

		authService.setUserSignin(user);

		return new ResponseData<>(new SigninResponse(acsToken, refToken));
	}

	@PostMapping("/token")
	public ResponseData<TokenResponse> valid(HttpServletRequest request) throws Exception {
		String email = String.valueOf(request.getAttribute(Const.TOKEN_EMAIL));
		if (StringUtils.isEmpty(email)) {
			throw new UnauthorizedException();
		}

		return new ResponseData<>(new TokenResponse(TokenUtil.createAccessToken(email)));
	}

	@PostMapping("/me")
	public ResponseData<User> me(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String email = String.valueOf(request.getAttribute(Const.TOKEN_EMAIL));
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
