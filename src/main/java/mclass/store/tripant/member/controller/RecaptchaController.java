package mclass.store.tripant.member.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import mclass.store.tripant.apikeys.KeysJaewon;
import mclass.store.tripant.config.RecaptchaConfig;

@RestController
@RequiredArgsConstructor
public class RecaptchaController {

	private final KeysJaewon keysJaewon;
	
	@PostMapping("/robot/join")
	@ResponseBody
	public int robotJoin(HttpServletRequest request) {
		RecaptchaConfig.setSecretKey(keysJaewon.getRobotSecret());
		String gRecaptchaResponse = request.getParameter("recaptcha");
		try {
			if(RecaptchaConfig.verify(gRecaptchaResponse)) {
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
