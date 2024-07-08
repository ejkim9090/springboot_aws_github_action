package mclass.store.tripant.member.domain;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Getter;

@Getter
public class CustomOAuth2User extends User implements OAuth2User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomOAuth2User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}
	
	@Override
	public Map<String, Object> getAttributes() {
		return null;
	}
	
	@Override
	public String getName() {
		return null;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
}
