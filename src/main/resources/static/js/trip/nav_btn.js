 /*편집 취소 저장 */
 //편집
function editHandler(){
	
	//편집페이지 정보 로드	
	displayEditMode(); 
	// ejkim

	//일차별 동그라미 색 변경
	circleColorHandler();
	//드래그 앤 드랍
	dragAndDrop();
	//편집 취소
	$(".cancel").click(cancelHandler);
	//편집 저장
	$(".save").click(saveHandler);
	
	//전체일정 선택 후 편집창 띄우기
	navHandler();
	//취소 버튼 활성화
	$(this).next().show(); 
	//n일차 btn 숨김
	$(this).parent().prevAll().find('.dayn').not(':first').hide(); 
	//편집 숨김
	$(this).hide();

	//저장btn 활성화 css변경
	$(this).siblings('.save').attr('disabled',false);
	//일정 목록보여주기
	$(this).parents().find('.tourlist').addClass('hide');
	$(this).parents().find('.edit-tourlist').removeClass('hide');
	$(this).parents().find('.spot-basket').removeClass('hide');
}
//취소
function cancelHandler(){
	//전체일정 클릭
	navHandler();
	//취소 숨김
	$(this).hide(); 
	//편집 보여줌
	$(this).prev().show(); 
	//저장 비활성화
	$(this).siblings('.save').attr('disabled',true); 
	//n일차 btn 보여줌
	$(this).parent().prevAll().find('.dayn').show(); 
	//담기 숨김
	$('#add-btn').addClass('hide');
	
	//일정 목록보여주기
	$(this).parents().find('.tourlist').removeClass('hide');
	$(this).parents().find('.edit-tourlist').addClass('hide');
	$(this).parents().find('.spot-basket').addClass('hide');
	//spot-check
	if($('#tab02').hasClass('hide')===false){
		$('#tab02').addClass('hide');
	}
}
//저장
function saveHandler(){
	//전체일정 클릭
	navHandler();
	$(this).siblings('.cancel').hide(); //취소 숨김
	$(this).siblings('.edit').show(); //편집 보여줌
	$(this).attr('disabled',true); //저장 비활성화
	$(this).parent().prevAll().find('.dayn').show(); //n일차 btn 보여줌
	$('#add-btn').addClass('hide');//담기 숨김
	//일정 목록보여주기
	$(this).parents().find('.tourlist').removeClass('hide');
	$(this).parents().find('.edit-tourlist').addClass('hide');
	$(this).parents().find('.spot-basket').addClass('hide');
	
	//spot-check
	if($('#tab02').hasClass('hide')===false){
		$('#tab02').addClass('hide');
	}
	

	//DB이동 ajax
	saveChanges();
}
//좌측 탭
function navHandler(){
	//좌측 탭
	$('.dayn a').click(function() {
		$('.dayn a').css("color", "black");
		//클릭한 버튼만 css 변경
		$(this).css("color", "#4BC9E5");
		
		//전체일정일 때, 일일 버튼 클릭할 때에 따른 일정 display
		hash = $(this).prop('hash');
		if(hash == '#tab0'){
			$('.column.flex').removeClass('hide');
		}
		else{
			$('.column.flex').addClass('hide');
			$(hash).removeClass('hide');
		}

		return false;
	}).filter(':eq(0)').click();
}

function saveChanges(){
		//안 쓰는 데이터 빼내기
		//삭제할 key : durationMin, endTime, startTime
		for(var i = 0 ; i < detailListEditMode.length;i++){
			editmode = detailListEditMode[i];
			
			for(var j = 0 ;j < editmode.dayDetailInfoEntity.length; j++){
				item = editmode.dayDetailInfoEntity[j];
				
				delete item.durationMin;
				delete item.endTime;
				delete item.startTime;
				//변경된 방문순서 key에 넣어주기
				item.travelOrder = j + 1; 
			}
		}
		saveData = JSON.stringify(detailListEditMode);
		//jjoggan TODO
		$.ajax({
		beforeSend : csrfHandler,
		error : ajaxErrorHandler,
		url: contextPath+"trip/save/changes",
		method:"post",
		contentType: "application/json", //보낼 데이터 타입 //; charset=utf-8 //필요??불필요??/
		data:{saveData : saveData},
		success : function(result) {
			
        }
	});
}