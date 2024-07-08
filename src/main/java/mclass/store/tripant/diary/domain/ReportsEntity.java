package mclass.store.tripant.diary.domain;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class ReportsEntity {
//	DIARY_ID     NOT NULL NUMBER        
//	MEM_EMAIL    NOT NULL VARCHAR2(100) 
//	REPORT_STATE NOT NULL CHAR(1)       
	private Integer diaryId; // 신고된 여행글
	private String memEmail; // 여행글을 신고한 사람
	private Integer reportState; // 0 신고 안한 상태, 1 신고한 상태
}
