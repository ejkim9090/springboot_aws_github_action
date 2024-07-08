package mclass.store.tripant.diary.domain;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data@Component
public class WritePlanTitleEntity {
//	PLAN_ID         NOT NULL NUMBER  
//	PLAN_TITLE      NOT NULL VARCHAR2(100)
	private Integer planId;
	private String planTitle;
	private String diaryMemEmail;
	
}
