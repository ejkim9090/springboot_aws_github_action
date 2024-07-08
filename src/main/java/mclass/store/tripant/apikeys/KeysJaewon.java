package mclass.store.tripant.apikeys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class KeysJaewon {
	
	//DB 연결
	@Value("${spring.datasource.hikari.jdbc-log4j2-driverClassName}")
	private String hikariDriverClassName;
	@Value("${spring.datasource.hikari.jdbc-log4j2-url}")
	private String hikariUrl;
	@Value("${spring.datasource.hikari.username}")
	private String hikariUsername;
	@Value("${spring.datasource.hikari.password}")
	private String hikariPassword;
	
	//도메인
	@Value("${domain}")
	private String domain;
	
	//구글 이메일 발송
	@Value("${email.username}")
	private String gmail;
	@Value("${email.password}")
	private String gmailPwd;
	
	//구글 리캡챠
	@Value("${robot.key}")
	private String robotKey;
	@Value("${robot.secret}")
	private String robotSecret;
}
