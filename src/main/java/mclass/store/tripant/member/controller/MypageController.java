package mclass.store.tripant.member.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import mclass.store.tripant.member.model.service.MemberService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/my")
public class MypageController {

	private final MemberService memberService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final Gson gson;
	
	@Value("${spring.security.oauth2.client.registration.naver.client-id}")
	private String naverClientId;
	@Value("${spring.security.oauth2.client.registration.naver.client-secret}")
	private String naverClientSecret;
	
	@Value("${spring.security.oauth2.client.registration.google.client-secret}")
	private String googleClientSecret;
	@Value("${spring.security.oauth2.client.registration.google.client-id}")
	private String googleClientId;
	
	// 마이페이지
	@GetMapping("/home")
	public String myHome(Model model, Principal principal) {
		String memEmail = principal.getName();
		Map<String, Object> map = memberService.myInfo(memEmail);
		model.addAttribute("memEmail", map.get("MEM_EMAIL"));
		model.addAttribute("memNick", map.get("MEM_NICK"));
		
		int memType =  Integer.parseInt(String.valueOf(map.get("MEM_TYPE")));
		
		int isKakao = memType & Integer.parseInt("0100", 2);
		int isNaver = memType & Integer.parseInt("0010", 2);
		int isGoogle = memType & Integer.parseInt("0001", 2);
		
		model.addAttribute("isKakao", isKakao);
		model.addAttribute("isNaver", isNaver);
		model.addAttribute("isGoogle", isGoogle);
		
		return "mypage/home";
	}
	
	// 카카오 연동 해제
	@PostMapping("/unlink/kakao")
	@ResponseBody
	public int unlinkKakao(Principal principal) throws IOException, InterruptedException {
		int result = 0;
		String memEmail = principal.getName();
		Map<String, Object> map = memberService.tokenValue(memEmail);
		
		HttpRequest request = HttpRequest.newBuilder()
			    .uri(URI.create("https://kapi.kakao.com/v1/user/unlink"))
			    .header("Content-Type", "application/json")
			    .header("Authorization", "Bearer " + map.get("MEM_TOKEN_KAKAO"))
			    .method("POST", HttpRequest.BodyPublishers.ofString("{}"))
			    .build();
		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		
		if(response.statusCode() == 200) {
			Map<String, Object> myInfo = memberService.myInfo(memEmail);
			Map<String, Object> updateToken = new HashMap<>();
			updateToken.put("memEmail", memEmail);
			int memType = Integer.parseInt(String.valueOf(myInfo.get("MEM_TYPE")));
			memType -= 4;
			updateToken.put("memType", memType);
			result = memberService.updateType(updateToken);
		}else if(response.statusCode() == 401) {
			return result = -1;
		}
		
		return result;
	}

	// 네이버 연동 해제
	@PostMapping("/unlink/naver")
	@ResponseBody
	public int unlinkNaver(Principal principal) throws IOException, InterruptedException {
		int result = 0;
		String memEmail = principal.getName();
		Map<String, Object> map = memberService.tokenValue(memEmail);
		
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("client_id", naverClientId);
		requestBody.put("client_secret", naverClientSecret);
		requestBody.put("access_token", map.get("MEM_TOKEN_NAVER"));
//		requestBody.put("grant_type", "delete");
		
//		import com.github.scribejava.core.builder.api.DefaultApi20;
//		OAuth20Service
		
		
		HttpRequest request = HttpRequest.newBuilder()
//			    .header("Content-Type", "application/json")
//			    .header("Content-Type", "application/x-www-form-urlencoded")
//			    .header("Authorization", "Bearer " + map.get("MEM_TOKEN_KAKAO"))
			    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
			    .uri(URI.create("https://nid.naver.com/oauth2.0/token?grant_type=delete&"))
			    .build();
		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println("body >>>>>>>>>> " + gson.toJson(requestBody));
		System.out.println("body >>>>>>>>>> " + HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)));
		System.out.println("response >>>>>>>>>> " + response.body());

//		String url = "https://nid.naver.com/oauth2.0/token"; /*URL*/
//        url += "?client_id=" + naverClientId; /*Service Key*/
//        url += "&client_secret=" + naverClientSecret; /*시도 이름(전국, 서울, 부산, 대구, 인천, 광주, 대전, 울산, 경기, 강원, 충북, 충남, 전북, 전남, 경북, 경남, 제주, 세종)*/
//        url += "&access_token=" + map.get("MEM_TOKEN_NAVER"); /*xml 또는 json*/
//        url += "&grant_type=delete";
//        
//        URL requestUrl = new URL(url);
//        HttpURLConnection urlConnection = (HttpURLConnection) requestUrl.openConnection();
//        urlConnection.setRequestMethod("GET");
//        
//        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//        String line = null;
//        System.out.println("NAVER-URL--- RESPONSE");
//        while ((line = br.readLine()) != null) {
//            System.out.println(line);
//        }
//        
//        br.close();
//        urlConnection.disconnect();
		
		return result;
	}
	
	/** 네이버 접근 토큰 삭제 요청하기 **/
