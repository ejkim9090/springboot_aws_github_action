$(loadedHanlder);

function loadedHanlder(){
	var url = window.location.pathname;
	//var param = window.location.search;
	$.ajax({
		beforeSend : csrfHandler,
		error : ajaxErrorHandler,
		url: url+"/info",
		method:"post",
		dataType:"json",
		success : function(dayEntityList) {
	        dayEntityList_org = dayEntityList;
			setEvent();
        }
	});
}
function setEvent(){
	//화면에 뿌릴 장소 정보 백틱으로 한번에 담아오기
	displayInfo();

	//map 화면 출력
	displayMap();
	
	//지도 범위 재설정 함수 실행
	setBounds();
	
	//일차별 동그라미 색 변경
	circleColorHandler();
	
	//좌측 탭 이벤트 설정
	navHandler();
	

	//편집
	$(".edit").click(editHandler);

	//hover 시 메모내용 표시
	$(".img-memo").hover(  function(e){      
			$(this).siblings('.memo').removeClass('hide');
			//사용자의 화면을 기준으로 X좌표 표시
			$(this).siblings('.memo').css('left',e.screenX+'px');
			$(this).siblings('.memo').css('opacity','1')
		},memoNoHandler);

}
