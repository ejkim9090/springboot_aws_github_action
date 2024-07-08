// ***

// 지도에 표시된 마커 객체를 가지고 있을 배열입니다
var markersSpot = [];  //아이디 , 마커 담김 

// 장소 체크박스 클릭
function spotCkBtnClickHandler(thisElement) {
	var id = $(thisElement).attr("id");
	var title = $(thisElement).parent().find(".spot-name").attr("value");
	var latx = $(thisElement).parent().find(".spot-x").attr("value");
	var lngy = $(thisElement).parent().find(".spot-y").attr("value");
	var img = $(thisElement).parent().find(".spot-img").attr("src");
	var addr = $(thisElement).parent().find(".spot-address").attr("value");

	var moveLatLon = new kakao.maps.LatLng(lngy, latx); // 맵 위치로 변환
	// 확대 크기 변경
	map.setLevel(6);
	// 지도 이동 [setCenter()(바로 이동) -  panTo()(부드럽게 이동)]
	map.panTo(moveLatLon);

	//=======================================체크박스 해제=========================================
	if ($(thisElement).is(":checked") == false) {
		//마커 삭제
		setMarkersSpot(null);
		markersSpot.length = 0;
		//마커 다시 붙이기
		$(".wrap-spotList").find('input:checked').each(function(index) {
			var title = $(this).parent().find(".spot-name").attr("value");
			var mapx = $(this).parent().find(".spot-x").attr("value");
			var mapy = $(this).parent().find(".spot-y").attr("value");
			addMarkerSpot(new kakao.maps.LatLng(mapy, mapx), title, $(this).attr("id"), index); // 마커 추가
		});

		// 장소 정보 삭제
		$.each(spotArr, function(idx, element) {  
			if (element.id == id) {
				spotArr.splice(idx, 1);
				return false;
			}
		});
		// 박스 리스트 삭제
		$(".selected-spot-box." + id).remove();
		$(".count-spot").html(markersSpot.length);

		// 박스 번호 다시 붙이기
		$(".selected-spot-box").each(function(idx, thisElement) {
			$(thisElement).find(".selected-spot-number").children().text(idx + 1);
		});

		// 장소 설정 정보 부분 업데이트
		$(".count-spot").html(markersSpot.length);
		timeInfoUpdate();// 총 시간 업데이트

	} else { //=====================================체크박스 선택=========================================
		var spotTime = 7200;
		//var spotType = ; 장소다타입 넣을지 말지
		spotArr[markersSpot.length] = new Spot(id, title, latx, lngy,img,spotTime);  //전체 일정 만들기 장소 정보 저장
		addMarkerSpot(new kakao.maps.LatLng(lngy, latx), title, $(thisElement).attr("id"), markersSpot.length); // 마커 추가

		//화면 리스트 추가
		var htmlVal = "";
		htmlVal += `
			<div class="selected-spot-box ${id}">
				<span class="box-id" value="${id}" style ="display:none"></span> 
				<div class="wrap-box flex">
					<div class="selected-spot-number">
						<p>${markersSpot.length}</p>
					</div>
					<img class="selected-spot-img" src="${img}" alt="장소이미지" style="width: 60px; height: 60px;">
					<div class="selected-spot-txt">
						<span>${title}</span>
						<span>${addr}</span>
					</div>
					<div>
						<p class="time ${markersSpot.length}" onclick="timeRangeBtnClickHandler(this);">2시간 0분</p>
					</div>
				</div>
				<div class="timerange-modal flex" style="display:none;">
					<p style="margin-left:30px; font-weight: bold;">머무는 시간 설정</p>
					<input class="spot-hours" type='number' value="2" min="0" max="23"></input>
					<p>시간</p>
					<input class="spot-mins" type='number' value="0" min="0" max="55" step="5"></input>
					<p>분</p>
					<p class="timerange-done" onclick="timeDoneBtnClickHandler(this);" data-arr-length="${markersSpot.length}">완료</p>
				</div>
			</div>`;
		$(".selected-spot-list").append(htmlVal);

		// 장소 설정 정보 부분 업데이트
		$(".count-spot").html(markersSpot.length);
		//timeInfoUpdate();// 총 시간 업데이트
	}
}

