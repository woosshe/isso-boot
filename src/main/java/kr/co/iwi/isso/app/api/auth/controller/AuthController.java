package kr.co.iwi.isso.app.api.auth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.iwi.isso.app.api.auth.service.AuthService;
import kr.co.iwi.isso.app.api.user.vo.model.User;
import kr.co.iwi.isso.app.api.auth.vo.request.SigninRequest;
import kr.co.iwi.isso.app.api.auth.vo.response.SigninResponse;
import kr.co.iwi.isso.common.Const;
import kr.co.iwi.isso.common.vo.Response;
import kr.co.iwi.isso.common.vo.ResponseData;
import kr.co.iwi.isso.exception.IException;
import kr.co.iwi.isso.exception.UnauthorizedException;
import kr.co.iwi.isso.util.SecureUtil;
import kr.co.iwi.isso.util.TokenUtil;

@RestController
@RequestMapping("/api/auth")
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

		///////////////////////////////////////////////////////////////////////////////////////////
		//
		// String acsToken = TokenUtil.createAccessToken(user.getEmail());
		// String refToken = TokenUtil.createRefreshToken();
		//
		// user.setRefToken(refToken);
		// user.setLastLoginIp(RequestUtil.getRemoteIp(request));
		//
		// authService.setUserSignin(user);
		//
		// response.addCookie(TokenUtil.makeCookie(Const.COOKIE_NAME_ACS, acsToken));
		// response.addCookie(TokenUtil.makeCookie(Const.COOKIE_NAME_REF, refToken));
		//
		// request.setAttribute(Const.COOKIE_NAME_ACS, acsToken);
		// request.setAttribute(Const.COOKIE_NAME_REF, refToken);
		//
		// return new ResponseData<>(new SigninResponse(acsToken, refToken));
		//
		///////////////////////////////////////////////////////////////////////////////////////////

		String acsToken = TokenUtil.createAccessToken(user.getEmail());
		response.addCookie(TokenUtil.makeCookie(Const.TOKEN_COOKIE, acsToken));
		request.setAttribute(Const.TOKEN_COOKIE, acsToken);

		return new ResponseData<>(new SigninResponse(acsToken));

		///////////////////////////////////////////////////////////////////////////////////////////
	}

	@PostMapping("/me")
	public ResponseData<User> me(HttpServletRequest request, HttpServletResponse response) throws Exception {

		///////////////////////////////////////////////////////////////////////////////////////////
		//
		// String acsToken = (String) request.getAttribute(Const.COOKIE_NAME_ACS);
		//
		// String email = TokenUtil.getSubject(acsToken);
		// if (StringUtils.isEmpty(email)) {
		// throw new UnauthorizedException();
		// }
		//
		///////////////////////////////////////////////////////////////////////////////////////////

		String email = (String) request.getAttribute(Const.TOKEN_EMAIL);

		///////////////////////////////////////////////////////////////////////////////////////////

		User user = authService.getUserInfo(email);
		if (user == null) {
			throw new UnauthorizedException();
		}

		return new ResponseData<>(user);
	}

	@PostMapping("/signout")
	public Response signin(HttpServletRequest request, HttpServletResponse response) throws Exception {

		response.addCookie(TokenUtil.makeCookie(Const.TOKEN_COOKIE, null, 0));
		request.removeAttribute(Const.TOKEN_COOKIE);

		return Response.success();
	}
}
