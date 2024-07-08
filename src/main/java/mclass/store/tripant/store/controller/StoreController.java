package mclass.store.tripant.store.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import mclass.store.tripant.store.domain.ItemEntity;
import mclass.store.tripant.store.model.service.StoreService;


@Controller
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {
	
	@Value("${pay.secret}")
	private String paySecret;
	@Value("${pay.storeId}")
	private String storeId;
	@Value("${pay.channelKey}")
	private String channelKey;
	
	private final StoreService storeService;
	private final Gson gson;
	
	// 스토어 페이지
	@GetMapping("")
	public ModelAndView store(ModelAndView mv, Principal principal) {
		mv.setViewName("store/home");
		String memEmail;
		if(principal != null) {
			memEmail = principal.getName();
		}else {
			memEmail = "";
		}
		List<ItemEntity> themeList = storeService.themeList(memEmail);
		List<ItemEntity> fontList = storeService.fontList();
		if(themeList.size() > 0) {
			mv.addObject("themeList", themeList);
		}
		mv.addObject("fontList", fontList);
		return mv;
	}
	
	// 장바구니에 담기
	@PostMapping("/insert")
	@ResponseBody
	public int storeInsert(@RequestParam List<String> items, Principal principal) {
		int size = items.size();
		Map<String, Object> map = new HashMap<>();
		List<String> list = new ArrayList<>();
		String memEmail = principal.getName();
		map.put("memEmail", memEmail);
		for(String itemCode : items) {
			list.add(itemCode);
		}
		map.put("list", list);
		int insertNum = storeService.insertItems(memEmail, map);
		int result;
		if(size == insertNum) {
			result = 1;
		}else {
			result = 0;
		}
		return result;
	}
	
	// 장바구니 페이지
	@GetMapping("/cart")
	public String storeCart(/*ModelAndView mv*/Model model, Principal principal) {
		model.addAttribute("storeId", storeId);
		model.addAttribute("channelKey", channelKey);
		if(principal != null) {
		
			// 사용자 이메일
			String memEmail = principal.getName();
			model.addAttribute("memEmail", memEmail);
			
			Map<String, Object> map = storeService.buyInfo(memEmail);
			
			// 사용자 닉네임
			String memNick = (String) map.get("MEM_NICK");
			model.addAttribute("memNick", memNick);
			
			// 사용자 휴대폰 번호
			String memTel = (String) map.get("MEM_TEL");
			model.addAttribute("memTel", memTel);
			
			// 주문번호
			int buyId = Integer.parseInt(String.valueOf(map.get("BUY_ID")));
			model.addAttribute("buyId", buyId);
			
			List<Map<String, Object>> list = storeService.cart(memEmail);
			if(list.size() > 0) {
				model.addAttribute("cart", list);
			}
		}
//		mv.setViewName("store/cart");
		return "store/cart";
	}
	
	// 결제 검증
	@PostMapping("/payment")
	@ResponseBody
	public int storePayment(String paymentId, String totalAmount, String[] items, String buyId, Principal principal) throws IOException, InterruptedException{
		
		// paymentId로 결제 단건 조회 API 호출
		HttpRequest request = HttpRequest.newBuilder()
			    .uri(URI.create("https://api.portone.io/payments/"+paymentId+"?storeId="+storeId))
			    .header("Content-Type", "application/json")
			    .header("Authorization", "PortOne "+paySecret)
			    .method("GET", HttpRequest.BodyPublishers.ofString("{}"))
			    .build();
		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		// 결제 단건 조회 응답
		Map<String, Object> responseBody = gson.fromJson(response.body(), Map.class);
		
		// 응답 중 결제 금액 세부 정보 항목 추출
		Map<String, Object> amount = gson.fromJson(gson.toJson(responseBody.get("amount")), Map.class);
		// 그 중 지불된 금액
		double paid = (double) amount.get("paid");
		
		int result;
		// 결제 금액과 지불된 금액이 같다면
		if(Double.parseDouble(totalAmount) == paid) {
			Map<String, Object> map = new HashMap<>();
			map.put("memEmail", principal.getName());
			map.put("buyId", buyId);
			List<String> list = new ArrayList<>();
			for(int i = 0; i < items.length; i++) {
				list.add(items[i]);
			}
			map.put("list", list);
			result = storeService.pay(map);
			return 1;
		}else {
			return result = 0;
		}
	}
	
	// 장바구니 삭제
	@PostMapping("/cart/del")
	@ResponseBody
	public int storeCartDel(@RequestParam List<String> items, Principal principal) {
		int size = items.size();
		Map<String, Object> map = new HashMap<>();
		String memEmail = principal.getName();
		map.put("memEmail", memEmail);
		map.put("itemCode", items);
		int delNum = storeService.cartDel(map);
		int result;
		if(size == delNum) {
			result = 1;
		}else {
			result = 0;
		}
		return result;
	}
	
	// 구매내역 페이지
	@GetMapping("/buy")
	public ModelAndView storeBuy(ModelAndView mv, Principal principal) {
		mv.setViewName("store/buy");
		String memEmail;
		if(principal != null) {
			memEmail = principal.getName();
		}else {
			memEmail = "";
		}
		List<Map<String, Object>> list = storeService.buy(memEmail);
		if(list.size() > 0) {
			mv.addObject("list", list);
		}
		return mv;
	}
}