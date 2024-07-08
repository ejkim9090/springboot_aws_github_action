package mclass.store.tripant.member.model.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mclass.store.tripant.member.domain.MemberEntity;
import mclass.store.tripant.member.model.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	
	// 로그인/로그아웃
	// 로그인
	public MemberEntity login(String memEmail) {
		return memberRepository.login(memEmail);
	}
	
	// 토큰 값 가져오기
	public Map<String, Object> tokenValue(String memEmail){
		return memberRepository.tokenValue(memEmail);
	}
	
	// SNS 유형 갱신
	public int updateType(Map<String, Object> map) {
		return memberRepository.updateType(map);
	}
	
	// 로그인 기록
	public int log(Map<String, Object> map) {
		return memberRepository.log(map);
	}
	
	// 휴면 해제
	public int awake(String memEmail) {
		return memberRepository.awake(memEmail);
	}

	// 회원가입
	// 회원가입
	public Integer join(MemberEntity memberEntity) {
		return memberRepository.join(memberEntity);
	}

	// 닉네임 중복 여부
	public Integer existNick(String memNick) {
		return memberRepository.existNick(memNick);
	}

	// 이메일 가입 여부
	public Integer existEmail(String memEmail) {
		return memberRepository.existEmail(memEmail);
	}

	// 비밀번호 재설정
	public int setPwd(Map<String, Object> map) {
		return memberRepository.setPwd(map);
	}

	// 마이페이지
	// 내 정보
	public Map<String, Object> myInfo(String memEmail){
		return memberRepository.myInfo(memEmail);
	}
	// 닉네임 변경
	public int saveNick(Map<String, Object> map) {
		return memberRepository.saveNick(map);
	}
	
	// 비밀번호 변경
	public int savePwd(Map<String, Object> map){
		return memberRepository.savePwd(map);
	}
	
	// 현재 비밀번호
	public String currPwd(String memEmail) {
		return memberRepository.currPwd(memEmail);
	};
	
	// 회원 탈퇴
	@Transactional
	public int memQuit(String memEmail) {
		int result = memberRepository.memQuit(memEmail);
		return result;
	}
}
