package mclass.store.tripant.admin.domain;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data 
@Component
public class AdminMemEntity {
	
	private String memNick;
	private String memEmail;
	private String memJoinDate;
	private String memQuitDate;
	private String memRole;
	private Integer memEnabled;
}
