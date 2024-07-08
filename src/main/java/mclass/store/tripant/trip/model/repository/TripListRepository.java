package mclass.store.tripant.trip.model.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import mclass.store.tripant.trip.domain.TripListEntity;
import mclass.store.tripant.trip.domain.TripShareEntity;

@Mapper //mybatis를 사용하기 위한 어노테이션
public interface TripListRepository {
	//나의 일정
	//목록 
	List<TripListEntity>  selectTripList(String memEmail);
	
	//삭제
	Integer delete(Integer planId);
	
	//유저검색
	List<TripShareEntity> find(Map<String, Object> map);
	
	//일정 공유 중인 맴버
	List<TripShareEntity> share(Map<String, Object> map);
	
	//유저 추가
	Integer add(Map<String, Object> map);
	
	//유저 추가
	Integer remove(Map<String, Object> map);
}
