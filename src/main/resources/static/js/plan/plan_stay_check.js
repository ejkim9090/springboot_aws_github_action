var stayBefor; // 저장 완료 전까지 담고있을 객체

//숙소 박스 초기화
function restStayBox() {
	var range = calendarPlan.dateArr.length - 1;
	// 장소 설정 초기화
	$(".count-stay").html(0);
	$(".time-stay").html("0일 / " + range + "일");

	$(".selected-stay-list").html(""); // 전체 삭제
	for (var i = 0; i < calendarPlan.dateArr.length - 1; i++) {
		var start = calendarPlan.dateArr[i].smalldate;
		var end = calendarPlan.dateArr[i + 1].smalldate;
		//화면 리스트 추가
		var htmlVal = "";
		htmlVal += `
			<div class="selected-stay-box">
				<div class="wrap-box flex">
					<div class="wrap-stay-first">
						<div class="selected-stay-number" style="background-color:var(--color_gray);">
							<p>${i + 1}</p>
						</div>
						<img class="box-stay-img" src="${contextPath}images/plan/stay_plus.png" alt="숙소이미지" style="width: 60px; height: 60px;">
						<div class="box-stay-txt">
							<span style="font-size:var(--font5); color:var(--color_gray);">${start} ~ ${end}</span>
							<span class="box-title" style="font-size:var(--font6); color:pink;">숙소를 추가해주세요</span>
							<span class="box-start" value="${start}" style ="display:none"></span> 
						</div>
						<span class="box-id" value="" style ="display:none"></span> 
					</div>	
					<div class="wrap-stay-second">
						<img class="delete btn" src="${contextPath}images/icons/trashcan.png" onclick="stayDeleteBtnClickHandler(this);">
					</div>
				</div>
			</div>`;
		$(".selected-stay-list").append(htmlVal);
	}
}

//숙소 탭 초기화
function restStaytab() {
	$(".modal-stay-list").html(""); // 전체 삭제
	for (var i = 0; i < calendarPlan.dateArr.length - 1; i++) {
		var start = calendarPlan.dateArr[i].smalldate;
		// 모달 날짜별 + 탭
		var modalVal = "";
		modalVal = `
		<div class="wrap-stay-tab">
			<p class="stay-tab-day">${start}</p>
			<img class="stay-tab-img" src="${contextPath}images/plan/stay_plus.png" onclick="stayTabBtnClickHandler(this);" alt="숙소이미지" style="width: 70px; height: 70px;">
			<p class="stay-tab-name">호텔 선택</p>
			<p class="stay-tab-id" value="" style ="display:none"></p> 
			<p class="stay-tab-x" value="" style ="display:none"></p> 
			<p class="stay-tab-y" value="" style ="display:none"></p> 
		</div>
		`;
		$(".modal-stay-list").append(modalVal);
	}
}

//===========================================실제 숙소 체크박스==========================================
//모달 완료
function stayModalDoneBtnClickHandler() {
	stayBefor = null; //비워놓기

	//모달 닫기
	$(".stay-modal").removeClass("show");
	//저장 전 전체 초기화 체크박스 해제
	$(".staycheck").prop("checked", false);
	//마커 일단 전부 삭제
	setMarkersStay(null);
	markersStay.length = 0;

	//화면에 있던 정보 저장(stay-tab 개수 만큼 반복문)
	$(".wrap-stay-tab").each(function() {
		var tabDay = $(this).children(".stay-tab-day").text();
		var tabImg = $(this).children(".stay-tab-img").attr("src");
		var tabName = $(this).children(".stay-tab-name").text();
		var tabId = $(this).children(".stay-tab-id").attr("value");
		var tabX = $(this).children(".stay-tab-x").attr("value");
		var tabY = $(this).children(".stay-tab-y").attr("value");

		if (tabX != "") { //숙소가 등록된 상황
			addMarkerStay(new kakao.maps.LatLng(tabY, tabX), tabName, tabId, markersStay.length); // 마커 추가
			$.each(calendarPlan.dateArr, function(idx, element) {
				// 숙소 정보 저장
				if (element.smalldate == tabDay) {
					element.stay.id = tabId;
					element.stay.title = tabName;
					element.stay.mapx = tabX;
					element.stay.mapy = tabY;
					element.stay.img = tabImg;
					return true;
				}
			});
		}
	});

	displayStayBoxList(); 	// 저장되어있는 숙소 박스 리스트에 넣기
	displayStayTabList(); 	// 저장되어있는 숙소 탭 리스트에 넣기
	displayStayCheckList(); // 저장되어있는 숙소 체크박스 다시 활성화

	// 장소 설정 정보 부분 업데이트
	$(".count-stay").text(markersStay.length);
	var dayTxt = markersStay.length + "일 / " + (calendarPlan.dateArr.length - 1) + "일";
	$(".time-stay").text(dayTxt);
}

