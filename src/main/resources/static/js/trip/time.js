

//ajax이용해서 장소간 이동시간 api값 가져오기
function durationHandler(startLngStr,startLatStr,endLngStr,endLatStr){
	var returndata;
	var dataset = {
		startLngStr : startLngStr,
		startLatStr : startLatStr,
		endLngStr : endLngStr,
		endLatStr : endLatStr
	};
	$.ajax({
		beforeSend : csrfHandler,
		error : ajaxErrorHandler,
		url: contextPath+'trip/duration',
		data:dataset,
	 	async: false,  
		method:"post",
		success : function(result){
		returndata= result; //결과값을 변수에 담아서 ajax 밖에서 return 해준다
		}
	});
	return returndata;
}
 
function unescapeHtml(str) {
 if (str == null) {
  return "";
 }
 return str
   .replace(/&amp;/g, '&')

   .replace(/&lt;/g, '<')

   .replace(/&gt;/g, '>')

   .replace(/&quot;/g, '"')

   .replace(/&#039;/g, "'")

   .replace(/&#39;/g, "'");

}
/* 시간 관련 js 설정*/
//
let arrLeng=[];
function secToHoursAndMin(stayTime){
	let hours = Math.floor(stayTime/3600);
	let minutes = stayTime % 3600;
	return [hours,minutes];
}

// 시, 분으로 값 분리
function dividTime(timeStr){
	 //time.split(':') >> ["11", "30"]로 분리
	 //.map(Number) >> [11, 30]로 분리하여 각 변수 hours, minutes에 할당
	let[hours,minutes] = timeStr.split(':').map(Number);
	
	//배열로 반환
	return [hours,minutes];
}

//시간 더하기
function addTime(timeStr, stayTime){
	//앞 장소의 떠나는 시각(또는 하루 일정 시작 시각) 11:00 을 :을 기준으로 시와 분으로 담기
	let[hours,minutes] =dividTime(timeStr);
	
	// 분단위로 바꿔서 도착시간과 머무는 시간 합하기
	//도착시각 + 머무는 시간 또는 이전 장소에서 출발 시각 + 이동시간
	let totalMinutes = hours*60 +minutes*1 +Math.ceil(stayTime/60);
	
	// spot 도착시각 또는 spot 떠나는 시각 구하기
	let finalHours = Math.floor(totalMinutes/60);
	let finalMinutes = totalMinutes%60;
	
	return `${String(finalHours).padStart(2, '0')}:${String(finalMinutes).padStart(2, '0')}`;
}
//페이지 들어가자마자 있는 일정 -편집 전 화면
function displayInfo(){
	//DB에서 받아온 original 정보
	detailList=dayEntityList_org;
	
	//html에 뿌릴 정보 백틱에 담기
	var navHtmlval =""; 		
	var htmlval = "";
	
	//도착, 출발 시각 변수 - 배열에 담음

	//이동시간 변수  - 해당 function 안에서만 사용되고 durationMin은 배열에 담음
	var duration ="";
	var prevDuration ="";

	//세션스토리지 및 세선배열 초기화 - 사용 X js의 얕은 복사를 활용하여 굳이 sessionStorage를 활용할 필요가 없음
	
	var detailListLength = detailList.length;
	dayPoints = new Array(detailListLength);  //(전체일정) 일정 날마다 장소들 지도에 표시될 위치 (points 담을 배열)// 초기화
	
	for(var i=0; i<detailListLength; i++ ){
		//DayEntity를 list에 담기
		details =  detailList[i];
		
		// <<<<<<<<<<<<<<< 백틱
		//좌측 탭 태그 넣기
		navHtmlval +=`
			<div class="dayn"><a href="#tab${i+1}">${i+1}일차</a></div>
			`;
		//세부일정 목록 넣기	
		htmlval += `
		<div class="column flex" data-columns="${i+1}" id="tab${i+1}">
			<div class="sub-title flex ">
				<h4 class="nday">${i+1}일차</h4>
				<h6  class="date">${details.travelDate}</h6>
			</div>
			<div class="container flex wfull">
			`;
		// >>>>>>>>>>>>>>>>>> 백틱

		//장소별 정보 넣기
		var daylength = details.dayDetailInfoEntity.length;
		var points = new Array(daylength); // 하루동안 장소들 지도에 표시될 위치 // 초기화
		//DayDetailInfoEntity 값 list에 넣기
		for(var j=0; j < daylength; j++ ){

			info =  details.dayDetailInfoEntity[j];

			//map에서 lng lat 값 넣기 KakaoMap Api
			var  point = new kakao.maps.LatLng(info.lat*1, info.lng*1);
			points[j] = point;
			
			//다음 장소로 이동시간(sec), 분단위로 변환하여 변수에 담기 
			if( (j+1) < daylength){  // 마지막 장소
				duration = durationHandler(info.lng, info.lat, details.dayDetailInfoEntity[j+1].lng,details.dayDetailInfoEntity[j+1].lat);
				// *** info에 durationMin 속성 추가
				info.durationMin=Math.ceil(duration/60);
			}	
			
			//머무는 시간 계산하기 ex) 10:00 - 11:00
			// *** info에 startTime , endTime 속성 추가
			//1번째 장소
			if(j == 0){
				info.startTime = details.scheduleStart;
				info.endTime = addTime(details.scheduleStart,info.stayTime);
			//2~n-1번째 장소	
			}else if(0 < j && j < daylength-1){
				info.startTime =  addTime(details.dayDetailInfoEntity[j-1].endTime, prevDuration);
				info.endTime = addTime(info.startTime,info.stayTime);
			//n번째 장소(숙소)	
			}else{
				info.startTime =  addTime(details.dayDetailInfoEntity[j-1].endTime, prevDuration);
				info.endTime = 	details.scheduleEnd;
			}
			
			//j번째 장소에서 다음 장소(j+1)로 이동하는데 걸리는 시간 변수에 담기 
			//prevDuration은 j+1의 도착시각을 계산할 때 사용됨 ex) 11:30-12:00에서 11:30 부분
			prevDuration = duration;
			
			// <<<<<<<<<<<<<<< 백틱
			//백틱에 값 넣기
			//장소정보	
			htmlval += `
				<div class="spot-block">
			 	<div class=" spot grid wfull" data-spot-order="${j+1}"  data-stay-time="${info.stayTime}">
			 		<div class="spot-number backimg"><p>${j+1}</p></div>
			 		<div class="spot-staytime">${info.startTime} - ${info.endTime}</div>
			 		<div class="spot-type">명소</div>
			 		<div class="spot-title wfull">${info.title}</div>
			 		<div class="spot-memo"><img class="img-memo" style="width: 20px;height:20px;" src="/images/icons/memoIcon.png" ><span class="memo">${info.memo}</span></div>
				`;
			 
			 //이미지 링크 유무에 따른 src 설정		
			 if(info.firstimage != null){ //이미지 값이 있을 때
				htmlval += `
				<div class="spot-img wfull hfull"><img class=" wfull hfull" src="${info.firstimage}" ></div>
				`;
			 }else{//이미지 값이 없을 때
				htmlval += `
				<div class="spot-img wfull hfull"><img class=" wfull hfull" src='/images/icons/spot_sample.png' ></div>
				`;
			 }
			 		
			//이동시간 표시 및 자동차 아이콘 표시 		
	 		if( (j+1) < daylength){
				htmlval += `
				<div class="spot-caricon"><img style="width:20px;height: 20px;" src="/images/icons/carIcon.png" /></div>
				<a class="spot-move" href="https://map.kakao.com/?target=car&sName=${info.title}&eName=${details.dayDetailInfoEntity[j+1].title}"  target="_blank">${info.durationMin} 분> </a>
				`;
			}else{// 숙소에 도착했을 땐 이동시간 표시 X
				htmlval += `
				<div class="spot-caricon hide"><img style="width:20px;height: 20px;" src="${contextPath}images/icons/carIcon.png" /></div>
				<div class="spot-move hide"> </div>
				`;
			}

		    htmlval +=`</div></div>`;
			// >>>>>>>>>>>>>>>>> 백틱

	    }/* 반복문 종료(j)*/
		
		dayPoints[i] = points;
		
		htmlval += `
		</div>	 </div>  
		`;
	}/*반복문 종료(i)*/
	//장소정보 넣기
	$(".tourlist .wrap-detaillist.flex").html(htmlval);
	//nav버튼 일차 수 만큼 넣기
	$(".whole").after(navHtmlval);
}

