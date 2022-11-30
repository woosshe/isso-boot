package kr.co.iwi.isso.util;

import java.security.Key;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import kr.co.iwi.isso.common.Const;

public class TokenUtil {

	// 엑세스 토큰 생성
	public static String createAccessToken(String subject) {
		if (StringUtils.isEmpty(subject)) {
			return null;
		}

		String secretKeyEncodeBase64 = Encoders.BASE64.encode(Const.TOKEN_SECRET_KEY.getBytes());
		byte[] keyBytes = Decoders.BASE64.decode(secretKeyEncodeBase64);
		Key key = Keys.hmacShaKeyFor(keyBytes);
		Claims claims = Jwts.claims().setSubject(subject);
		return Jwts.builder().signWith(key).setClaims(claims).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + Const.ACS_TOKEN_MINUTES * 60 * 1000)).compact();
	}

	// 리프레쉬 토큰 생성
	public static String createRefreshToken() {
		String secretKeyEncodeBase64 = Encoders.BASE64.encode(Const.TOKEN_SECRET_KEY.getBytes());
		byte[] keyBytes = Decoders.BASE64.decode(secretKeyEncodeBase64);
		Key key = Keys.hmacShaKeyFor(keyBytes);
		return Jwts.builder().signWith(key).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + Const.REF_TOKEN_MINUTES * 60 * 1000)).compact();
	}

	private static Claims getClaims(String token) {
		if (StringUtils.isEmpty(token)) {
			return null;
		}

		String secretKeyEncodeBase64 = Encoders.BASE64.encode(Const.TOKEN_SECRET_KEY.getBytes());
		byte[] keyBytes = Decoders.BASE64.decode(secretKeyEncodeBase64);
		Key key = Keys.hmacShaKeyFor(keyBytes);

		Claims claims = null;
		try {
			claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		} catch (JwtException e) {
			claims = null;
		}

		return claims;
	}

	public static String getSubject(String token) {
		final Claims claims = getClaims(token);
		if (claims == null) {
			return null;
		}
		return claims.getSubject();
	}

	private static Date getExpiration(String token) {
		final Claims claims = getClaims(token);
		if (claims == null) {
			return null;
		}
		return claims.getExpiration();
	}

	public static Boolean isExpired(String token) {
		Date exp = getExpiration(token);
		if (exp == null) {
			return true;
		}
		return getExpiration(token).before(new Date());
	}

	public static Boolean validate(String token, String subject) {
		final String tokenSubject = getSubject(token);
		return (subject.equals(tokenSubject) && !isExpired(token));
	}

	public static Cookie makeCookie(String name, String value) {
		return makeCookie(name, value, Const.REF_TOKEN_MINUTES);
	}

	public static Cookie makeCookie(String name, String value, long min) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(Long.valueOf(min * 60).intValue());
		// cookie.setMaxAge(Long.valueOf(60 * 60 * 24 * 365).intValue());
		cookie.setDomain(Const.IWI_DOMAIN);
		cookie.setPath("/");
		cookie.setHttpOnly(true);

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		cookie.setSecure(request.isSecure());

		return cookie;
	}

}
