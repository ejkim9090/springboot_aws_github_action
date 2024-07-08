package mclass.store.tripant.place.domain;

import lombok.Data;

@Data 
public class AreaEntity {
//	AREA_CODE       NOT NULL NUMBER         
//	AREA_NAME       NOT NULL VARCHAR2(50)   
//	AREA_SHORT_NAME NOT NULL VARCHAR2(10)   
//	AREA_ENG_NAME   NOT NULL VARCHAR2(50)   
//	AREA_FILE_NAME  NOT NULL VARCHAR2(50)   
//	AREA_LANDMARK   NOT NULL VARCHAR2(50)   
//	AREA_X          NOT NULL VARCHAR2(50)   
//	AREA_Y          NOT NULL VARCHAR2(50)   
//	AREA_EXPLAIN    NOT NULL VARCHAR2(1000) 
	private Integer areaCode;
	private String areaName;
	private String areaShortName;
	private String areaEngName;
	private String areaFileName;
	private String areaRandmark;
	private String areaX;
	private String areaY;
	private String areaExplain;
}
