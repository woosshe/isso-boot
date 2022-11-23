package kr.co.iwi.isso.app.auth.vo.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
	private Long userSeq;
	private String email;
	private String password;
	private String userNo;
	private String userNm;
	private String userTel;
	private String userHp;
	private String userFax;
	private String userBirth;
	private String userSex;
	private String entryYmd;
	private String quitYmd;
	private String posiCd;
	private String dutyCd;
	private String deptCd;
	private String useYn;
	private String delYn;
	private String refToken;
	private Date refIssueDt;
	private Date lastLoginDt;
	private String lastLoginIp;
}
