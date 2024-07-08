package mclass.store.tripant.trip.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import mclass.store.tripant.place.domain.PlaceboxEntity;
import mclass.store.tripant.trip.domain.DayEntity;
import mclass.store.tripant.trip.model.service.TripService;

@Controller
@RequestMapping(value = "/trip")
public class TripController {
	@Autowired
	private TripService service;
	
	//map 위치간 이동시간
	@Value("${kakao.map.rest.api}")
	private String apikey;
	
	//여행 기본정보 불러오기
	@GetMapping(value="/detail/{planId}")
	public String detail(Model model,@PathVariable Integer planId ) {
		model.addAttribute("planInfo", service.planInfo(planId));
		model.addAttribute("detailList", service.detailList(planId));
		return "trip/trip";
	}
	
	//여행 세부일정 데이터 불러오기
	@PostMapping(value="/detail/{planId}/info")
	@ResponseBody
	public List<DayEntity> detail2(ModelAndView mv,@PathVariable Integer planId) {
		List<DayEntity> dayEntityList= service.detailList(planId);
		return dayEntityList;
	}

	
	//지점 간 이동시간 구하기 ajax
	@PostMapping("/duration")
	@ResponseBody //ajax 사용
	public String getduration(
			@RequestParam String startLngStr,  //시작 경도 
			@RequestParam String startLatStr,  //시작 위도
			@RequestParam String endLngStr,    //도착 경도
			@RequestParam String endLatStr     //도착 위도
	) {
		String duration = service.getduration(startLngStr,startLatStr,endLngStr,endLatStr);
		return duration;
	}
	
	//장소 추가 - list 출력
	@PostMapping("/spot")
	public String spot(Model model, @RequestParam Integer areaCode, @RequestParam Integer spotType,
			@RequestParam Integer clickSpotNum) throws IOException {
		model.addAttribute("spotList", null);
		// 20개씩 더 출력하기
		int maxNum = (clickSpotNum + 1) * 20;
		List<PlaceboxEntity> spotList = service.selectTypeList(areaCode, spotType, maxNum);
		model.addAttribute("spotList", spotList);
		return "plan/spot_tab_content";
	}
	
	//장소 추가 - 찾기
	@PostMapping("/spot/find")
	public String spotFindMore(Model model, @RequestParam("findArea") String findArea,
			@RequestParam("areaCode") Integer areaCode, @RequestParam("clickSpotFindNum") Integer clickSpotFindNum) {
		model.addAttribute("spotList", null);
		// 20개씩 더 출력하기
		int maxNum = (clickSpotFindNum + 1) * 20;
		List<PlaceboxEntity> spotList = service.selectSpotFindList(findArea, areaCode, maxNum);
		model.addAttribute("spotList", spotList);
		return "plan/spot_tab_content";
	}
	
	//일정 저장 
	//jjoggan TODO
	@PostMapping("/save/changes")
	@ResponseBody
	public int saveChanges(
			@RequestBody List<DayEntity> saveData
			) {
		
		int result = 0;
		return result;
	}
}
