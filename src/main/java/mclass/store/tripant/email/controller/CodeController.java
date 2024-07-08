package mclass.store.tripant.email.controller;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import mclass.store.tripant.apikeys.KeysJaewon;
import mclass.store.tripant.member.model.service.MemberService;

@Controller
@RequestMapping("/code")
@RequiredArgsConstructor
public class CodeController {
	
	private final KeysJaewon keysJaewon;
	private final Gmail gmail;
	private final MemberService memberService;
	
	//인증번호 발송
	@PostMapping("/send")
	@ResponseBody
	public String codeSend(HttpServletResponse response,@RequestParam String memEmail
			, HttpSession session) {
		if(memberService.existEmail(memEmail) == 1) {
			return "-1";
		}
		// 랜덤 문자열
		int leftLimit = 97; // 'a'
		int rightLimit = 122; // 'z'
		int codeLength = 10;
		Random rand = new Random();
		StringBuilder sb = new StringBuilder(codeLength);
		for(int i = 0; i < codeLength; i++) {
			int randLimitInt = leftLimit + (int)(rand.nextFloat() * (rightLimit - leftLimit + 1));
			sb.append((char)randLimitInt);
		}
		String code = sb.toString();
		
		// 사용자 인증 이메일 발송 내용
		String from = keysJaewon.getGmail();
		String to = memEmail;
		String subject = "[Tripant] 이메일 인증번호";
		String content = "인증번호: "+code; 
		
		// 이메일 전송 : SMTP 이용을 위함
		Properties p = new Properties();
		p.put("mail.smtp.user", from);
		p.put("mail.smtp.host", "smtp.googlemail.com"); // google SMTP 주소
		p.put("mail.smtp.port", "465");
		p.put("mail.smtp.starttls.enable", "true");
		p.put("mail.smtp.auth", "true");
		p.put("mail.smtp.debug", "true");
		p.put("mail.smtp.socketFactory.port", "465");
		p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		p.put("mail.smtp.socketFactory.fallback", "false");
		p.put("mail.smtp.ssl.protocols", "TLSv1.2"); // 추가된 코드
		p.put("mail.smtp.ssl.enable", "true");  // 추가된 코드
		try{
			Authenticator auth = gmail;
			Session ses = Session.getInstance(p, auth);
			ses.setDebug(true);
			MimeMessage msg = new MimeMessage(ses); 
			msg.setSubject(subject);		// 메일 제목
			Address fromAddr = new InternetAddress(from); 	// 보내는 사람 정보
			msg.setFrom(fromAddr);
			Address toAddr = new InternetAddress(to);		// 받는 사람 정보
			msg.addRecipient(Message.RecipientType.TO, toAddr);
			msg.setContent(content, "text/html;charset=UTF-8");
			Transport.send(msg); // 메세지 전송
			session.setAttribute("code", code);
			return "1";
		}catch(Exception e){
			session.removeAttribute("code");
			e.printStackTrace();
			return "0";
		}
	}
	
	//인증번호 확인
	@PostMapping("/check")
	@ResponseBody
	public String codeCheck(HttpSession session, @RequestParam String inputCode) throws IOException {
		if(inputCode.equals(session.getAttribute("code"))) {
			session.removeAttribute("code");
			return "1";
		}else {
			return "0";
		}
	}
	
