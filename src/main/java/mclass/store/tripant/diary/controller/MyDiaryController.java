package mclass.store.tripant.diary.controller;

import java.security.Principal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import mclass.store.tripant.diary.domain.DiaryBoardEntity;
import mclass.store.tripant.diary.domain.WritePlanTitleEntity;
import mclass.store.tripant.diary.model.service.DiaryService;

@Controller
@RequestMapping("/my")
public class MyDiaryController {

	@Autowired
	private DiaryService diaryService;

	@GetMapping("/diary") // 특정 사용자가 작성한 모든 글 조회
	public ModelAndView mydiary(ModelAndView mv, Principal principal) {
		mv.addObject("diaries", diaryService.selectMyDiaryList(principal.getName(), 4));
		mv.setViewName("diary/my/my_board");
		return mv;
	}

	@PostMapping("/diary/more") // 특정 사용자가 작성한 모든 글 조회 더보기
	public String mydiaryMore(Model model, Principal principal, Integer clickNum) {
		int maxNum = (clickNum + 1) * 4;
		model.addAttribute("diaries", diaryService.selectMyDiaryList(principal.getName(), maxNum));
		return "diary/my/board_more_fragment";
	}
	// 기본 글 버전
	@GetMapping("/post")
	public ModelAndView showDiaryForm(Principal pricipal) {
		ModelAndView mv = new ModelAndView();
		List<WritePlanTitleEntity> plans = diaryService.getAllPlans(pricipal.getName());
		mv.addObject("plans", plans);
		mv.addObject("diaryEntry", new WritePlanTitleEntity()); // 폼 데이터를 위한 빈 객체 추가
		mv.setViewName("diary/my/my_write");
		return mv;
	}

	// 글쓰기 처리
	@PostMapping("/post")
	@ResponseBody
	public ResponseEntity<?> createDiary(@RequestBody DiaryBoardEntity diaryForm, Principal pricipal) {

		diaryForm.setDiaryMemEmail(pricipal.getName());
		diaryForm.setDiaryViews(diaryForm.getDiaryViews() == null ? 0 : diaryForm.getDiaryViews()); // 기본값 설정

		// 공개 여부 설정
		diaryForm.setDiaryOpen(diaryForm.getDiaryOpen() != null ? "0" : "1"); // true면 공개 ("0"), false면 비공개 ("1")

		// DiaryPostEntity 저장 (diaryService를 통해 저장 후 diary 객체는 DB에 저장된 후 자동으로 생성된 ID가
		// 채워짐)
		diaryForm = diaryService.save(diaryForm);

		// 저장된 DiaryPostEntity를 ResponseEntity로 반환
		return ResponseEntity.ok().body(diaryForm);
	}
	// 폰트 구매 후 글 버전
	@GetMapping("/post/font")
	public ModelAndView showFontDiaryForm(Principal pricipal) {
		ModelAndView mv = new ModelAndView();
		List<WritePlanTitleEntity> plans = diaryService.getAllPlans(pricipal.getName());
		mv.addObject("plans", plans);
		mv.addObject("diaryEntry", new WritePlanTitleEntity()); // 폼 데이터를 위한 빈 객체 추가
		mv.setViewName("diary/my/my_write_font");
		return mv;
	}
	// 글쓰기 처리(폰트 구매버전)
		@PostMapping("/post/font")
		@ResponseBody
		public ResponseEntity<?> createFontDiary(@RequestBody DiaryBoardEntity diaryForm, Principal pricipal) {

			diaryForm.setDiaryMemEmail(pricipal.getName());
			diaryForm.setDiaryViews(diaryForm.getDiaryViews() == null ? 0 : diaryForm.getDiaryViews()); // 기본값 설정

			// 공개 여부 설정
			diaryForm.setDiaryOpen(diaryForm.getDiaryOpen() != null ? "0" : "1"); // true면 공개 ("0"), false면 비공개 ("1")

			// DiaryPostEntity 저장 (diaryService를 통해 저장 후 diary 객체는 DB에 저장된 후 자동으로 생성된 ID가
			// 채워짐)
			diaryForm = diaryService.save(diaryForm);

			// 저장된 DiaryPostEntity를 ResponseEntity로 반환
			return ResponseEntity.ok().body(diaryForm);
		}
	
	
	   // 여행글 삭제 처리
    @PostMapping("/diary/delete/{diaryId}")
    @ResponseBody
    public int deleteDiaryById(@PathVariable("diaryId") int diaryId, Principal pricipal) {
        // 여기서 diaryId를 사용하여 삭제 작업을 수행합니다.
        int result = 0;
      
		try {
			
			result = diaryService.deleteDiaryById(diaryId, pricipal.getName());
		} catch (Exception e) {
			
			e.printStackTrace();
			result = -1;
		} // DiaryService에서 삭제 메서드 호출
        return result; // 삭제 성공 시 1, 실패 시 0을 반환합니다.
    }
 // 여행글 신고 처리
    @PostMapping("/diary/report/{diaryId}")
    @ResponseBody
    public int reportsOne(@PathVariable("diaryId")int diaryId, Principal principal) {
    	int result = 0;
		try {
			result = diaryService.reportsOne(diaryId, principal.getName());
		} catch (SQLIntegrityConstraintViolationException e) {
			e.printStackTrace();
			result = -1;
		}
        return result;
    }
    
	// 좋아요 기능
	@PostMapping("/diary/like/{diaryId}")
	@ResponseBody
	public Integer likeDiary(@PathVariable int diaryId, Principal pricipal) {
		int result = diaryService.likeDiary(diaryId, pricipal.getName());
		return result;
	}
	// 좋아요해제 기능
	@PostMapping("/diary/unlike/{diaryId}")
	@ResponseBody
	public Integer unlikeDiary(@PathVariable int diaryId, Principal pricipal) {
		int result = diaryService.unlikeDiary(diaryId, pricipal.getName());
		return result;
	}

}
