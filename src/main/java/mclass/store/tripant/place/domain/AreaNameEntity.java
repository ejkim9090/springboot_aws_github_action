package mclass.store.tripant.place.domain;

import lombok.Data;

@Data
public class AreaNameEntity {
	// 지역명만 가져오기
	private Integer areaCode;
	private String areaName;
	private String areaShortName;
	private String areaEngName;
	private String areaFileName;
}
