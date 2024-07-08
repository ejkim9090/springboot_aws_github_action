package mclass.store.tripant.member.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mclass.store.tripant.apikeys.KeysJaewon;
import mclass.store.tripant.config.RecaptchaConfig;
import mclass.store.tripant.member.domain.MemberEntity;
import mclass.store.tripant.member.model.service.MemberService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/join")
@Slf4j
public class JoinController {
	
	private final KeysJaewon keysJaewon;
	private final MemberService memberService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Value("${robot.secret}")
	private String robotSecret;

	//회원가입 페이지
	@GetMapping("")
	public String join(Model model) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		String today = sdf.format(calendar.getTime());
		model.addAttribute("today", today);
		
		String agree1 = "<개인정보 취급 동의>\n"
				+ "1. 트립앤트에서 귀하의 휴대폰 번호, 생년월일을 보관하는 것에 동의합니다.\n"
				+ "2. 최근 로그인 및 로그인 시도로부터 6개월 동안 로그인 및 로그인 시도가 없을 시 해당 계정은 휴면 회원으로 변경되는 것에 동의합니다.";
		model.addAttribute("agree1", agree1);
		String agree2  = "<SNS 관련 동의>\n"
				+ "1. SNS를 통한 로그인 및 회원가입 시 트립엔트에서 귀하의 이메일을 수집함에 동의합니다.\n"
				+ "2. 카카오 서비스는 연동 해제가 가능하며 네이버, 구글 서비스에 대해서는 연동 해제 기능이 구현되지 않았으며 이 점을 인지하였음을 동의합니다.";
		model.addAttribute("agree2", agree2);
		
		return "member/join";
	}
	
	//SNS 회원가입 페이지
	@GetMapping("/sns")
	public String joinSns(Model model) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		String today = sdf.format(calendar.getTime());
		model.addAttribute("today", today);
		
		String agree1 = "<개인정보 취급 동의>\n"
				+ "1. 트립앤트에서 귀하의 휴대폰 번호, 생년월일을 보관하는 것에 동의합니다.\n"
				+ "2. 최근 로그인 및 로그인 시도로부터 6개월 동안 로그인 및 로그인 시도가 없을 시 해당 계정은 휴면 회원으로 변경되는 것에 동의합니다.";
		model.addAttribute("agree1", agree1);
		String agree2  = "<SNS 관련 동의>\n"
				+ "1. SNS를 통한 로그인 및 회원가입 시 트립엔트에서 귀하의 이메일을 수집함에 동의합니다.\n"
				+ "2. 카카오 서비스는 연동 해제가 가능하며 네이버, 구글 서비스에 대해서는 연동 해제 기능이 구현되지 않았으며 이 점을 인지하였음을 동의합니다.";
		model.addAttribute("agree2", agree2);
		return "member/joinsns";
	}
	
	// 닉네임 중복 검사
	@PostMapping("/nick/check")
	@ResponseBody
	public Integer joinNickCheck(@RequestParam String memNick) {
		int result = memberService.existNick(memNick);
		return result;
	}
	
	// 회원가입
	@PostMapping("")
	@ResponseBody
	public int joinP(MemberEntity memberEntity, String recaptcha) {
		
		memberEntity.setMemPassword(bCryptPasswordEncoder.encode(memberEntity.getMemPassword()));
		memberEntity.setMemEnabled(1);
		memberEntity.setMemRole("ROLE_MEM");
		memberEntity.setMemType(Integer.parseInt(String.valueOf(8)));
		memberEntity.setMemTel(memberEntity.getMemTel().replaceAll("-", ""));
		log.debug("[sjw] mem = "+memberEntity);
		
		RecaptchaConfig.setSecretKey(robotSecret);
		try {
			if(RecaptchaConfig.verify(recaptcha)) {
				memberService.join(memberEntity);
				return 1;
			}else {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	// SNS 회원가입
	@PostMapping("/sns")
	@ResponseBody
	public int joinSnsP(MemberEntity memberEntity, String recaptcha, HttpSession session) {
		
		String memEmail = (String) session.getAttribute("memEmail");
		int memType = Integer.parseInt((String) session.getAttribute("memType"));
		memberEntity.setMemEmail(memEmail);
		memberEntity.setMemPassword(bCryptPasswordEncoder.encode(memberEntity.getMemPassword()));
		memberEntity.setMemEnabled(1);
		memberEntity.setMemRole("ROLE_MEM");
		memberEntity.setMemType(memType);
		memberEntity.setMemTel(memberEntity.getMemTel().replaceAll("-", ""));
		
		RecaptchaConfig.setSecretKey(keysJaewon.getRobotSecret());
		try {
			if(RecaptchaConfig.verify(recaptcha)) {
				memberService.join(memberEntity);
				session.removeAttribute("memType");
				session.removeAttribute("memEmail");
				return 1;
			}else {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
}
