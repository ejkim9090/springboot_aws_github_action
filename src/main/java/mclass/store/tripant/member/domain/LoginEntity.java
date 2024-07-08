package mclass.store.tripant.member.domain;

import lombok.Data;

@Data
public class LoginEntity {
	private String memEmail;
	private String memNick;
	private String memRole;
	private Integer memEnabled;
}