	//비밀번호 찾기 - 인증번호 발송
	@PostMapping("/send/pwd")
	@ResponseBody
	public String codeSendPwd(HttpServletResponse response,@RequestParam String memEmail
			, HttpSession session) {
		if(memberService.existEmail(memEmail) == 0) {
			return "-1";
		}
		// 랜덤 문자열
		int leftLimit = 97; // 'a'
		int rightLimit = 122; // 'z'
		int codeLength = 10;
		Random rand = new Random();
		StringBuilder sb = new StringBuilder(codeLength);
		for(int i = 0; i < codeLength; i++) {
			int randLimitInt = leftLimit + (int)(rand.nextFloat() * (rightLimit - leftLimit + 1));
			sb.append((char)randLimitInt);
		}
		String code = sb.toString();
		
		// 사용자 인증 이메일 발송 내용
		String from = keysJaewon.getGmail();
		String to = memEmail;
		String subject = "[Tripant] 이메일 인증번호";
		String content = "인증번호: "+code; 
		
		// 이메일 전송 : SMTP 이용을 위함
		Properties p = new Properties();
		p.put("mail.smtp.user", from);
		p.put("mail.smtp.host", "smtp.googlemail.com"); // google SMTP 주소
		p.put("mail.smtp.port", "465");
		p.put("mail.smtp.starttls.enable", "true");
		p.put("mail.smtp.auth", "true");
		p.put("mail.smtp.debug", "true");
		p.put("mail.smtp.socketFactory.port", "465");
		p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		p.put("mail.smtp.socketFactory.fallback", "false");
		p.put("mail.smtp.ssl.protocols", "TLSv1.2"); // 추가된 코드
		p.put("mail.smtp.ssl.enable", "true");  // 추가된 코드
		try{
			Authenticator auth = gmail;
			Session ses = Session.getInstance(p, auth);
			ses.setDebug(true);
			MimeMessage msg = new MimeMessage(ses); 
			msg.setSubject(subject);		// 메일 제목
			Address fromAddr = new InternetAddress(from); 	// 보내는 사람 정보
			msg.setFrom(fromAddr);
			Address toAddr = new InternetAddress(to);		// 받는 사람 정보
			msg.addRecipient(Message.RecipientType.TO, toAddr);
			msg.setContent(content, "text/html;charset=UTF-8");
			Transport.send(msg); // 메세지 전송
			session.setAttribute("code", code);
			return "1";
		}catch(Exception e){
			session.removeAttribute("code");
			e.printStackTrace();
			return "0";
		}
	}
	
	// 휴면 해제 - 인증번호 발송
	@PostMapping("/send/awake")
	@ResponseBody
	public String codeSendAwake(HttpServletResponse response,@RequestParam String memEmail
			, HttpSession session) {
		if(memberService.existEmail(memEmail) == 0) {
			return "-1";
		}
		// 랜덤 문자열
		int leftLimit = 97; // 'a'
		int rightLimit = 122; // 'z'
		int codeLength = 10;
		Random rand = new Random();
		StringBuilder sb = new StringBuilder(codeLength);
		for(int i = 0; i < codeLength; i++) {
			int randLimitInt = leftLimit + (int)(rand.nextFloat() * (rightLimit - leftLimit + 1));
			sb.append((char)randLimitInt);
		}
		String code = sb.toString();
		
		// 사용자 인증 이메일 발송 내용
		String from = keysJaewon.getGmail();
		String to = memEmail;
		String subject = "[Tripant] 이메일 인증번호";
		String content = "인증번호: "+code; 
		
		// 이메일 전송 : SMTP 이용을 위함
		Properties p = new Properties();
		p.put("mail.smtp.user", from);
		p.put("mail.smtp.host", "smtp.googlemail.com"); // google SMTP 주소
		p.put("mail.smtp.port", "465");
		p.put("mail.smtp.starttls.enable", "true");
		p.put("mail.smtp.auth", "true");
		p.put("mail.smtp.debug", "true");
		p.put("mail.smtp.socketFactory.port", "465");
		p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		p.put("mail.smtp.socketFactory.fallback", "false");
		p.put("mail.smtp.ssl.protocols", "TLSv1.2"); // 추가된 코드
		p.put("mail.smtp.ssl.enable", "true");  // 추가된 코드
		try{
			Authenticator auth = gmail;
			Session ses = Session.getInstance(p, auth);
			ses.setDebug(true);
			MimeMessage msg = new MimeMessage(ses); 
			msg.setSubject(subject);		// 메일 제목
			Address fromAddr = new InternetAddress(from); 	// 보내는 사람 정보
			msg.setFrom(fromAddr);
			Address toAddr = new InternetAddress(to);		// 받는 사람 정보
			msg.addRecipient(Message.RecipientType.TO, toAddr);
			msg.setContent(content, "text/html;charset=UTF-8");
			Transport.send(msg); // 메세지 전송
			session.setAttribute("code", code);
			return "1";
		}catch(Exception e){
			session.removeAttribute("code");
			e.printStackTrace();
			return "0";
		}
	}
	
	//인증번호 확인
	@PostMapping("/check/awake")
	@ResponseBody
	public int codeCheckAwake(HttpSession session, @RequestParam String inputCode) throws IOException {
		int result = 0;
		if(inputCode.equals(session.getAttribute("code"))) {
			session.removeAttribute("code");
			result = memberService.awake((String) session.getAttribute("memEmail"));
			session.removeAttribute("memEmail");
			return result;
		}else {
			return result;
		}
	}
}
