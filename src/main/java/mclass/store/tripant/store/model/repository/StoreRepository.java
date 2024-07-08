package mclass.store.tripant.store.model.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import mclass.store.tripant.store.domain.ItemEntity;

@Mapper
public interface StoreRepository {
	// 스토어
	// 테마 목록
	List<ItemEntity> themeList(String memEmail);

	// 폰트 목록
	List<ItemEntity> fontList();
	
	// 장바구니에 담기
	int fontDel(String memEmail);
	int insertItems(Map<String, Object> map);
	
	// 장바구니
	// 장바구니 목록
	List<Map<String, Object>> cart(String memEmail);
	
	// 장바구니 삭제
	int cartDel(Map<String, Object> map);
	
	// 구매내역
	// 구매내역 목록
	List<Map<String, Object>> buy(String memEmail);
	
	// 주문정보
	Map<String, Object> buyInfo(String memEmail);
	
	// 구매내역 추가
	int beforePay(Map<String, Object> map);
	int pay(Map<String, Object> map);
}