// 저장되어있는 숙소 체크박스 다시 활성화
function displayStayCheckList() {
	$.each(calendarPlan.dateArr, function(idx, element) {
		// 숙소 정보 박스 넣기
		$(".stay-box").each(function() {
			var listId = $(this).children("input:checkbox").attr("id");
			if (element.stay?.id == listId) {
				$(this).find('input:checkbox').prop("checked", true);
			}
		});
	});
}

// 저장되어있는 숙소 탭 리스트에 넣기
function displayStayTabList() {
	$.each(calendarPlan.dateArr, function(idx, element) {
		// 숙소 정보 박스 넣기
		$(".wrap-stay-tab").each(function() {
			var tabDay = $(this).children(".stay-tab-day").text();
			if (element.smalldate == tabDay) {
				$(this).children(".stay-tab-img").attr("src", element.stay.img);
				$(this).children(".stay-tab-name").text(element.stay.title);
				$(this).children(".stay-tab-id").attr("value", element.stay.id);
				$(this).children(".stay-tab-x").attr("value", element.stay.mapx);
				$(this).children(".stay-tab-y").attr("value", element.stay.mapy);
			}
		});
	});
}

// 저장되어있는 숙소 박스 리스트에 넣기
function displayStayBoxList() {
	$.each(calendarPlan.dateArr, function(idx, element) {
		// 숙소 정보 박스 넣기
		$(".wrap-box").each(function() {
			var boxDay = $(this).find(".box-start").attr("value");
			if (element.smalldate == boxDay) {
				$(this).find(".box-id").attr("value", element.stay.id);
				$(this).find(".box-stay-img").attr("src", element.stay.img);
				$(this).find(".box-title").text(element.stay.title);
				$(this).find(".box-stay-txt").children().css("color", "black"); //TODO 해당되는곳만 스타일 변경되게
				$(this).find(".selected-stay-number").css("background-color", "var(--color_day3_pink)");
			}
		});
	});
}

// 숙소 + 탭 클릭
function stayTabBtnClickHandler(thisElement) {
	addMarkerStay(new kakao.maps.LatLng(stayBefor.mapy, stayBefor.mapx), stayBefor.title, stayBefor.id, markersStay.length); // 마커 추가

	//화면 변경
	$(thisElement).attr("src", stayBefor.img);
	$(thisElement).next().text(stayBefor.title);
	$(thisElement).next().next().attr("value", stayBefor.id);
	$(thisElement).next().next().next().attr("value", stayBefor.mapx);
	$(thisElement).next().next().next().next().attr("value", stayBefor.mapy);
}

//모달 전체 선택
function stayModalAllBtnClickHandler(thisElement) {
	addMarkerStay(new kakao.maps.LatLng(stayBefor.mapy, stayBefor.mapx), stayBefor.title, stayBefor.id, markersStay.length); // 마커 추가

	// 숙소 탭 전체 반복문
	$(".wrap-stay-tab").each(function() {
		$(this).children(".stay-tab-img").attr("src", stayBefor.img);
		$(this).children(".stay-tab-name").text(stayBefor.title);
		$(this).children(".stay-tab-id").attr("value", stayBefor.id);
		$(this).children(".stay-tab-x").attr("value", stayBefor.mapx);
		$(this).children(".stay-tab-y").attr("value", stayBefor.mapy);
	});
}

