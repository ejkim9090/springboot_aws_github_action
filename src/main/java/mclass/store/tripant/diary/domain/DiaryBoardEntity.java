package mclass.store.tripant.diary.domain;


import org.springframework.stereotype.Component;
import lombok.Data;
import lombok.Setter;

@Data
@Component
@Setter
public class DiaryBoardEntity {

//	DIARY_ID        NOT NULL NUMBER         
//	DIARY_MEM_EMAIL NOT NULL VARCHAR2(100)  
//	DIARY_PLAN_ID   NOT NULL NUMBER         
//	DIARY_TITLE     NOT NULL VARCHAR2(100)  
//	DIARY_CONTENT   NOT NULL VARCHAR2(4000) 
//	DIARY_DATE      NOT NULL DATE           
//	DIARY_OPEN      NOT NULL CHAR(1)        
//	DIARY_VIEWS              NUMBER         
//	DIARY_THEME              VARCHAR2(50)
	private Integer diaryId;
	private String diaryMemEmail;
	private Integer diaryPlanId;
	private String diaryTitle;
	private String diaryContent;
//	private List<String> diaryContentArrList;
	private String diaryDate;
	private String diaryOpen;
	private Integer diaryViews;
	private String diaryTheme;
	private String memNick;
	private Integer isMyLikes;
	private String diaryImage; // 대표 이미지 -  
	private String diaryPreview;	// 미리보기 글 
	

//	public void setDiaryContent(String diaryContent) {
//		this.diaryContent = diaryContent;
//
//		// 추가로 UTF-8 - 4000byte List<String> diaryContentArrList
//		this.diaryContentArrList = StringLengthApi.getMaxByteStringList(diaryContent, 4000, 1000);
//		for (String item : diaryContentArrList) {
//			System.out.println("---- : "+ item.getBytes().length);
//			System.out.println(item);
//		}
//	}
}
