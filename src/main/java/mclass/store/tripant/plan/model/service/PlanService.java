package mclass.store.tripant.plan.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mclass.store.tripant.place.domain.AreaEntity;
import mclass.store.tripant.place.domain.AreaNameEntity;
import mclass.store.tripant.place.domain.AreaPointEntity;
import mclass.store.tripant.place.domain.PlaceboxEntity;
import mclass.store.tripant.plan.model.repostiory.PlanRepository;

@Service
public class PlanService {
	@Autowired
	private PlanRepository planRepository;

	public int selectPlanCount() {
		return planRepository.selectPlanCount();
	}

	public int selectMemCount() {
		return planRepository.selectMemCount();
	}

	public List<AreaNameEntity> selectAreaNameList() {
		return planRepository.selectAreaNameList();
	}

	public List<AreaNameEntity> selectAreaFindList(String findArea) {
		return planRepository.selectAreaFindList(findArea);
	}

	public AreaEntity selectAreaInfo(int areaCode) {
		return planRepository.selectAreaInfo(areaCode);
	}

	public String selectAreaShortName(int areaCode) {
		return planRepository.selectAreaShortName(areaCode);
	}

	public List<PlaceboxEntity> selectTypeList(int areaCode, int placeType, int maxNum) {
		return planRepository.selectTypeList(areaCode, placeType, maxNum);
	}
	
	public List<PlaceboxEntity> selectSpotFindList(String findArea, int areaCode,  int maxNum) {
		return planRepository.selectSpotFindList(findArea, areaCode, maxNum);
	}
	
	public List<PlaceboxEntity> selectStayFindList(String findArea, int areaCode, int maxNum) {
		return planRepository.selectStayFindList(findArea, areaCode, maxNum);
	}
	
	public AreaPointEntity selectAreaPoint(int findArea) {
		return planRepository.selectAreaPoint(findArea);
	}
}
