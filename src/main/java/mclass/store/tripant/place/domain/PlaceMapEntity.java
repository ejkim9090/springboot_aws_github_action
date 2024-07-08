package mclass.store.tripant.place.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PlaceMapEntity {
	private Integer type;
	private Integer contentid;
	private String mapx; //경도
	private String mapy; //위도
}
