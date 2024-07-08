// 시간 설정 칸 열림
function stayTimeRangeBtnClickHandler(thisElement) {
	$(thisElement).children(".timerange").addClass('hide');
	$(thisElement).parents('.spot-block').children(".timerange-modal").removeClass('hide');
}

// 시간 설정 완료
function timeDoneBtnClickHandler(thisElement) {
	$(thisElement).parents('.spot-block').children(".timerange").removeClass('hide');
	$(thisElement).parents(".timerange-modal").addClass('hide');
	var idx=$(thisElement).parents('.spot-block').data('i');
	var jdx=$(thisElement).parents('.spot-block').data('j');
	
	
	var hours = $(thisElement).parent().children('.spot-hours').val();
	var mins = $(thisElement).parent().children('.spot-mins').val();
	var key = $(thisElement).parents('.spot-block.draggable').data('sessionkey');
	
	var timeVal = hours*3600 + mins*60;
	// *** TODO  storage 안 씀 변경
	var temp = JSON.parse(editStorage.getItem(key));
	
	detailListEditMode[idx].dayDetailInfoEntity[jdx].stayTime = timeVal;
	
	// 수정된 객체를 다시 JSON 문자열로 변환하여 sessionStorage에 저장
	editStorage.setItem(key, JSON.stringify(temp));
	
	displayEditModeAfterDragEnd();
	circleColorHandler();
}

//memo 이미지에서 마우스가 벗어났을 때
function memoNoHandler(){
	$(this).siblings('.memo').addClass('hide');
}

//memo 내용을 추가할 모달창 생성 memo관련 함수 async필수
async function memoClickHandler(el){
    // 'memo' 요소의 텍스트를 가져옵니다.
    memoText = $(el).siblings('.memo').text();
	var i =$(el).parents('.spot-block').data('i');
	var j =$(el).parents('.spot-block').data('j');
    // Swal.fire 다이얼로그를 표시하고 사용자의 입력을 기다린다
    const { value: memo } = await Swal.fire({
        input: "textarea",
        inputLabel: "메모작성",
        inputValue: memoText,
        inputPlaceholder: "여행에 필요한 정보를 이곳에 작성해보세요. 최대 900자",
        inputAttributes: {
            maxlength: "900"
        },
        showCancelButton: true,
        confirmButtonColor: "#000000", 
        confirmButtonText: "확인"
    });
    if (memo && memo.trim().length > 0) {
        Swal.fire("<h1>저장완료</h1>");
		//배열의 memo값에 변경된 memo값 넣기
		detailListEditMode[i].dayDetailInfoEntity[j].memo=memo;
        //display값 변경해주기
        $(el).siblings('.memo').text(memo);
    }
}
//스팟 삭제하기
function removeSpot(el){
	spotTitle = $(el).prevAll('.spot-title').text();
	var idx = $(el).parents('.spot-block').data('i');
	var jdx = $(el).parents('.spot-block').data('j');
	console.log("title");
	console.log(spotTitle);
	Swal.fire({
	  title: "<h2>"+spotTitle+"</h2>",
	  text: "삭제하시겠습니까?",
	  showCancelButton: true,
	  confirmButtonColor: "#000000",
	  cancelButtonColor: "#d33",
	  confirmButtonText: "삭제",
	  cancelButtonText: "취소"
	}).then((result) => {
	  if (result.isConfirmed) {
		//배열에서 삭제후 화면 리로드
		var details =  detailListEditMode[idx];
		var daylength = details.dayDetailInfoEntity.length;
		
		if(jdx == daylength-1){
		//배열의 마지막 인덱스일 때	
			details.dayDetailInfoEntity.pop();
		}else{
			for(var j = jdx; j < daylength - 1 ; j++){
				details.dayDetailInfoEntity[j] = details.dayDetailInfoEntity[j+1];
			}
			details.dayDetailInfoEntity.pop();
		}
		//배열에서 장소 삭제 후 알람 띄우기
	    Swal.fire({
	      title: "Deleted!",
	      text: "해당 장소를 일정에서 삭제하였습니다.",
	      icon: "success"
	    });
	    

		// *** 편집된 내용 다시 display
		displayEditModeAfterDragEnd();
		//일차별 동그라미 색 변경
		circleColorHandler();
		//maker display 
		displayMarker();
		//드래그 앤 드랍
		dragAndDrop();
	  }
	});
}

