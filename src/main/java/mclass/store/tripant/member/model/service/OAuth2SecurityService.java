package mclass.store.tripant.member.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mclass.store.tripant.member.domain.CustomOAuth2User;
import mclass.store.tripant.member.domain.MemberEntity;
import mclass.store.tripant.member.domain.MemberRole;
import mclass.store.tripant.member.model.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2SecurityService extends DefaultOAuth2UserService {
	
	private final MemberRepository memberRepository;
	private final Gson gson;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		String email = "";
		Map<String, Object> map = new HashMap<>();
		String snsType = userRequest.getClientRegistration().getRegistrationId();
		
		// 카카오 로그인
		if(snsType.equals("kakao")) {
			Map<String, Object> newMap = new HashMap<>();
			
			// 카카오 이메일
			Map<String, Object> kakaoAttributes = oAuth2User.getAttribute("kakao_account");
			email = (String) kakaoAttributes.get("email");
			
			// 카카오 토큰 업데이트
			Map<String, Object> updateToken = new HashMap<>();
			updateToken.put("memEmail", email);
			updateToken.put("kakaoToken", userRequest.getAccessToken().getTokenValue());
			memberRepository.updateToken(updateToken);
			
			// 로그인 정보
			MemberEntity memberEntity = memberRepository.login(email);
			
			// 이미 가입된 경우
			if(memberEntity != null) {
				// sns 유형
				int memType = Integer.parseInt(String.valueOf(memberEntity.getMemType()));
				
				// 카카오 가입 여부
				int isKakao = memType & 4;
				if(isKakao != 4) {
					memType += 4;
				}
				
				newMap.put("memEmail", email);
				newMap.put("memType", memType);
				
				// SNS 유형 갱신
				memberRepository.updateType(newMap);
				
			}
			// 카카오로 가입하는 경우
			else {
				map.put("memType", "12");
			}
		}else if(snsType.equals("naver")) {
			Map<String, Object> newMap = new HashMap<>();
			
			// 네이버 이메일
			Map<String, Object> naverAttributes = oAuth2User.getAttribute("response");
			email = (String) naverAttributes.get("email");
			
			// 네이버 토큰 업데이트
			Map<String, Object> updateToken = new HashMap<>();
			updateToken.put("memEmail", email);
			updateToken.put("naverToken", userRequest.getAccessToken().getTokenValue());
			memberRepository.updateToken(updateToken);
			System.out.println("네이버 토큰 >>>>>>>>>>>>> "+userRequest.getAdditionalParameters());
			
			// 로그인 정보
			MemberEntity memberEntity = memberRepository.login(email);
			
			// 이미 가입된 경우
			if(memberEntity != null) {
				// sns 유형
				int memType = Integer.parseInt(String.valueOf(memberEntity.getMemType()));
				
				// 네이버 가입 여부
				int isNaver = memType & 2;
				if(isNaver != 2) {
					memType += 2;
				}
				
				newMap.put("memEmail", email);
				newMap.put("memType", memType);
				
				// SNS 유형 갱신
				memberRepository.updateType(newMap);
				
			}
			// 네이버로 가입하는 경우
			else {
				map.put("memType", "10");
			}
		}else if(snsType.equals("google")){
			Map<String, Object> newMap = new HashMap<>();
			
			// 구글 이메일
			email = (String) oAuth2User.getAttributes().get("email");
			
			// 구글 토큰 업데이트
			Map<String, Object> updateToken = new HashMap<>();
			updateToken.put("memEmail", email);
			updateToken.put("googleToken", userRequest.getAccessToken().getTokenValue());
			System.out.println("구글 토큰 >>>>>>>> "+userRequest.getAccessToken().getTokenValue());
			memberRepository.updateToken(updateToken);
			
			// 로그인 정보
			MemberEntity memberEntity = memberRepository.login(email);
			
			// 이미 가입된 경우
			if(memberEntity != null) {
				// sns 유형
				int memType = Integer.parseInt(String.valueOf(memberEntity.getMemType()));
				
				// 구글 가입 여부
				int isGoogle = memType & 1;
				if(isGoogle != 1) {
					memType += 1;
				}
				
				newMap.put("memEmail", email);
				newMap.put("memType", memType);
				
				// SNS 유형 갱신
				memberRepository.updateType(newMap);
				
			}
			// 구글로 가입하는 경우
			else {
				map.put("memType", "9");
			}
		}
		map.put("memEmail", email);
		
		log.debug("[sjw] email = "+email);
		Optional<MemberEntity> memberEntityOp = Optional.ofNullable(memberRepository.login(email));
		if(memberEntityOp.isEmpty()) {
			throw new UsernameNotFoundException(gson.toJson(map));
		}
		MemberEntity memberEntity = memberEntityOp.get();
		String memRole = memberEntity.getMemRole();
		if(memRole.equals("ROLE_SLEEP")) {
			throw new AuthenticationException("4021") {
				private static final long serialVersionUID = 1L;
			};
		}
		if(memberEntity.getMemEnabled() == 0) {
			throw new OAuth2AuthenticationException("401");
		}
		List<GrantedAuthority> authorities = new ArrayList<>();
		switch(memRole) {
			case "ROLE_ADMIN": authorities.add(new SimpleGrantedAuthority(MemberRole.ADMIN.getRole()));
			case "ROLE_VIP": authorities.add(new SimpleGrantedAuthority(MemberRole.VIP.getRole()));
			case "ROLE_MEM": authorities.add(new SimpleGrantedAuthority(MemberRole.MEM.getRole()));
		}
		log.debug("[sjw] oAuth2User = "+oAuth2User.toString());
		
		
		
		return new CustomOAuth2User(email, memberEntity.getMemPassword(), authorities);
	}
}
