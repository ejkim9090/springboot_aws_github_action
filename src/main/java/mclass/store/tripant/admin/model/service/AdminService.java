package mclass.store.tripant.admin.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mclass.store.tripant.admin.domain.AdminBoardEntity;
import mclass.store.tripant.admin.domain.AdminMemEntity;
import mclass.store.tripant.admin.domain.AdminStoreEntity;
import mclass.store.tripant.admin.model.repository.AdminRepository;

@Service
public class AdminService {
	
	@Autowired
	private AdminRepository admindao;
	
	//회원리스트
	public Map<String, Object> selectMemList( int num, int pageNum, int currentPageNum, String searchMem) {					
		
		//현재페이지: currentPage
		//하단에 표시할 페이지 수: memPageNum
		//화면에 한번에 표시되는  글 수 : memNum	
		Map<String, Object> result = null;
		
		//총 게시글 개수
		int totalCount = admindao.totalCount();
		
		int startRownum = num * (currentPageNum - 1) + 1;
		int endRownum = num * currentPageNum;
		
//		전체페이지수(총 게시글 개수/한 페이지 당 글 수) => (총 게시글 개수%한 페이지 당 글 수== 0)?(총 게시글 개수/한 페이지 당 글 수):(총 게시글 개수/한 페이지 당 글 수+1)
		int totalPageCount = (totalCount % num == 0) ? (totalCount / num) : (totalCount / num) + 1;
		// 조건문 - 앞에가 0이 맞으면 : 앞에꺼, 0이 아니면 : 뒤에꺼
		
		//시작페이지
		int startPageNum = (currentPageNum % pageNum == 0) ? ((currentPageNum / pageNum) - 1) * pageNum + 1
				: (currentPageNum / pageNum) * pageNum + 1;
		
		//끝페이지
		int endPageNum = (startPageNum + pageNum > totalPageCount) ? totalPageCount : startPageNum + pageNum - 1;
		
		List<AdminMemEntity> memList = admindao.selectMemList(startRownum, endRownum, searchMem);
		result = new HashMap<String, Object>();
		result.put("memList", memList);
		result.put("totalCount", totalCount);
		result.put("totalPageCount", totalPageCount);
		result.put("startPageNum", startPageNum);
		result.put("endPageNum", endPageNum);
		result.put("currentPage", currentPageNum);
		result.put("searchMem", searchMem);
		
		return result;
	}

	//회원검색
	public Map<String, Object> search(int num, int pageNum, int currentPageNum, String searchMem){
		
		Map<String, Object> result = null;
		
		//총 게시글 개수
		int totalCount = admindao.totalCountSearch(searchMem);
		
		int startRownum = num * (currentPageNum - 1) + 1;
		int endRownum = num * currentPageNum;
		
//		전체페이지수(총 게시글 개수/한 페이지 당 글 수) => (총 게시글 개수%한 페이지 당 글 수== 0)?(총 게시글 개수/한 페이지 당 글 수):(총 게시글 개수/한 페이지 당 글 수+1)
		int totalPageCount = (totalCount % num == 0) ? (totalCount / num) : (totalCount / num) + 1;
		// 조건문 - 앞에가 0이 맞으면 : 앞에꺼, 0이 아니면 : 뒤에꺼
		
		//시작페이지
		int startPageNum = (currentPageNum % pageNum == 0) ? ((currentPageNum / pageNum) - 1) * pageNum + 1
				: (currentPageNum / pageNum) * pageNum + 1;
		
		//끝페이지
		int endPageNum = (startPageNum + pageNum > totalPageCount) ? totalPageCount : startPageNum + pageNum - 1;
		
		List<AdminMemEntity> memList = admindao.selectMemListSearch(startRownum, endRownum, searchMem);
		result = new HashMap<String, Object>();
		result.put("memList", memList);
		result.put("totalCount", totalCount);
		result.put("totalPageCount", totalPageCount);
		result.put("startPageNum", startPageNum);
		result.put("endPageNum", endPageNum);
		result.put("currentPage", currentPageNum);
		result.put("searchMem", searchMem);

		return result;
	}
	
	//등급변경 활성화 여부
	public Integer adminMemInfo(Map<String, Object> map) {
		return admindao.adminMemInfo(map);
	}
	
