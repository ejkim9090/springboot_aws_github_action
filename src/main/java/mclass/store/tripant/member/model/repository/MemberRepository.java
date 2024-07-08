package mclass.store.tripant.member.model.repository;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import mclass.store.tripant.member.domain.MemberEntity;

@Mapper
public interface MemberRepository {
	// 로그인/로그아웃
	// 로그인
	public MemberEntity login(String memEmail);
	
	// SNS 토큰
	public int updateToken(Map<String, Object> map);
	
	// 토큰 값 가져오기
	public Map<String, Object> tokenValue(String memEmail);
	
	// SNS 유형 갱신
	public int updateType(Map<String, Object> map);
	
	// 로그인 기록
	public int log(Map<String, Object> map);
	
	// 휴면 해제
	public int awake(String memEmail);

	// 회원가입
	// 회원가입
	public Integer join(MemberEntity memberEntity);

	// 닉네임 중복 여부
	public Integer existNick(String memNick);

	// 이메일 가입 여부
	public Integer existEmail(String memEmail);

	// 비밀번호 재설정
	public int setPwd(Map<String, Object> map);
	
	// 마이페이지
	// 내 정보
	public Map<String, Object> myInfo(String memEmail);
	
	// 닉네임 변경
	public int saveNick(Map<String, Object> map);

	// 비밀번호 변경
	public int savePwd(Map<String, Object> map);
	
	// 현재 비밀번호
	public String currPwd(String memEmail);
	
	// 회원 탈퇴
	public int memQuit(String memEmail);
}
