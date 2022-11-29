
package kr.co.iwi.isso.app.auth.vo.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
	private long userSeq;
	private String email;

	@JsonIgnore
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
	private String posiNm;
	private String dutyCd;
	private String dutyNm;
	private String deptCd;
	private String deptNm;
	private String useYn;
	private String delYn;

	@JsonIgnore
	private String refToken;

	@JsonIgnore
	private Date refIssueDt;

	@JsonIgnore
	private String refIssueIp;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastLoginDt;

	private String lastLoginIp;
	private long loginFailCnt;
}
