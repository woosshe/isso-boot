package kr.co.iwi.isso.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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

	public static Map<String, Object> getBodyMap(HttpServletRequest request) {

		Map<String, Object> body = new HashMap<String, Object>();

		try {
			StringBuilder stringBuilder = new StringBuilder();
			BufferedReader br = null;
			String line = "";

			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				br = new BufferedReader(new InputStreamReader(inputStream));
				while ((line = br.readLine()) != null) {
					stringBuilder.append(line);
				}
			} else {
				log.info("Data 없음");
			}

			String bodyJson = stringBuilder.toString();

			JsonParser springParser = JsonParserFactory.getJsonParser();
			body = springParser.parseMap(bodyJson);
		} catch (Exception e) {
			// do anything
		}

		return body;
	}
}
