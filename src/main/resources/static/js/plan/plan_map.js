// 지도 세팅
var mapContainer;
var map;

$(document).ready(function() {
	var areaPointX;
	var areaPointY;
	$.ajax({
		beforeSend : csrfHandler,
		error : ajaxErrorHandler,
		url: contextPath + "plan"
		, method: "post"
		, data: {
			areaCode: areacode
		}
		, success: function(result) {
			if (result != null) {
				areaPointX = result.areaX;
				areaPointY = result.areaY;
				setAreaMap(areaPointX, areaPointY); // 처음 지역 중심 좌표로 이동
			}
		}
	});
});

// 처음 지역 중심 좌표로 이동
function setAreaMap(x, y) {
	mapContainer = document.getElementById('map'), // 지도를 표시할 div 
		mapOption = {
			center: new kakao.maps.LatLng(y, x), // 지도의 중심좌표 (y값, x값)  
			level: 9 // 지도의 확대 레벨
		};

	map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
}