	//게시글리스트
	public Map<String, Object> boardList(int num, int pageNum, int currentPageNum,String pick,String search){
		Map<String, Object> result = null;
		
		//총 게시글 개수
		int totalCount = admindao.diaryCount();
		
		int startRownum = num * (currentPageNum - 1) + 1;
		int endRownum = num * currentPageNum;
		
//		전체페이지수(총 게시글 개수/한 페이지 당 글 수) => (총 게시글 개수%한 페이지 당 글 수== 0)?(총 게시글 개수/한 페이지 당 글 수):(총 게시글 개수/한 페이지 당 글 수+1)
		int totalPageCount = (totalCount % num == 0) ? (totalCount / num) : (totalCount / num) + 1;
		
		//시작페이지
		int startPageNum = (currentPageNum % pageNum == 0) ? ((currentPageNum / pageNum) - 1) * pageNum + 1
				: (currentPageNum / pageNum) * pageNum + 1;
		
		//끝페이지
		int endPageNum = (startPageNum + pageNum > totalPageCount) ? totalPageCount : startPageNum + pageNum - 1;
		
		List<AdminBoardEntity> boardList = admindao.boardList(startRownum, endRownum, pick,search);
		result = new HashMap<String, Object>();
		result.put("boardList", boardList);
		result.put("totalCount", totalCount);
		result.put("totalPageCount", totalPageCount);
		result.put("startPageNum", startPageNum);
		result.put("endPageNum", endPageNum);
		result.put("currentPage", currentPageNum);
		result.put("pick", pick);
		result.put("search", search);

		return result;
	}
	
	//게시글 조건검색(select)
	public Map<String, Object> keywordSearch(int num, int pageNum, int currentPageNum,String pick,String search){
		Map<String, Object> result = null;
		
		//총 게시글 개수
		int totalCount = admindao.keywordSearchCount(pick,search);
		
		int startRownum = num * (currentPageNum - 1) + 1;
		int endRownum = num * currentPageNum;
		
//		전체페이지수(총 게시글 개수/한 페이지 당 글 수) => (총 게시글 개수%한 페이지 당 글 수== 0)?(총 게시글 개수/한 페이지 당 글 수):(총 게시글 개수/한 페이지 당 글 수+1)
		int totalPageCount = (totalCount % num == 0) ? (totalCount / num) : (totalCount / num) + 1;
		
		//시작페이지
		int startPageNum = (currentPageNum % pageNum == 0) ? ((currentPageNum / pageNum) - 1) * pageNum + 1
				: (currentPageNum / pageNum) * pageNum + 1;
		
		//끝페이지
		int endPageNum = (startPageNum + pageNum > totalPageCount) ? totalPageCount : startPageNum + pageNum - 1;
		
		List<AdminBoardEntity> boardList = admindao.keywordSearch(startRownum, endRownum, pick,search);
		result = new HashMap<String, Object>();
		result.put("boardList", boardList);
		result.put("totalCount", totalCount);
		result.put("totalPageCount", totalPageCount);
		result.put("startPageNum", startPageNum);
		result.put("endPageNum", endPageNum);
		result.put("currentPage", currentPageNum);
		result.put("pick", pick);
		result.put("search", search);

		return result;
	}

	//좋아요 정렬
	public List<AdminBoardEntity> boardLikes() {
		
		return admindao.boardLike();
	}
	//조회수 정렬
	public List<AdminBoardEntity> boardView() {
			
		return admindao.boardView();
	}
		
	//신고게시글
	public Map<String, Object> complainList(int num, int pageNum, int currentPageNum, String searchMem) {					
		
		//현재페이지: currentPage
		//하단에 표시할 페이지 수: memPageNum
		//화면에 한번에 표시되는  글 수 : memNum	
		Map<String, Object> result = null;
		
		//총 게시글 개수
		int totalCount = admindao.boardCount();
		
		int startRownum = num * (currentPageNum - 1) + 1;
		int endRownum = num * currentPageNum;
		
//		전체페이지수(총 게시글 개수/한 페이지 당 글 수) => (총 게시글 개수%한 페이지 당 글 수== 0)?(총 게시글 개수/한 페이지 당 글 수):(총 게시글 개수/한 페이지 당 글 수+1)
		int totalPageCount = (totalCount % num == 0) ? (totalCount / num) : (totalCount / num) + 1;
		
		//시작페이지
		int startPageNum = (currentPageNum % pageNum == 0) ? ((currentPageNum / pageNum) - 1) * pageNum + 1
				: (currentPageNum / pageNum) * pageNum + 1;
		
		//끝페이지
		int endPageNum = (startPageNum + pageNum > totalPageCount) ? totalPageCount : startPageNum + pageNum - 1;
		
		List<AdminBoardEntity> complainList = admindao.complainList(startRownum, endRownum, searchMem);
		result = new HashMap<String, Object>();
		result.put("complainBoard", complainList);
		result.put("totalCount", totalCount);
		result.put("totalPageCount", totalPageCount);
		result.put("startPageNum", startPageNum);
		result.put("endPageNum", endPageNum);
		result.put("currentPage", currentPageNum);
		result.put("searchMem", searchMem);
		
		return result;
	}	
	