// 장소 설정 정보 부분 업데이트
/*function timeInfoUpdate() {
	var calcHouers = 0;
	var calcMins = 0;
	var secSum = 0;
	$(".timerange-modal").each(function() {
		var hours =$(this).children('.spot-hours').val();
		var mins = $(this).children('.spot-mins').val();
		secSum += (hours * 60 * 60) + (mins * 60); //초로 변환하기

		// 선텍한(초 -> 시간, 분)
		calcHouers = Math.floor(secSum / 3600);
		calcMins = Math.floor((secSum - calcHouers * 3600) / 60);
	});
	
	
	if(calendarPlan.timeRange < secSum){
		alert("총 이용가능 시간보다 장소 이용시간이 많을 수 없습니다. 다시 설정해주세요");
		$(".time-spot").css("color", "red");
	} else{
		$(".time-spot").css("color", "black");
	}
	
	
	var rangeHouers = 0;
	var rangeMins = 0;
	// 사용 가능한(초 -> 시간, 분)
	var rangeHouers = Math.floor(calendarPlan.timeRange / 3600);
	var rangeMins = Math.floor((calendarPlan.timeRange - rangeHouers * 3600) / 60);

	var timeVal = calcHouers + "시간 " + calcMins + "분 /" + rangeHouers + "시간 " + rangeMins + "분";
	$(".time-spot").html(timeVal);
}*/

// 시간 설정 모달 열림
function timeRangeBtnClickHandler(thisElement) {
	$(thisElement).parents('.wrap-box').hide();
	$(thisElement).parents('.wrap-box').next().show();
}

// 시간 설정 완료
function timeDoneBtnClickHandler(thisElement) {
	$(thisElement).parents('.timerange-modal').prev().show();
	$(thisElement).parents('.timerange-modal').hide();
	var length = $(thisElement).data('arr-length');
	var hours = $(thisElement).parent().children('.spot-hours').val();
	var mins = $(thisElement).parent().children('.spot-mins').val();
	var timeVal = hours + "시간 " + mins + "분";
	spotArr[length-1].spotTime = hours * 3600 + mins * 60;
	$(thisElement).parents('.timerange-modal').prev().find('.time').text(timeVal);
	//timeInfoUpdate(); // 총 시간 업데이트
}

// *** 장소 설정 초기화
function spotResetBtnClickHandler() {
	$(".selected-spot-box").remove();
	spotArr.length = 0;
	//체크박스 해제
	$(".spotcheck").prop("checked", false);
	//마커 삭제
	setMarkersSpot(null);
	markersSpot.length = 0;
	// 장소 설정 정보 삭제
	$(".time-spot").html("");
	$(".count-spot").html(0);
}

//===================================================마커=====================================================
// 마커를 생성하고 지도위에 표시하는 함수입니다
function addMarkerSpot(position, title, id, index) {
	var imageSrc = "/images/location/location5.png";// 마커 이미지의 이미지 주소입니다
	var content = `       
		    <div class="custom-marker" th:fragment="markernum(i)">
		        <img src="${imageSrc}" style="width: 30px; height: 32px;">
		        <span>${index + 1}</span>
		    </div>`;
	// 마커를 생성합니다
	var marker = new kakao.maps.CustomOverlay({
		position: position,
		content: content,
		title: title,
		yAnchor: 1
	});
	// 마커가 지도 위에 표시되도록 설정합니다
	marker.setMap(map);
	// 생성된 마커를 배열에 추가합니다 
	markersSpot.push({ id: id, marker: marker });
}

// 배열에 추가된 마커들을 지도에 표시하거나 삭제하는 함수입니다
function setMarkersSpot(map) {
	for (var i = 0; i < markersSpot.length; i++) {
		markersSpot[i].marker.setMap(map);
	}
}
