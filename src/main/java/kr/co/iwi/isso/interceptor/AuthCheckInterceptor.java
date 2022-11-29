package kr.co.iwi.isso.interceptor;

import java.util.Date;
import java.util.Map;

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
import kr.co.iwi.isso.util.RequestUtil;
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
		Date now = new Date();
		System.out.println(now.toString() + "  /  " + request.getRequestURI());

		String acsToken = null;
		String refToken = null;

		String authorization = request.getHeader("Authorization");
		if (StringUtils.isEmpty(authorization)) {
			throw new UnauthorizedException();
		}

		Map<String, Object> body = RequestUtil.getBodyMap(request);

		if (Const.GRANT_TYPE_REF.equals(body.get("grant_type"))) {
			refToken = TokenUtil.extract(authorization);
		} else {
			acsToken = TokenUtil.extract(authorization);
		}

		String email = null;

		if (StringUtils.isNotEmpty(acsToken)) {

			// 엑세스 토큰 만료 체크
			if (TokenUtil.isExpired(acsToken)) {
				System.out.println("엑세스 토큰 만료");
				throw new TokenExpiredException();
			}

			// 엑세스 토큰 email 추출
			email = TokenUtil.getSubject(acsToken);
			if (StringUtils.isEmpty(email)) {
				throw new UnauthorizedException();
			}

		} else if (StringUtils.isNotEmpty(refToken)) {

			// 리프레시 토큰 만료 체크
			if (TokenUtil.isExpired(refToken)) {
				System.out.println("리프레시 토큰 만료");
				throw new TokenExpiredException();
			}

			// 리프레시 토큰 사용자 검증
			User user = authService.getUserInfoByToken(refToken, RequestUtil.getRemoteIp(request));
			if (user == null) {
				throw new UnauthorizedException();
			}

			// 사용자 email 추출
			email = user.getEmail();
			if (StringUtils.isEmpty(email)) {
				throw new UnauthorizedException();
			}

		} else {
			// 인증실패
			throw new UnauthorizedException();
		}

		request.setAttribute(Const.TOKEN_EMAIL, email);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

}