	//신고게시글 검색
	public  Map<String, Object> complainsearch(int num, int pageNum, int currentPageNum, String searchMem){
		
		Map<String, Object> result = null;
		
		//총 게시글 개수
		int totalCount = admindao.boardCountSearch(searchMem);
		
		int startRownum = num * (currentPageNum - 1) + 1;
		int endRownum = num * currentPageNum;
		
//		전체페이지수(총 게시글 개수/한 페이지 당 글 수) => (총 게시글 개수%한 페이지 당 글 수== 0)?(총 게시글 개수/한 페이지 당 글 수):(총 게시글 개수/한 페이지 당 글 수+1)
		int totalPageCount = (totalCount % num == 0) ? (totalCount / num) : (totalCount / num) + 1;
		
		//시작페이지
		int startPageNum = (currentPageNum % pageNum == 0) ? ((currentPageNum / pageNum) - 1) * pageNum + 1
				: (currentPageNum / pageNum) * pageNum + 1;
		
		//끝페이지
		int endPageNum = (startPageNum + pageNum > totalPageCount) ? totalPageCount : startPageNum + pageNum - 1;
		
		List<AdminBoardEntity> complainList = admindao.complainList(startRownum, endRownum, searchMem);
		result = new HashMap<String, Object>();
		result.put("complainBoard", complainList);
		result.put("totalCount", totalCount);
		result.put("totalPageCount", totalPageCount);
		result.put("startPageNum", startPageNum);
		result.put("endPageNum", endPageNum);
		result.put("currentPage", currentPageNum);
		result.put("searchMem", searchMem);
		
		return result;
	}
	
	//신고수 초기화
	public Integer complainReset(Integer diaryId) {
		return admindao.complainReset(diaryId);
	}
	
	//신고수 정렬
	public List<AdminBoardEntity> boardReport(){
		return admindao.boardReport();
	}
	
	// 결제 취소 페이지
	// 결제 목록
	public Map<String, Object> payList(int num, int pageNum, int currentPageNum, String searchMem){
		
		Map<String, Object> result = null;
		
		//총 게시글 개수
		int totalCount = admindao.payCount();
		
		int startRownum = num * (currentPageNum - 1) + 1;
		int endRownum = num * currentPageNum;
		
//		전체페이지수(총 게시글 개수/한 페이지 당 글 수) => (총 게시글 개수%한 페이지 당 글 수== 0)?(총 게시글 개수/한 페이지 당 글 수):(총 게시글 개수/한 페이지 당 글 수+1)
		int totalPageCount = (totalCount % num == 0) ? (totalCount / num) : (totalCount / num) + 1;
		
		//시작페이지
		int startPageNum = (currentPageNum % pageNum == 0) ? ((currentPageNum / pageNum) - 1) * pageNum + 1
				: (currentPageNum / pageNum) * pageNum + 1;
		
		//끝페이지
		int endPageNum = (startPageNum + pageNum > totalPageCount) ? totalPageCount : startPageNum + pageNum - 1;
		
		
		List<AdminStoreEntity> cancelList = admindao.payList(startRownum, endRownum, searchMem);
		result = new HashMap<String, Object>();
		result.put("map", cancelList);
		result.put("totalCount", totalCount);
		result.put("totalPageCount", totalPageCount);
		result.put("startPageNum", startPageNum);
		result.put("endPageNum", endPageNum);
		result.put("currentPage", currentPageNum);
		result.put("searchMem", searchMem);
		
		return result;
		
	}

	//결제취소 회원 검색
	public Map<String, Object> cancelSearch(int num, int pageNum, int currentPageNum, String searchMem){
		Map<String, Object> result = null;
		
		//총 게시글 개수
		int totalCount = admindao.payCountSearch(searchMem);
		
		int startRownum = num * (currentPageNum - 1) + 1;
		int endRownum = num * currentPageNum;
		
//		전체페이지수(총 게시글 개수/한 페이지 당 글 수) => (총 게시글 개수%한 페이지 당 글 수== 0)?(총 게시글 개수/한 페이지 당 글 수):(총 게시글 개수/한 페이지 당 글 수+1)
		int totalPageCount = (totalCount % num == 0) ? (totalCount / num) : (totalCount / num) + 1;
		
		//시작페이지
		int startPageNum = (currentPageNum % pageNum == 0) ? ((currentPageNum / pageNum) - 1) * pageNum + 1
				: (currentPageNum / pageNum) * pageNum + 1;
		
		//끝페이지
		int endPageNum = (startPageNum + pageNum > totalPageCount) ? totalPageCount : startPageNum + pageNum - 1;
		
		
		List<AdminStoreEntity> cancelList = admindao.payList(startRownum, endRownum, searchMem);
		result = new HashMap<String, Object>();
		result.put("map", cancelList);
		result.put("totalCount", totalCount);
		result.put("totalPageCount", totalPageCount);
		result.put("startPageNum", startPageNum);
		result.put("endPageNum", endPageNum);
		result.put("currentPage", currentPageNum);
		result.put("searchMem", searchMem);
		
		return result;
	}
	
