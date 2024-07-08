$(document).ready(function() {
	$('#daterange').daterangepicker({
		opens: "center",
		beforeSend: function(xhr) {
			xhr.setRequestHeader(header, token);
		},
		alwaysOpen: true,
		"locale": {
			"format": "YYYY.MM.DD",
			"separator": " ~ ",
			"applyLabel": "확인",
			"cancelLabel": "일정 만들기 취소",
			"fromLabel": "From",
			"toLabel": "To",
			"customRangeLabel": "Custom",
			"weekLabel": "W",
			"daysOfWeek": ["일", "월", "화", "수", "목", "금", "토"],
			"monthNames": ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
		},
		"minDate": new Date(),
		"startDate": new Date(),
		"endDate": new Date(),
		"drops": "auto"
	}, function(start, end, label) {
		//console.log('New date range selected: ' + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD') + ' (predefined range: ' + label + ')');
	});

	$('#daterange').focus();
});

// 달력 취소 시 메인으로 돌아감
$('#daterange').on('cancel.daterangepicker', function(ev, picker) {
	beforeSave = false;  //무조건 메인으로 돌아감
	location.href = "/";
});

// 달력 기간 입력
$('#daterange').on('apply.daterangepicker', function(ev, picker) {
	let diff = Math.abs(picker.endDate - picker.startDate);
	diff = Math.ceil(diff / (1000 * 60 * 60 * 24));
	console.log(diff);
	
	if (diff == 1) {
		alert("기간이 너무 작습니다. 기간을 다시 입력해주세요. \n (최소 2일 최대 10일)");
		$('#daterange').focus();
	} else if (diff > 10) {
		alert("기간이 너무 큽니다. 기간을 다시 입력해주세요. \n (최소 2일 최대 10일)");
		$('#daterange').focus();
	} else {
		$(".time_btn").show();
		
		var start = picker.startDate.format('YYYY.MM.DD');
		var end = picker.endDate.format('YYYY.MM.DD');
		//달력 모달 닫기
		$('.modal').removeClass('show');

		// 화면에 기간 정보 입력
		const period = start + " ~ " + end;
		$('.plan-priod').html(period);

		var htmlVal = `입력하신 여행 기간이 여행지 도착날짜와 여행지 출발 날짜가 맞는지 확인해주세요. 
		기본 설정 시간은 오전 10시~오후 10시 총 12시간 입니다.
		<br>
		<p style="font-weight: bold; margin-top:5px;">
		기본 세팅 시간은 오전10시 부터 오후 10시입니다. 종료 시각이 시작 시각 보다 크지 않을경우 글자가 빨간색으로 변합니다. 올바르게 입력하여 시간 설정을 완료해주세요.
		</p>
		`;
		$('.plan-timeEx').html(htmlVal);

		// 일정 시작, 끝 시간 테이블 만들기
		for (let i = 0; i < diff; i++) {
			let dateStr = new Date(start);
			//하루하루 정보
			let date = new Date(dateStr.setDate(dateStr.getDate() + i));

			//월/일 형태로 변경
			let yyyy = date.getFullYear();
			let MM = ('0' + (date.getMonth() + 1)).slice(-2);
			let dd = ('0' + date.getDate()).slice(-2);
			let smalldate = MM + '/' + dd;
			
			let dateY = yyyy + MM + dd;

			//요일 가져오기
			const WEEKDAY = ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'];
			let day = WEEKDAY[date.getDay()];

			calendarPlan.dateArr[i] = new PlanDate(dateY, smalldate, day);
		}
		//시간 테이블 생성
		displayDayTable();
		//숙소 화면 초기화
		restStayBox();
		restStaytab();
	}
});

// 시간 입력 테이블 
function displayDayTable() {
	var htmlVal = `
		<li>
			<div class="time-h"><h5>일자</h5></div>
			<div class="time-h"><h5>요일</h5></div>
			<div class="time-h"><h5>시작 시각</h5></div>
			<div class="time-h"><h5>종료 시각</h5></div>
		</li>
	`;
	for (var idx in calendarPlan.dateArr) {
		var date = calendarPlan.dateArr[idx].smalldate;
		var day = calendarPlan.dateArr[idx].day;
		htmlVal += `
		<li>
			<div><h5>${date}</h5></div>
			<div><h5>${day}</h5></div>
			<div><input type="time" class="timeRange" id="start-${idx}" value="10:00"></div>
			<div><input type="time" class="timeRange" id="end-${idx}" value="22:00"></div>
		</li>
		`;
	}
	$('.wrap-time ul').html(htmlVal);

	// 시간 입력 체크
	$('#timeForm input').on('input', timeInputCheck);

	// 시간 입력 완료 -> 화면 이동
	$(".time_btn").on("click", function() {
		// 시간 정보 저장
		saveTimeInfo();
		// 화면 이동
		$('.tab-content > div').hide().filter(this.hash).fadeIn();
//		$('.tab-nav a').css('color', 'black');
		$('.tab-nav a').removeClass('active');
		$('.nav-2').addClass('active');

		$('.tab-content > #tab02').show();
		$(".main-wrapper .tab-content").css("width", "40%");
	});
}
// 시간 정보 변수에 저장
function saveTimeInfo() {
	var rangeSecSum = 0;
	//각각 시간 값 저장
	for (var idx in calendarPlan.dateArr) {
		var startstr = $('#start-' + idx).val(); // 시작
		var startArr = startstr.split(':');
		var startH = parseInt(startArr[0]); // 시작 시각
		var startM = parseInt(startArr[1]); // 시작 분
		var startSumSec = (startH * 60 * 60) + (startM * 60); //초로 변환하기
		//console.log(startSumSec);

		var endstr = $('#end-' + idx).val(); // 종료
		var endArr = endstr.split(':');
		var endH = parseInt(endArr[0]); // 종료 시각
		var endM = parseInt(endArr[1]); // 종료 분
		var endSumSec = (endH * 60 * 60) + (endM * 60); //초로 변환하기
		//console.log(endSumSec);

		rangeSecSum += (endSumSec - startSumSec);  // 하루치 활동 시간 초

		// 시간 정보 저장
		calendarPlan.dateArr[idx].startTime = startstr;
		calendarPlan.dateArr[idx].endTime = endstr;
	}
	//전체 시간 범위 넣어주기
	calendarPlan.timeRange = rangeSecSum;
	console.log(calendarPlan);

	var rangeHouers = 0;
	var rangeMins = 0;
	//초 -> 시간, 분
	var rangeHouers = Math.floor(calendarPlan.timeRange / 3600);
	var rangeMins = Math.floor((calendarPlan.timeRange - rangeHouers * 3600) / 60);

	var timeVal = "0시간 0분 /" + rangeHouers + "시간 " + rangeMins + "분";
	$(".time-spot").html(timeVal);
}

// 시간 입력 체크
function timeInputCheck() {
	console.log(calendarPlan);
	//입력 범위 체크
	let id = $(this).attr('id');
	let id_num;
	if (id.includes("end-"))
		id_num = id.replace('end-', '');
	if (id.includes("start-"))
		id_num = id.replace('start-', '');

	var start = $('#start-' + id_num).val();
	var end = $('#end-' + id_num).val();
	
	if (start >= end) { // 시작 시간이 더 큼
		$(this).css('color', 'red');
	}
	else {
		$('.timeRange').css('color', 'black');
	}
}