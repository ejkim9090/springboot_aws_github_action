package mclass.store.tripant.admin.model.repository;



import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import mclass.store.tripant.admin.domain.AdminBoardEntity;
import mclass.store.tripant.admin.domain.AdminMemEntity;
import mclass.store.tripant.admin.domain.AdminStoreEntity;


@Mapper
public interface AdminRepository {
	
	//회원관리
	//회원정보 한페이지에 나열
	public List<AdminMemEntity> selectMemList(int startRownum, int endRownum, String searchMem);
	//회원검색
	public List<AdminMemEntity> selectMemListSearch(int startRownum, int endRownum, String searchMem);
	
	//회원 등급 변경 활성화 여부 
	public Integer adminMemInfo(Map<String, Object> map);
	
	//개수
	public int totalCount();
	public int totalCountSearch(String searchMem);
	
	//게시글관리
	//전체게시글
	public List<AdminBoardEntity> boardList(int startRownum, int endRownum, String pick,String search);
	public int diaryCount();
	public int keywordSearchCount(String pick,String search);
	
	//게시글 검색(select)
	public List<AdminBoardEntity> keywordSearch(int startRownum, int endRownum, String pick,String search);
	
	//좋아요 많은 순으로 정렬
	public List<AdminBoardEntity> boardLike();
	//조회수 정렬
	public List<AdminBoardEntity> boardView();
	
	//신고게시글 관리
	//신고게시글
	public List<AdminBoardEntity> complainList(int startRownum, int endRownum,String searchMem);
	public int boardCount();
	public int boardCountSearch(String searchMem);
	//신고게시글 검색
	public List<AdminBoardEntity> complainsearch(String memNick);
	
	//신고수 초기화
	public Integer complainReset(Integer diaryId);
	
	//신고수 정렬
	public List<AdminBoardEntity> boardReport();

	// 결제 취소 페이지
	// 결제 목록
	public List<AdminStoreEntity> payList(int startRownum, int endRownum,String searchMem);
	public int payCount();
	public int payCountSearch(String searchMem);
	
	//결제 취소 회원 검색
	public List<AdminStoreEntity> cancelSearch(String memNick);
		
	// 결제 취소
	public int payCancel(Map<String, Object> map);
	
	// 상품 관리 페이지
	// 상품목록
	public List<AdminStoreEntity> itemList(int startRownum, int endRownum,String itemCode);
	public int itemCount();
	public int itemSearchCount(String itemCode);
	//상품검색
	public List<AdminStoreEntity> itemsearch(int startRownum, int endRownum,String itemCode);
		
	// 상품정보
	public Map<String, Object> itemInfo(String itemCode);
	// 상품추가
	public int itemInsert(Map<String, Object> map); 
	// 상품수정
	public int itemUpdate(Map<String, Object> map);
	//상품삭제
	public int  itemDelete(String itemCode);
	
	
}
