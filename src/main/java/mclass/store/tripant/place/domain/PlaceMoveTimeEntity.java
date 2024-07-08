package mclass.store.tripant.place.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceMoveTimeEntity {
//	TYPE            NOT NULL NUMBER       
//	CONTENTID_START NOT NULL NUMBER       
//	CONTENTID_END   NOT NULL NUMBER       
//	MOVE_TIME       NOT NULL VARCHAR2(50) 
//	AREACODE        NOT NULL NUMBER 
	
	private Integer typeStart;
	private Integer contentidStart;
	private Integer typeEnd;
	private Integer contentidEnd;
	private String moveTime;
}
