package mclass.store.tripant.diary.domain;

import org.springframework.stereotype.Component;
import lombok.Data;

@Data@Component
public class LikeEntity {

//	DIARY_ID  NOT NULL NUMBER        
//	MEM_EMAIL NOT NULL VARCHAR2(100)
	private int diaryId;
	private int likeCount; // 글에 좋아요 수
	private int isLikeMemEmail; // 내가 좋아요 여부
}