//	public void deleteInfo(OAuth2AccessToken oauthToken, String access_token) throws IOException{		
//		String getAccessTokenEndpoint= "https://nid.naver.com/oauth2.0/token";
//		OAuth20Service oauthService = new ServiceBuilder()
//				.apiKey(CLIENT_ID)
//    			.apiSecret(CLIENT_SECRET)
//    			.callback(REDIRECT_URI).build(NaverLoginApi.instance());
//		/** 접근 토큰 삭제 요청을 위한 setting
//		  		
//		URL에 보낼 정보
//		=> https://nid.naver.com/oauth2.0/token?grant_type=delete&client_id=CLIENT_ID&client_secret=CLIENT_SECRET&access_token=ACCESS_TOKEN&service_provider=NAVER
//		
//		**/		
//		OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, getAccessTokenEndpoint, oauthService);
//		oAuthRequest.addQuerystringParameter("client_id", CLIENT_ID);
//        oAuthRequest.addQuerystringParameter("client_secret", CLIENT_SECRET);
//        oAuthRequest.addQuerystringParameter("access_token", access_token);
//        oAuthRequest.addQuerystringParameter("grant_type", "delete");
//        oAuthRequest.addQuerystringParameter("service_provider", "NAVER");
//		
//		oauthService.signRequest(oauthToken, oAuthRequest);
//		
//		Response response = oAuthRequest.send();		
//		System.out.println("\n[NAVER에 접근 토큰 삭제 요청결과]\n\t=>" + response.getBody());
//	}
	
	
	
	
	// 구글 연동 해제
	@PostMapping("/unlink/google")
	@ResponseBody
	public int unlinkGoogle(Principal principal) throws IOException, InterruptedException {
		int result = 0;
		String memEmail = principal.getName();
		Map<String, Object> map = memberService.tokenValue(memEmail);
		
		googleRefreshToken(principal);
		
		return result;
	}
	
	// 구글 갱신 토큰 받기
	public String googleRefreshToken(Principal principal) throws IOException, InterruptedException {
		String memEmail = principal.getName();
		Map<String, Object> map = memberService.tokenValue(memEmail);
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("token", "Bearer " + map.get("MEM_TOKEN_GOOGLE"));
		requestBody.put("returnSecureToken", true);
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://identitytoolkit.googleapis.com/v1/accounts:signInWithCustomToken?key="+googleClientId))
				.header("Content-Type", "application/json")
//				.header("Authorization", "Bearer " + map.get("MEM_TOKEN_GOOGLE"))
				.method("POST", HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
				.build();
		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println("구글갱신토큰 = "+response.body());
		return "";
	}
	
	// 닉네임 변경 페이지
	@GetMapping("/nick")
	public String myNick() {
		return "mypage/chNick";
	}

	// 닉네임 변경
	@PostMapping("/nick")
	@ResponseBody
	public int saveNick(String memNick, Principal principal) {
		String memEmail = principal.getName();
		Map<String, Object> map = new HashMap<>();
		map.put("memNick", memNick);
		map.put("memEmail", memEmail);
		int result = memberService.saveNick(map);
		return result;
	}

	// 비밀번호 변경 페이지
	@GetMapping("/pwd")
	public String myPwd() {
		return "mypage/chPwd";
	}
	
	// 비밀번호 변경
	@PostMapping("/pwd")
	@ResponseBody
	public int savePwd(String memPassword, Principal principal) {
		String memEmail = principal.getName();
		Map<String, Object> map = new HashMap<>();
		map.put("memPassword", bCryptPasswordEncoder.encode(memPassword));
		map.put("memEmail", memEmail);
		int result = memberService.savePwd(map);
		return result;
	}

	// 회원 탈퇴 페이지
	@GetMapping("/quit")
	public String myQuit() {
		return "mypage/quit";
	}
	
	// 현재 비밀번호
	@PostMapping("/pwd/use")
	@ResponseBody
	public int pwdUse(String memPassword, Principal principal) {
		String memEmail = principal.getName();
		String currPwd = memberService.currPwd(memEmail);
		if(bCryptPasswordEncoder.matches(memPassword, currPwd)) {
			return 1;
		}else {
			return 0;
		}
	}
	
	// 회원 탈퇴
	@PostMapping("/quit")
	@ResponseBody
	public int myQuit(Principal principal) {
		String memEmail = principal.getName();
		int result = memberService.memQuit(memEmail);
		return result;
	}

}
