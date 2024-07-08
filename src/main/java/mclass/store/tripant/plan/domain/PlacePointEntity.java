package mclass.store.tripant.plan.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlacePointEntity {
	private Integer index;
	private String contentid;
    private String mapx;         
    private String mapy;  
    private Integer weight;
}