//편집페이지에서 장소 삭제하기
function deleteSpotEditMode(idx,jdx){
	var details =  detailListEditMode[idx];
	var daylength = details.dayDetailInfoEntity.length;
	
	if(jdx == daylength-1){
	//배열의 마지막 인덱스일 때	
		details.dayDetailInfoEntity.pop();
	}else{
		for(var j = jdx; j < daylength - 1 ; j++){
			details.dayDetailInfoEntity[j] = details.dayDetailInfoEntity[j+1];
		}
		details.dayDetailInfoEntity.pop();
	}
}
//편집 화면 들어갈 때 첫 display 
function displayEditMode(){
	
	var htmlval = "";
	
 	detailListEditMode= JSON.parse(JSON.stringify(detailList));
	
	for(var i=0; i<detailListEditMode.length; i++ ){
		//DayEntity를 list에 담기
		details =  detailListEditMode[i];
		// <<<<<<<<<<<<<<< 백틱
		//좌측 탭 태그 넣기
		htmlval+=`
				<div class="column flex" data-columns="${i+1}" id="tab${i+1}">
					<div class="sub-title flex ">
						<h4 class="nday">${i+1}일차</h4>
						<h6  class="date">${details.travelDate}</h6>
					</div>
				 	<div  class="container flex wfull">
				 	`;
				 	
		// >>>>>>>>>>>>>>>>>> 백틱
		
	 	//장소별 정보 넣기
		var daylength = details.dayDetailInfoEntity.length;
		var points = new Array(daylength); // 하루동안 장소들 지도에 표시될 위치 // 초기화
	 	for(var j=0; j< daylength; j++ ){
			
			info =  details.dayDetailInfoEntity[j];
			
			//머무는 시간 바꾸기
			let[hours, minutes] = secToHoursAndMin(info.stayTime);
			
			// <<<<<<<<<<<<<<< 백틱
			//백틱에 값 넣기
			htmlval+=`
			<div class="spot-block draggable"  draggable ="true" data-i="${i}", data-j="${j}">
				<div class="timerange-modal hide" >
					<div><p style="margin-left:30px; font-weight: bold; padding: 10px 0;">머무는 시간 설정</p></div>
					<div class="flex ">
						<input class="spot-hours" type='number' value=${hours} min="0" max="23"></input>
						<p>시간</p>
						<input class="spot-mins" type='number' value=${minutes} min="0" max="55" step="5"></input>
						<p>분</p>
						<p class="timerange-done" onclick="timeDoneBtnClickHandler(this);">완료</p>
					</div>
				</div>`;
				
				
			//info spot div 정제해서 넣기 //장소정보	
			htmlval+=`
			 	<div class="spot grid wfull" >
			 		<div class="spot-number backimg"><p>${j+1}</p></div>
			 		<div class="spot-staytime" onclick="stayTimeRangeBtnClickHandler(this);" data-startTime="${info.startTime}" data-endTime="${info.endTime}">
			 			<p class="timerange">${info.startTime} - ${info.endTime}</p>
			 		</div>
			 		
			 		<div class="spot-type">명소</div>
			 		<div class="spot-title wfull"> ${info.title}</div>
			 		<div class="spot-memo"><img class="img-memo" onclick="memoClickHandler(this);" style="width: 20px;height:20px;" src="/images/icons/memoIcon.png" ><span  class="memo">${info.memo}</span></div>
			 		
			 		<!-- 이미지 X-->
			 		`;
			
			//이동시간 표시 및 자동차 아이콘 표시 
			//마지막  상소일 경우 이동시간 hide
			if( (j+1) < daylength){
				htmlval+=`
					<div class="spot-caricon"><img style="width:20px;height: 20px;" src="/images/icons/carIcon.png" /></div>
					<div class="spot-move"> ${info.durationMin}분> </div>`;
			}else{
				htmlval+=`
					<div class="spot-caricon hide"><img style="width:20px;height: 20px;" src="/images/icons/carIcon.png" /></div>
					<div class="spot-move">  </div>`;
			}
			
			htmlval+=`<!-- x 버튼 -->
					<button class="spot-remove"  onclick="removeSpot(this);"> 
						<img class="wfull img trash-bin "  src="/images/icons/cancel.png"/>
					</button>
			    </div><!-- spot  -->
			`;	
				
			//spot-block 닫기	
			htmlval+=`</div> <!--spot-block  -->`;
			// >>>>>>>>>>>>>>>>> 백틱
		} /* 반복문 종료(j)*/
					
		htmlval+=`	
					</div> <!-- container  -->
				</div>	<!-- column -->
				`;
		// >>>>>>>>>>>>>>>>> 백틱
	}//반복문 종료(i)
	
	//장소정보 넣기
	$(".edit-tourlist .wrap-detaillist.flex").html(htmlval);
}

