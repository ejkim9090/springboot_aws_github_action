package mclass.store.tripant.plan.model.repostiory;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import mclass.store.tripant.place.domain.AreaEntity;
import mclass.store.tripant.place.domain.AreaNameEntity;
import mclass.store.tripant.place.domain.AreaPointEntity;
import mclass.store.tripant.place.domain.PlaceboxEntity;

@Mapper
public interface PlanRepository {
	int selectPlanCount(); //생성된 일정 수
	int selectMemCount();  //가입한 멤버 수
	List<AreaNameEntity> selectAreaNameList(); //지역명 리스트
	List<AreaNameEntity> selectAreaFindList(String findArea);  //지역명 검색
	AreaEntity selectAreaInfo(int areaCode); // 지역 정보 
	String selectAreaShortName(int areaCode); // 짧은 이름
	List<PlaceboxEntity> selectTypeList(int areaCode, int placeType, int maxNum); //더보기 클릭 시
	List<PlaceboxEntity> selectSpotFindList(String findArea, @Param("areaCode") int areaCode, @Param("maxNum") int  maxNum); //장소명 검색 더보기
	List<PlaceboxEntity> selectStayFindList(String findArea, @Param("areaCode") int  areaCode, @Param("maxNum") int  maxNum); //장소명 검색 더보기
	AreaPointEntity selectAreaPoint(int findArea);
	
}
