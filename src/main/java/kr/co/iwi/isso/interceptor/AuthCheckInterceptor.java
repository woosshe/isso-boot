package kr.co.iwi.isso.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import kr.co.iwi.isso.app.auth.service.AuthService;
import kr.co.iwi.isso.app.auth.vo.model.User;
import kr.co.iwi.isso.common.Const;
import kr.co.iwi.isso.exception.TokenExpiredException;
import kr.co.iwi.isso.exception.UnauthorizedException;
import kr.co.iwi.isso.util.TokenUtil;

public class AuthCheckInterceptor implements HandlerInterceptor {

	@Autowired
	private AuthService authService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		this.userAuthCheck(request, response);

		return true;
	}

	private void userAuthCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.removeAttribute(Const.COOKIE_NAME_ACS);
		request.removeAttribute(Const.COOKIE_NAME_REF);

		String acsToken = null;
		String refToken = null;

		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			throw new UnauthorizedException();
		}

		for (Cookie cookie : cookies) {
			if (Const.COOKIE_NAME_ACS.equals(cookie.getName())) {
				acsToken = cookie.getValue();
			} else if (Const.COOKIE_NAME_REF.equals(cookie.getName())) {
				refToken = cookie.getValue();
			}
		}

		// System.out.println("===== autchCheck =========================");
		// System.out.println(acsToken);
		// System.out.println(refToken);
		// System.out.println("==========================================");

		boolean isEmptyAcsToken = StringUtils.isEmpty(acsToken);
		boolean isEmptyRefToken = StringUtils.isEmpty(refToken);

		if (isEmptyAcsToken && isEmptyRefToken) {
			throw new UnauthorizedException();
		}

		boolean isExpiredAcsToken = true;
		boolean isExpiredRefToken = true;

		if (!isEmptyAcsToken) {
			isExpiredAcsToken = TokenUtil.isExpired(acsToken);
		}

		if (!isEmptyRefToken) {
			isExpiredRefToken = TokenUtil.isExpired(refToken);
		}

		if (isExpiredAcsToken && isExpiredRefToken) {
			throw new TokenExpiredException();
		}

		String email = null;
		if (isExpiredAcsToken) {
			// 엑세스 토큰 만료, 리프레시 토큰 검증

			if (isEmptyRefToken || isExpiredRefToken) {
				// 리프레시 토큰 누락 or 만료
				throw new TokenExpiredException();
			}

			User user = authService.getUserInfoByToken(refToken);
			if (user == null) {
				// 리프레시 토큰 검증 실패
				throw new UnauthorizedException();
			}

			email = user.getEmail();
		} else {
			// 엑세스 토큰 갱신

			email = TokenUtil.getSubject(acsToken);
		}

		if (StringUtils.isEmpty(email)) {
			throw new UnauthorizedException();
		}

		acsToken = TokenUtil.createAccessToken(email);
		response.addCookie(TokenUtil.makeCookie(Const.COOKIE_NAME_ACS, acsToken));

		request.setAttribute(Const.COOKIE_NAME_ACS, acsToken);
		request.setAttribute(Const.COOKIE_NAME_REF, refToken);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

}