//드래그 앤 드롭 시 display
function displayEditModeAfterDragEnd(){
	//
	clearPolylines();
	//html에 뿌릴 정보 백틱에 담기
	var htmlval = "";
	
	//이동시간 변수  - 해당 function 안에서만 사용되고 durationMin은 배열에 담음
	var duration ="";
	var prevDuration ="";
	
	dayPoints = [];
	dayPoints = new Array(detailListEditMode.length); // (전체일정)일정 날마다 장소들 지도에 표시될 위치 (points 담을 배열)// 초기화
	for(var i=0; i<detailListEditMode.length; i++ ){
		
		//DayEntity를 list에 담기
		details =  detailListEditMode[i];
		
		// <<<<<<<<<<<<<<< 백틱
		//좌측 탭 태그 넣기
		htmlval+=`
				<div class="column flex" data-columns="${i+1}" id="tab${i+1}">
					<div class="sub-title flex ">
						<h4 class="nday">${i+1}일차</h4>
						<h6  class="date">${details.travelDate}</h6>
					</div>
				 	<div  class="container flex wfull">
				 	`;
		// >>>>>>>>>>>>>>>>>> 백틱
		
	 	//장소별 정보 넣기
		var daylength = details.dayDetailInfoEntity.length;
		var points = new Array(daylength); // 하루동안 장소들 지도에 표시될 위치 // 초기화
	 	for(var j=0; j< daylength; j++ ){
			
			info =  details.dayDetailInfoEntity[j];
			
			//머무는 시간 바꾸기
			let[hours, minutes] = secToHoursAndMin(info.stayTime);
			
			//다음 장소로 이동시간(sec), 분단위로 변환하여 변수에 담기 
			if((j+1) < daylength){
				duration = durationHandler(info.lng,info.lat,details.dayDetailInfoEntity[j+1].lng,details.dayDetailInfoEntity[j+1].lat);
				info.durationMin=Math.ceil(duration/60); // ceil : 소수점 올림
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
			
			
			
			//map에서 lng lat 값 넣기 KakaoMap Api
			var  point = new kakao.maps.LatLng(info.lat*1, info.lng*1);
			points[j] = point;
			
			// <<<<<<<<<<<<<<< 백틱
			//백틱에 값 넣기
			htmlval+=`
			<div class="spot-block draggable"  draggable ="true" data-i="${i}", data-j="${j}">
				<div class="timerange-modal hide" >
					<div><p style="margin-left:30px; font-weight: bold; padding: 10px 0;">머무는 시간 설정</p></div>
					<div class="flex ">
						<input class="spot-hours" type='number' value=${hours} min="0" max="23"></input>
						<p>시간</p>
						<input class="spot-mins" type='number' value=${minutes} min="0" max="55" step="5"></input>
						<p>분</p>
						<p class="timerange-done" onclick="timeDoneBtnClickHandler(this);">완료</p>
					</div>
				</div>`;
				
				
			//info spot div 정제해서 넣기 //장소정보	
			htmlval+=`
			 	<div class="spot grid wfull" >
			 		<div class="spot-number backimg"><p>${j+1}</p></div>
			 		<div class="spot-staytime" onclick="stayTimeRangeBtnClickHandler(this);" data-startTime="${info.startTime}" data-endTime="${info.endTime}">
			 			<p class="timerange" style="cursor: pointer;">${info.startTime} - ${info.endTime}</p>
			 		</div>
			 		
			 		<div class="spot-type">명소</div>
			 		<div class="spot-title wfull"> ${info.title}</div>
			 		<div class="spot-memo"><img class="img-memo"  onclick="memoClickHandler(this);" style="width: 20px;height:20px;cursor: pointer;" src="/images/icons/memoIcon.png" ><span class="memo">${info.memo}</span></div>
			 		
			 		<!-- 이미지 X-->
			 		`;
			
			//이동시간 표시 및 자동차 아이콘 표시 
			//마지막  상소일 경우 이동시간 hide
			if( (j+1) < daylength){
				htmlval+=`
					<div class="spot-caricon"><img style="width:20px;height: 20px;" src="/images/icons/carIcon.png" /></div>
					<div class="spot-move"> ${info.durationMin}분> </div>`;
			}else{
				htmlval+=`
					<div class="spot-caricon hide"><img style="width:20px;height: 20px;" src="/images/icons/carIcon.png" /></div>
					<div class="spot-move">  </div>`;
			}
			
			htmlval+=`<!-- x 버튼 -->
					<button class="spot-remove" onclick="removeSpot(this);"> 
						<img class="wfull img trash-bin "  src="/images/icons/cancel.png"/>
					</button>
			    </div><!-- spot  -->
			`;	
				
			//spot-block 닫기	
			htmlval+=`</div> <!--spot-block  -->`;
			// >>>>>>>>>>>>>>>>> 백틱
		} /* 반복문 종료(j)*/
		dayPoints[i] = points;			
		htmlval+=`	
					</div> <!-- container  -->
				</div>	<!-- column -->
				`;
		// >>>>>>>>>>>>>>>>> 백틱
	}//반복문 종료(i)
	
	//장소정보 넣기
	$(".edit-tourlist .wrap-detaillist.flex").html(htmlval);
}