	// 결제 취소
	public int payCancel(Map<String, Object> map) {
		return admindao.payCancel(map);
	}
	
	// 상품 관리 페이지
	// 상품목록
	public Map<String, Object> itemList(int num, int pageNum, int currentPageNum,String itemCode){
		Map<String, Object> result = null;
		
		//총 게시글 개수
		int totalCount = admindao.itemCount();
		
		int startRownum = num * (currentPageNum - 1) + 1;
		int endRownum = num * currentPageNum;
		
//		전체페이지수(총 게시글 개수/한 페이지 당 글 수) => (총 게시글 개수%한 페이지 당 글 수== 0)?(총 게시글 개수/한 페이지 당 글 수):(총 게시글 개수/한 페이지 당 글 수+1)
		int totalPageCount = (totalCount % num == 0) ? (totalCount / num) : (totalCount / num) + 1;
		
		//시작페이지
		int startPageNum = (currentPageNum % pageNum == 0) ? ((currentPageNum / pageNum) - 1) * pageNum + 1
				: (currentPageNum / pageNum) * pageNum + 1;
		
		//끝페이지
		int endPageNum = (startPageNum + pageNum > totalPageCount) ? totalPageCount : startPageNum + pageNum - 1;
		
		
		List<AdminStoreEntity> goodsList = admindao.itemList(startRownum, endRownum,itemCode);
		result = new HashMap<String, Object>();
		result.put("goodsList", goodsList);
		result.put("totalCount", totalCount);
		result.put("totalPageCount", totalPageCount);
		result.put("startPageNum", startPageNum);
		result.put("endPageNum", endPageNum);
		result.put("currentPage", currentPageNum);
		result.put("itemCode", itemCode);
		
		return result;
	}
	
	//상품검색
	public Map<String, Object> itemListSearch(int num, int pageNum, int currentPageNum,String itemCode){
		Map<String, Object> result = null;
		
		//총 게시글 개수
		int totalCount = admindao.itemSearchCount(itemCode);
		
		int startRownum = num * (currentPageNum - 1) + 1;
		int endRownum = num * currentPageNum;
		
//		전체페이지수(총 게시글 개수/한 페이지 당 글 수) => (총 게시글 개수%한 페이지 당 글 수== 0)?(총 게시글 개수/한 페이지 당 글 수):(총 게시글 개수/한 페이지 당 글 수+1)
		int totalPageCount = (totalCount % num == 0) ? (totalCount / num) : (totalCount / num) + 1;
		
		//시작페이지
		int startPageNum = (currentPageNum % pageNum == 0) ? ((currentPageNum / pageNum) - 1) * pageNum + 1
				: (currentPageNum / pageNum) * pageNum + 1;
		
		//끝페이지
		int endPageNum = (startPageNum + pageNum > totalPageCount) ? totalPageCount : startPageNum + pageNum - 1;
		
		
		List<AdminStoreEntity> goodsList = admindao.itemList(startRownum, endRownum,itemCode);
		result = new HashMap<String, Object>();
		result.put("goodsList", goodsList);
		result.put("totalCount", totalCount);
		result.put("totalPageCount", totalPageCount);
		result.put("startPageNum", startPageNum);
		result.put("endPageNum", endPageNum);
		result.put("currentPage", currentPageNum);
		result.put("itemCode", itemCode);
		
		return result;
	}
	
	// 상품정보
	public Map<String, Object> itemInfo(String itemCode){
		return admindao.itemInfo(itemCode);
	}
	// 상품추가
	public int itemInsert(Map<String, Object> map) {
		return admindao.itemInsert(map);
	}
	// 상품수정
	public int itemUpdate(Map<String, Object> map) {
		return admindao.itemUpdate(map);
	}
	//상품 삭제
	public int itemDelete(String itemCode) {
		return admindao.itemDelete(itemCode);
	}
	
}
