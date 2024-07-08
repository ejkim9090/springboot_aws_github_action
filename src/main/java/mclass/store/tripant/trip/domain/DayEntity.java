package mclass.store.tripant.trip.domain;

import java.util.List;

import lombok.Data;

@Data
public class DayEntity {
	//중복되는 값과 중복되지 않는 데이터 Entity 구분후 연결
	private String travelDate;
	private String scheduleStart;
	private String scheduleEnd;
	private List<DayDetailInfoEntity> dayDetailInfoEntity;
}
