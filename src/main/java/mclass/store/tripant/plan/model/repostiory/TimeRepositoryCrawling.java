package mclass.store.tripant.plan.model.repostiory;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import mclass.store.tripant.place.domain.PlaceMapEntity;
import mclass.store.tripant.place.domain.PlaceMoveTimeEntity;


@Mapper
public interface TimeRepositoryCrawling {
	public int deleteAllPlaceMoveTime();
	public List<PlaceMapEntity> selectPlaceMapList(int areacode);
	public int insertPlaceMoveTime(PlaceMoveTimeEntity moveTimeEntity);
}
