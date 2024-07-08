package mclass.store.tripant.trip.model.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import mclass.store.tripant.place.domain.PlaceboxEntity;
import mclass.store.tripant.trip.domain.DayEntity;


@Mapper
public interface TripRepository {
	
	//여행 일정 불러오기
	List<DayEntity> detailList(Integer planId);
	
	//여행 기본(포괄)정보 불러오기
	Map<String,Object> planInfo(Integer planId); 
	
	//spot 정보 불러오기 
	List<PlaceboxEntity> selectTypeList(int areaCode, int placeType, int maxNum); //더보기 클릭 시
	
	//spot 검색 정보 불러오기
	List<PlaceboxEntity> selectSpotFindList(String findArea, @Param("areaCode") int areaCode, @Param("maxNum") int  maxNum); //장소명 검색 더보기
}
