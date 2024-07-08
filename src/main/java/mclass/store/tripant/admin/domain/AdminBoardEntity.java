package mclass.store.tripant.admin.domain;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class AdminBoardEntity {

	private Integer diaryId;
	private String diaryTitle;
	private String diaryDate;
	private String memNick;
	
	private Integer diaryViews; //조회수
	private Integer likes; //좋아요수
	private Integer reports;  //신고수
	
}
