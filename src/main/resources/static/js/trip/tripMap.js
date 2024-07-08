//마커 지도에 표시
function displayMarker(){
/*마커 추가 및 생성*/
var i
//, marker
;
var imageSrc = '' ;// 마커이미지의 주소입니다    


/*마커 커스터마이징*/ //j i 순서 주의
for(j = 0; j<dayPoints.length; j++){
		dayPoint = dayPoints[j];
		imageSrc=mapCircleHandler(j+1); // /images/location/location3.png 등 마커이미지 주소 변경
	for (i = 0; i < dayPoint.length; i++) {
		
	     // customOverlay 생성 - 마커위에 숫자 올리기 // 마커이미지의 크기 style로 지정
	    var content = `       
		    <div class="custom-marker" th:fragment="markernum(i)">
		        <img src="${contextPath}${imageSrc}" style="width: 30px; height: 32px;"> 
		        <span>${i + 1}</span>
		    </div>`;
	    var customOverlay = new kakao.maps.CustomOverlay({
	        position: dayPoint[i],
	        content: content,
	        yAnchor: 1
	    });
		    
	    //marker.setMap(map); //지도 위에 마커 표시
	    customOverlay.setMap(map); //지도 위에 마커 표시
	    
	 	/*지도에 표시할 선을 생성합니다*/ 
	    var polyline = new kakao.maps.Polyline({
	        path: dayPoint, // 선을 구성하는 좌표배열 입니다
	        strokeWeight: 2, // 선의 두께 입니다
	        strokeColor: mapLineHandler(j+1), // 선의 색깔입니다
	        strokeOpacity: 0.7, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
	        strokeStyle: 'shortdash' // 선의 스타일입니다
	    });
	 	// 지도에 선을 표시합니다 
	    polyline.setMap(map);  
	    polylines.push(polyline);
	    // LatLngBounds 객체에 좌표를 추가합니다
	    bounds.extend(dayPoint[i]);
	} 
}//마커 커스터마이징
	
}


// 폴리라인을 초기화하는 함수
function clearPolylines() {
    while (polylines.length > 0) {
        var polyline = polylines.pop();
        polyline.setMap(null); // 지도에서 폴리라인 제거
    }
}
// 지도 출력
function displayMap( ){
/* 1. 지도 생성*/
 mapContainer = document.getElementById('map'), // 지도를 표시할 div 
    mapOption = { 
	center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표(필수 입력) : kakao본사
        level: 9// 지도의 확대 레벨 큰 숫자 : 큰 범위
    };
//지도를 생성합니다
map = new kakao.maps.Map(mapContainer, mapOption); 

//지도를 재설정할 범위정보를 가지고 있을 LatLngBounds 객체를 생성합니다
bounds = new kakao.maps.LatLngBounds();   	

//마커 지도에 표시
displayMarker();
}


/* 지도 중심 이동하기*/
//이동할 좌표 입력 - TODO 수정 필요
function panTo() {
	//x좌표 위도 100번대
	var latx= $(this).parent().prev().prev().children(".mapx").val();
	//y좌표 경도 33번대
	var lngy= $(this).parent().prev().children(".mapy").val();
    // 이동할 위도 경도 위치를 생성합니다 
    var moveLatLon = new kakao.maps.LatLng(lngy,latx);
    
    // 지도 중심을 부드럽게 이동시킵니다
    // 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다
    map.panTo(moveLatLon);       
}
/* 3. 지도 범위 재설정 */
function setBounds() {
    // LatLngBounds 객체에 추가된 좌표들을 기준으로 지도의 범위를 재설정합니다
    // 이때 지도의 중심좌표와 레벨이 변경될 수 있습니다
    map.setBounds(bounds);
}
