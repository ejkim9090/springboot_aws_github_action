package mclass.store.tripant.store.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mclass.store.tripant.store.domain.ItemEntity;
import mclass.store.tripant.store.model.repository.StoreRepository;

@Service
@RequiredArgsConstructor
public class StoreService {

	private final StoreRepository storeRepository;

	// 스토어
	// 테마 목록
	public List<ItemEntity> themeList(String memEmail) {
		return storeRepository.themeList(memEmail);
	}

	// 폰트 목록
	public List<ItemEntity> fontList() {
		return storeRepository.fontList();
	}
	
	// 장바구니에 담기
	@Transactional
	public int insertItems(String memEmail, Map<String, Object> map) {
		storeRepository.fontDel(memEmail);
		int result = storeRepository.insertItems(map);
		return result;
	}
	
	// 장바구니
	// 장바구니 목록
	public List<Map<String, Object>> cart(String memEmail){
		return storeRepository.cart(memEmail);
	}
	
	// 장바구니 삭제
	public int cartDel(Map<String, Object> map) {
		return storeRepository.cartDel(map);
	}
	
	// 구매내역
	// 구매내역 목록
	public List<Map<String, Object>> buy(String memEmail){
		return storeRepository.buy(memEmail);
	}
	
	// 구매내역 주문번호
	public Map<String, Object> buyInfo(String memEmail) {
		return storeRepository.buyInfo(memEmail);
	}
	
	// 구매내역 추가
	@Transactional
	public int pay(Map<String, Object> map) {
		storeRepository.beforePay(map);
		int result = storeRepository.pay(map);
		return result;
	}
}
