var clickspotnum = 0;
var clickspotfindnum = 0;
var spottype;
var spotboxCount;
//검색명
var findArea;
var spotId = "";

//더보기
function spotMoreBtnClickHandler(thisElement) {
	// 클릭횟수 증가
	clickspotnum += 1;

	$.ajax({
		beforeSend : csrfHandler,
		error : ajaxErrorHandler,
		url: contextPath + "plan/spot"
		, method: "post"
		, context: this
		, data: {
			areaCode: areacode,
			spotType: spottype,
			clickSpotNum: clickspotnum
		}
	}).done(function(wrap_spot) {
		$(spotId).replaceWith(wrap_spot);

		//더보기 계속 붙일건지
		spotboxCount = $(spotId).find(".spot-box").length;
		if (spotboxCount < 20 * (clickspotnum + 1)) { //20개 보다 적게 나온경우
			$(".spot_more_btn").remove();
		}

		// 미리 클릭해 둔 리스트 다시 활성화
		listCheckSpot();
	});
}

//탭 눌렀을때 기본 리스트 뿌리기
$(document).ready(function() {
	$('.spot-tab-nav a').click(function() {
		//검색했던게 있으면 지우기
		if ($("#find-spot").val().trim().length != 0) {
			$("#find-spot").val("");
		}

		$('.spot-tab-content > div').hide().filter(this.hash).fadeIn();
		$('.spot-tab-nav a').css("color", "var(--color_gray)");
		$('.spot-tab-nav li').css("background-color", "white");
		$('.spot-tab-nav a').removeClass('active');
		$(this).addClass('active');
		$(this).css("color", "white");
		$(this).parent().css("background-color", "var(--color_day9_blue)");

		//더보기 클릭 횟수 초기화
		clickspotnum = 0;

		areacode = $(".plan-areacode").attr("value");
		var placeTypeS = $(this).text();

		if (placeTypeS == '관광지') {
			spottype = 1;
		} else if (placeTypeS == '문화시설') {
			spottype = 2;
		} else if (placeTypeS == '쇼핑') {
			spottype = 3;
		} else if (placeTypeS == '음식점') {
			spottype = 4;
		}
		spotId = "#spot-tab0" + spottype + " .wrap-spotList" //필요한 탭 content만 값 넣기

		$.ajax({
			beforeSend : csrfHandler,
			error : ajaxErrorHandler,
			url: contextPath + "plan/spot"
			, method: "post"
			, context: this
			, data: {
				areaCode: areacode,
				spotType: spottype,
				clickSpotNum: clickspotnum
			}
		}).done(function(wrap_spot) {
			$(spotId).replaceWith(wrap_spot);

			//결과값 null 체크
			spotboxCount = $(spotId).find(".spot-box").length;

			if (spotboxCount == 0) { //결과 없음
				$(".spot_more_btn").remove();
				var htmlVal = '<p style="text-align: center;">결과가 없습니다.</p>';
				$(".resultSpotCheck").html(htmlVal);
			} else if (spotboxCount < 20) { //20개 보다 적게 나온경우
				$(".spot_more_btn").remove();
			}

			// 미리 클릭해 둔 리스트 다시 활성화
			listCheckSpot();
		});

		return false;
	}).filter(':eq(0)').click();

	//검색
	$(".btn.find-spot").on("click", btnSpotFindClickHandler);
});

//검색
function btnSpotFindClickHandler() {
	//버튼 클릭 초기화
	clickspotfindnum = 0;
	//타입 전체 선택 해제
	$('.spot-tab-nav a').css("color", "var(--color_gray)");
	$('.spot-tab-nav li').css("background-color", "white");
	$('.spot-tab-nav a').removeClass('active');

	findArea = $("input[name=find-spot]").val().trim();
	if (findArea.length == 0) {
		alert("빈문자열만 입력할 수 없습니다. 검색할 장소명을 입력해주세요.");
		return;
	}

	$.ajax({
		beforeSend : csrfHandler,
		error : ajaxErrorHandler,
		url: contextPath+"plan/spot/find"
		, method: "post"
		, context: this
		, data: {
			findArea: findArea,
			areaCode: areacode,
			clickSpotFindNum: clickspotfindnum
		}
	}).done(function(wrap_spot) {
		$(spotId).replaceWith(wrap_spot);

		//결과값 null(검색 결과 더보기) 체크
		spotboxCount = $(spotId).find(".spot-box").length;
		var htmlVal;
		if (spotboxCount == 0) { //결과 없음
			htmlVal = '<p style="text-align: center;">결과가 없습니다.</p>';
		} else if (spotboxCount >= 20) { //더보기 필요
			htmlVal = `
				<button type="button" onclick="spotFindMoreBtnClickHandler(this);"
				class="spot_find_more_btn">더보기</button>`;
		}
		$(".resultSpotCheck").html(htmlVal);
		$(".spot_more_btn").remove(); //검색아닌 더보기 지우기

		// 미리 클릭해 둔 리스트 다시 활성화
		listCheckSpot();
	});
}

//검색 더보기
function spotFindMoreBtnClickHandler(thisElement) {
	clickspotfindnum += 1;

	$.ajax({
		beforeSend : csrfHandler,
		error : ajaxErrorHandler,
		url: contextPath + "plan/spot/find"
		, method: "post"
		, context: this
		, data: {
			areaCode: areacode,
			findArea: findArea,
			clickSpotFindNum: clickspotfindnum
		}
	}).done(function(wrap_spot) {
		$(spotId).replaceWith(wrap_spot);

		//결과값 null(검색 결과 더보기) 체크
		spotboxCount = $(spotId).find(".spot-box").length;
		if (spotboxCount >= 20 * (clickspotfindnum + 1)) {
			var htmlVal = `
				<button type="button" onclick="spotFindMoreBtnClickHandler(this);"
				class="spot_find_more_btn">더보기</button>`;
			$(".resultSpotCheck").html(htmlVal);
		} else { //20개 보다 적게 나온경우
			$(".spot_find_more_btn").remove();
		}

		$(".spot_more_btn").remove(); //검색아닌 더보기 지우기

		// 미리 클릭해 둔 리스트 다시 활성화
		listCheckSpot();
	});
}

// 미리 클릭해 둔 리스트 다시 활성화
function listCheckSpot() {
	$.each(calendarPlan.spotArr, function(idx, element) {
		var checkId = "#" + element.id;
		$(checkId).attr("checked", true);
	});
}
