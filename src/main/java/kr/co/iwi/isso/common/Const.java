package kr.co.iwi.isso.common;

public class Const {

	public static final String IWI_DOMAIN = "localhost";
	public static final String[] IWI_DOMAINS = { "http://localhost:3000", "http://localhost:8080" };

	// public static final String IWI_DOMAIN = "iwi.co.kr";
	// public static final String[] IWI_DOMAINS = { "https://iwms.iwi.co.kr", "https://russel.iwi.co.kr" };

	// 토큰 생성 암호 키
	public static final String TOKEN_SECRET_KEY = "qNrjv5g^AlXVOhcc3y#OP@ez^XqUmKpT"; // more then 32 bytes

	// 인증 토큰 유효시간
	public static final long ACS_TOKEN_MINUTES = 60 * 1; // 1시간
	public static final long REF_TOKEN_MINUTES = 60 * 24 * 90; // 90일

	///////////////////////////////////////////////////////////////////////////////////////////
	//
	// public static final String COOKIE_NAME_ACS = "I-TOKEN-ACS";
	// public static final String COOKIE_NAME_REF = "I-TOKEN-REF";
	//
	///////////////////////////////////////////////////////////////////////////////////////////

	public static final String TOKEN_COOKIE = "XSRF-TOKEN";
	public static final String TOKEN_EMAIL = "TOKEN_EMAIL";
}
