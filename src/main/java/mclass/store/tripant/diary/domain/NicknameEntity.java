package mclass.store.tripant.diary.domain;

import java.sql.Date;


import org.springframework.stereotype.Component;

import lombok.Data;

@Data@Component
public class NicknameEntity {

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
	private Date diaryDate; // java.sql.Date 타입으로 변경
	private String diaryOpen;
	private Integer diaryViews;
	private String memNick;	//회원의 닉네임
	
}
