package mclass.store.tripant.member.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mclass.store.tripant.apikeys.KeysJaewon;
import mclass.store.tripant.member.domain.MemberEntity;
import mclass.store.tripant.member.model.service.MemberService;

@RequiredArgsConstructor
@Controller
@Slf4j
public class LoginController {
	
	private final KeysJaewon keysJaewon;
	private final MemberService memberService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	//로그인 페이지
	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false)String error, 
						@RequestParam(value = "exception", required = false)String exception, 
						Model model) {
		String msg="";
		if(exception != null) {
			switch(exception) {
			case "BadCredentialsException":
				msg = "이메일 또는 비밀번호가 맞지 않습니다. 다시 확인해주세요.";
				break;
			case "InternalAuthenticationServiceException":
				msg = "시스템 문제로 인해 요청을 처리할 수 없습니다. 관리자에게 문의해주세요.";
				break;
			case "UsernameNotFoundException":
				msg = "계정이 존재하지 않습니다. 회원가입 진행 후 로그인 해주세요.";
				break;
			case "AuthenticationException":
				msg = "정지된 회원입니다. 관리자에게 문의해주세요.";
				break;
			default:
				msg = "알 수 없는 이유로 로그인에 실패하였습니다. 관리자에게 문의해주세요.";
				break;
			}
		}
		model.addAttribute("robotKey", keysJaewon.getRobotKey());
		model.addAttribute("error", error);
		model.addAttribute("exception", msg);
		return "member/login";
	}
	
	//비밀번호 찾기 페이지
	@GetMapping("/pwd")
	public String pwd() {
		return "member/pwd";
	}
	
	//비밀번호 재설정
	@PostMapping("/pwd")
	@ResponseBody
	public int pwdP(@RequestParam String memEmail, @RequestParam String memPassword) {
		log.debug("[sjw] memEmail = "+memEmail);
		log.debug("[sjw] memPwd = "+memPassword);
		Map<String , Object> map = new HashMap<>();
		map.put("memEmail", memEmail);
		map.put("memPassword", bCryptPasswordEncoder.encode(memPassword));
		int result = memberService.setPwd(map);
		return result;
	}
	
	// 오류 페이지
	@GetMapping("/exception")
	public String exception(Model model, HttpSession session, @RequestParam String code) {
		model.addAttribute("code", code);
		String memEmail = (String) session.getAttribute("memEmail");
		model.addAttribute("memEmail", memEmail);
		MemberEntity memberEntity = memberService.login(memEmail);
		model.addAttribute("memRole", memberEntity.getMemRole());
		return "common/exception";
	}
}

