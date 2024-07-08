package mclass.store.tripant.diary.domain;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class CloudinaryPostEntity {
//	DIARY_ID        NOT NULL NUMBER         
//	DIARY_MEM_EMAIL NOT NULL VARCHAR2(100)  
//	DLARY_IMAGE              CLOB           
//	DLARY_PREVIEW            VARCHAR2(1500) 

	private Integer diaryId;
	private String diaryMemEmail;
	private String diaryImage;
	private String diaryPreview;
}
