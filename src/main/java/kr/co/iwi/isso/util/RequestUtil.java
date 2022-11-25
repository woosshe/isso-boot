package kr.co.iwi.isso.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class RequestUtil {
	public static String getRemoteIp(HttpServletRequest request) throws Exception {
		String remoteIp = request.getHeader("X-Forwarded-For");
		if (StringUtils.isEmpty(remoteIp) || "unknown".equalsIgnoreCase(remoteIp))
			remoteIp = request.getHeader("X-Real-IP");
		if (StringUtils.isEmpty(remoteIp) || "unknown".equalsIgnoreCase(remoteIp))
			remoteIp = request.getHeader("Proxy-Client-IP");
		if (StringUtils.isEmpty(remoteIp) || "unknown".equalsIgnoreCase(remoteIp))
			remoteIp = request.getHeader("WL-Proxy-Client-IP");
		if (StringUtils.isEmpty(remoteIp) || "unknown".equalsIgnoreCase(remoteIp))
			remoteIp = request.getHeader("HTTP_CLIENT_IP");
		if (StringUtils.isEmpty(remoteIp) || "unknown".equalsIgnoreCase(remoteIp))
			remoteIp = request.getHeader("HTTP_X_FORWARDED_FOR");
		if (StringUtils.isEmpty(remoteIp) || "unknown".equalsIgnoreCase(remoteIp))
			remoteIp = request.getRemoteAddr();
		return remoteIp;
	}
}
