package mclass.store.tripant.member.domain;

import lombok.Getter;

@Getter
public enum MemberRole {
	ADMIN("ADMIN"), VIP("VIP"), MEM("MEM"), SLEEP("SLEEP");
	
	private String role;
	
	private MemberRole(String role){
		this.role = role;
	}
}