// 숙소 삭제 버튼
function stayDeleteBtnClickHandler(thisElement) {
	var boxStart = $(thisElement).parent().prev().find(".box-start").attr("value");

	// 장소 정보 삭제
	$.each(calendarPlan.dateArr, function(idx, element) {
		console.log(element.smalldate);
		if (element.smalldate == boxStart) {
			element.stay = new Stay();
			return false;
		}
	});

	setMarkersStay(null);
	markersStay.length = 0;
	// 장소 정보 삭제
	$.each(calendarPlan.dateArr, function(idx, element) {
		if (element.stay.id != null) {
			addMarkerStay(new kakao.maps.LatLng(element.stay.mapy, element.stay.mapx), element.stay.title, element.stay.id, markersStay.length); // 마커 추가
		}
	});

	//숙소 화면 초기화
	restStayBox();
	restStaytab();
	$(".staycheck").prop("checked", false);

	displayStayBoxList();    // 저장되어있는 숙소 박스 리스트에 넣기 
	displayStayTabList();    // 저장되어있는 숙소 탭 리스트에 넣기  
	displayStayCheckList();  // 저장되어있는 숙소 체크박스 다시 활성화
	
	var ccc = 0;
	
	$(".wrap-box").each(function() {
		var title = $(this).find(".box-title").text();
		if(title != "숙소를 추가해주세요"){
			ccc +=1;
		}
	});

	// 장소 설정 정보 부분 업데이트
	$(".count-stay").text(ccc);
	var dayTxt = ccc + "일 / " + (calendarPlan.dateArr.length - 1) + "일";
	$(".time-stay").text(dayTxt);
}

// 지도에 표시된 마커 객체를 가지고 있을 배열입니다
var markersStay = [];  //아이디 , 마커 담김 
// 숙소 체크박스 클릭
function stayCkBtnClickHandler(thisElement) {
	var id = $(thisElement).attr("id");
	var title = $(thisElement).parent().find(".stay-name").attr("value");
	var latx = $(thisElement).parent().find(".stay-x").attr("value");
	var lngy = $(thisElement).parent().find(".stay-y").attr("value");
	var img = $(thisElement).parent().find(".stay-img").attr("src");

	var moveLatLon = new kakao.maps.LatLng(lngy, latx); // 맵 위치로 변환
	// 확대 크기 변경
	map.setLevel(6);
	// 지도 이동 [setCenter()(바로 이동) -  panTo()(부드럽게 이동)]
	map.panTo(moveLatLon);

	if ($(thisElement).is(":checked") == false) { // 체크박스 해제
		//불가
	} else { // 체크박스 선택
		stayBefor = new Stay(id, title, latx, lngy, img);

		//모달 숙소 이름 변경
		$(".modal-stay-name").html(title);

		//숙소 화면 초기화
		restStayBox();
		restStaytab();
		$(".staycheck").prop("checked", false); //체크박스 해제

		displayStayBoxList();   // 저장되어있는 숙소 박스 리스트에 넣기 
		displayStayTabList();   // 저장되어있는 숙소 탭 리스트에 넣기  
		displayStayCheckList(); // 저장되어있는 숙소 체크박스 다시 활성화

		$(".stay-modal").addClass("show");
	}
}

// 숙소 설정 초기화 버튼
function stayResetBtnClickHandler() {
	$(".selected-stay-box").remove();
	//체크박스 해제
	$(".staycheck").prop("checked", false);
	//마커 삭제
	setMarkersStay(null);
	markersStay.length = 0;

	//숙소 화면 초기화
	restStayBox();
	restStaytab();
	stayResetStayList(); // 저장되어있는 숙소 초기화
}

// 저장되어있는 숙소 초기화
function stayResetStayList() {
	$.each(calendarPlan.dateArr, function(idx, element) {
		element.stay = new Stay();
	});
}

//===================================================마커=====================================================
// 마커를 생성하고 지도위에 표시하는 함수입니다
function addMarkerStay(position, title, id, index) {
	var imageSrc = "/images/location/location3.png";// 마커 이미지의 이미지 주소입니다
	// 마커 이미지의 이미지 크기 입니다
	var imageSize = new kakao.maps.Size(24, 26);
	// 마커 이미지를 생성합니다    
	var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
	// 마커를 생성합니다
	var marker = new kakao.maps.Marker({
		map: map, // 마커를 표시할 지도
		position: position, // 마커를 표시할 위치
		title: title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
		image: markerImage // 마커 이미지 
	});
	// 마커가 지도 위에 표시되도록 설정합니다
	marker.setMap(map);
	// 생성된 마커를 배열에 추가합니다 
	markersStay.push({ id: id, marker: marker });
}

// 배열에 추가된 마커들을 지도에 표시하거나 삭제하는 함수입니다
function setMarkersStay(map) {
	for (var i = 0; i < markersStay.length; i++) {
		markersStay[i].marker.setMap(map);
	}
}

// 외부영역 클릭 시 모달 닫기
$(document).mouseup(function(e) {
	var LayerModal = $(".stay-modal");
	if (LayerModal.has(e.target).length === 0) {
		LayerModal.removeClass("show");
	}
});