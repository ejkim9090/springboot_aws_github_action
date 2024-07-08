package mclass.store.tripant.plan.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.servlet.http.HttpSession;
import mclass.store.tripant.place.domain.AreaEntity;
import mclass.store.tripant.place.domain.AreaPointEntity;
import mclass.store.tripant.place.domain.PlaceboxEntity;
import mclass.store.tripant.plan.domain.CalendarPlanEntity;
import mclass.store.tripant.plan.model.service.PlanService;
import mclass.store.tripant.plan.model.service.PlanningAlgorithm;

@Controller
@RequestMapping(value = "plan")
//@Slf4j  //private Logger log = LoggerFactory.getLogger(PlanController.class); 내용을 대신해줌
public class PlanController {
	// private Logger log = LoggerFactory.getLogger(PlanController.class);

	@Autowired
	private PlanService planService;

	@Autowired
	private PlanningAlgorithm planningAlgorithm;

	@GetMapping("")
	public String make(@SessionAttribute(name = "areaCode") Integer areaCode,
			@SessionAttribute(name = "planTitle") String planTitle) {
		return "plan/make";
	}

	@PostMapping("")
	@ResponseBody
	public AreaPointEntity point(@RequestParam("areaCode") Integer areaCode) {
		AreaPointEntity point = planService.selectAreaPoint(areaCode);
		return point;
	}

	// 지역&제목 모달에서 선택한 지역 정보 가져오기
	@PostMapping("/area")
	@ResponseBody
	public AreaEntity area(@RequestParam("areaCode") Integer areaCode) {
		AreaEntity areaInfo = planService.selectAreaInfo(areaCode);
		return areaInfo;
	}

	@PostMapping("/keep")
	public String keep(@RequestParam("areaCode") Integer areaCode, @RequestParam("planTitle") String planTitle,
			HttpSession session) throws IOException {
		session.setAttribute("areaCode", areaCode);
		session.setAttribute("planTitle", planTitle);
		// 짧은 이름으로 넘기기
		String areaShortName = planService.selectAreaShortName(areaCode);
		session.setAttribute("areaShortName", areaShortName);
		return "redirect:/plan";
	}

	// -------------------------------------spot-------------------------------------------

	@PostMapping("/spot")
	public String spot(Model model, @RequestParam Integer areaCode, @RequestParam Integer spotType,
			@RequestParam Integer clickSpotNum) throws IOException {
		// 20개씩 더 출력하기
		int maxNum = (clickSpotNum + 1) * 20;
		List<PlaceboxEntity> spotList = planService.selectTypeList(areaCode, spotType, maxNum);
		model.addAttribute("spotList", spotList);
		return "plan/spot_tab_content";
	}

	@PostMapping("/spot/find")
	public String spotFindMore(Model model, @RequestParam("findArea") String findArea,
			@RequestParam("areaCode") Integer areaCode, @RequestParam("clickSpotFindNum") Integer clickSpotFindNum) {
		// 20개씩 더 출력하기
		int maxNum = (clickSpotFindNum + 1) * 20;
		List<PlaceboxEntity> spotList = planService.selectSpotFindList(findArea, areaCode, maxNum);
		model.addAttribute("spotList", spotList);
		return "plan/spot_tab_content";
	}

	// -------------------------------------stay-------------------------------------------

	@PostMapping("/stay")
	public String stayMore(Model model, @RequestParam("areaCode") Integer areaCode,
			@RequestParam("stayType") Integer stayType, @RequestParam("clickStayNum") Integer clickStayNum)
			throws IOException {
		// 20개씩 더 출력하기
		int maxNum = (clickStayNum + 1) * 20;
		List<PlaceboxEntity> stayList = planService.selectTypeList(areaCode, stayType, maxNum);
		model.addAttribute("stayList", stayList);
		return "plan/stay_tab_content";
	}

	@PostMapping("/stay/find")
	public String stayFindMore(Model model, @RequestParam("findArea") String findArea,
			@RequestParam("areaCode") Integer areaCode, @RequestParam("clickStayFindNum") Integer clickStayFindNum) {
		// 20개씩 더 출력하기
		int maxNum = (clickStayFindNum + 1) * 20;
		List<PlaceboxEntity> stayList = planService.selectStayFindList(findArea, areaCode, maxNum);
		model.addAttribute("stayList", stayList);
		return "plan/stay_tab_content";
	}

	// -------------------------------------planning-------------------------------------------
	@PostMapping("/planning")
	@ResponseBody
	public String planning(@RequestBody String jsonString, @SessionAttribute(name = "areaCode") Integer areaCode) {
		planningAlgorithm.planningJson(jsonString, areaCode);
		return "aaa";
	}

	// ---------------------------------numberFormatException-------------------------------------------
	@ExceptionHandler
	public String numberFormatExceptionHandler(NumberFormatException e) {
		e.printStackTrace();
		return "redirect:/";
	}

}
