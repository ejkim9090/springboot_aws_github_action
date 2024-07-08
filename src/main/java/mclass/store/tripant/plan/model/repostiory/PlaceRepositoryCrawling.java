package mclass.store.tripant.plan.model.repostiory;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import mclass.store.tripant.place.domain.PlaceEntity;

@Mapper
public interface PlaceRepositoryCrawling {
	public int insertPlace(List<PlaceEntity> dtolist);
}
