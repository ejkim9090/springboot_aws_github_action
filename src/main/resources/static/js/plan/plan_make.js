// 일정만들기에서 전체적으로 사용할 클래스
class PlanDate {
	constructor(date, smalldate, day) {
		this.date = date; //2024.06.06
		this.smalldate = smalldate; //06/06
		this.day = day; //목요일
	}
	startTime; //10:00
	endTime;   //22:00
	stay = new Stay();  //숙소
}

class Spot {
	constructor(id, title, mapx, mapy) {
		this.id = id;
		this.title = title;
		this.mapx = mapx;
		this.mapy = mapy;
	}
	spotTime;
}

class Stay {
	constructor(id, title, mapx, mapy, img) {
		this.id = id;
		this.title = title;
		this.mapx = mapx;
		this.mapy = mapy;
		this.img = img;
	}
}

class CalendarPlan {
	dateArr;
	spotArr;
	timeRange = 0;
}

let calendarPlan = new CalendarPlan();
calendarPlan.dateArr = new Array(PlanDate);
calendarPlan.spotArr = new Array(Spot);

// 저장되기 전에만 방지 처리
var beforeSave = true;

//창닫기, 새로고침 시 확인 이벤트
$(window).bind("beforeunload", function(e) {
	if (beforeSave) {
		return "창을 닫으실래요?";
	}
});

//지역 코드
var areacode;

$(loadedHandler);
function loadedHandler() {
	//메인 화면 돌아가기
	$(".logo").on("click", function() {
		location.href = contextPath;
	});

	//달력 다시 열기
	$(".plan-priod").on("click", function() {
		beforeSave = false;
		if (confirm("기간을 다시 설정하면 작성한 내용이 없어집니다. 괜찮습니까?") == true) {
			location.reload(true);
			$(".modal").addClass("show");
		} else {
			event.preventDefault();
		}
	});

	var cls_name;
	//좌측 탭
	$('.tab-nav a').click(function() {
		var clickElement = this;
		function moveStep() {
			$('.tab-nav a').removeClass('active');
			$(clickElement).addClass('active');
			$('.tab-content > div').hide().filter(clickElement.hash).fadeIn();
		}
		// click		
		var click_cls_name = $(clickElement).attr("class");
		// current
		var currentActive = $('.tab-nav .active').attr("class");
		if (click_cls_name == 'nav-1' && !currentActive) {
			moveStep();
			//saveTimeInfo(); //시간 정보 저장
			$(".main-wrapper .tab-content").css("width", "25%");

		} else {

			cls_name = currentActive.replace(' active', '');
			if (cls_name == 'nav-1') {
				if (click_cls_name == 'nav-3') {
					if (markersSpot.length < calendarPlan.dateArr.length) {
						alert("하루에 한 개 이상의 장소에 방문해야해요. 장소를 더 추가해주세요!");
					} else {
						moveStep();
						$(".main-wrapper .tab-content").css("width", "40%");
					}
				} else {
					moveStep();
					saveTimeInfo(); //시간 정보 저장
					$(".main-wrapper .tab-content").css("width", "40%");
				}
			} else if (cls_name == 'nav-2') {
				if (click_cls_name == 'nav-3') {
					if (markersSpot.length < calendarPlan.dateArr.length) {
						alert("하루에 한 개 이상의 장소에 방문해야해요. 장소를 더 추가해주세요!");
					} else {
						moveStep();
						$(".main-wrapper .tab-content").css("width", "40%");
					}
				} else {
					moveStep();
					$(".main-wrapper .tab-content").css("width", "25%");
				}
			} else if (cls_name == 'nav-3') {
				if (click_cls_name == 'nav-2') {
					moveStep();
					$(".main-wrapper .tab-content").css("width", "40%");
				} else {
					moveStep();
					$(".main-wrapper .tab-content").css("width", "25%");
				}
			}

		}
		return false;
	}).filter(':eq(0)').click();

	//다음 버튼
	$(".next.btn").on("click", function() {
		$(".tab-nav > li").each(function() {
			cls_name = $(this).find('a').attr("class");
			if (cls_name === 'nav-1 active') {
				//2번으로 이동
				$('.tab-nav a').removeClass('active');
				$('.nav-2').addClass('active');

				$('.tab-content > #tab01').hide();
				$('.tab-content > #tab02').show();
				saveTimeInfo();
				$(".main-wrapper .tab-content").css("width", "40%");
				return false;
			} else if (cls_name === 'nav-2 active') {
				// 장소가 최소 날짜 수 만큼
				if (markersSpot.length < calendarPlan.dateArr.length) {
					alert("하루에 한 개 이상의 장소에 방문해야해요. 장소를 더 추가해주세요!");
				} else {
					//3번으로 이동
					$('.tab-nav a').removeClass('active');
					$('.nav-3').addClass('active');

					$('.tab-content > #tab02').hide();
					$('.tab-content > #tab03').show();
					$(".main-wrapper .tab-content").css("width", "40%");
				}
				return false;
			} else if (cls_name === 'nav-3 active') {
				if (markersStay.length < calendarPlan.dateArr.length - 1) {
					alert("하루에 한 개 이상의 숙소에 방문해야해요. 숙소를 더 추가해주세요!");
				} else {
					// 일정 만들기 알고리즘 돌리기
					const jsonString = JSON.stringify(calendarPlan);
					$.ajax({
						beforeSend : csrfHandler,
						error : ajaxErrorHandler,
						url: contextPath + "plan/planning",
						method: "post",
						contentType: "application/json",
						data: jsonString,
						traditional: true, //필수
						//dataType: "json",
						success: function(data) {
							console.log(data);
						}
					});
				}
			}
		});
	});
}