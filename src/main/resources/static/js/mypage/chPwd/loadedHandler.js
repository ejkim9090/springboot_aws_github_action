$(loadedHandler);
function loadedHandler(){
	//버튼:hover css
	$('.btn').hover(function(){
		if(!$(this).prop('disabled')){
			$(this).css('background-color', 'lightgray');
			$(this).css('color', 'black');
		}
	}, function(){
		if(!$(this).prop('disabled')){
			$(this).css('background-color', 'black');
			$(this).css('color', 'white');
		}
	});
	//비밀번호 검사
	$("input#memPassword").on("input", pwdCheckHandler);
	//비밀번호 보기
	$("#seePwd").on("click", seePwdHandler);
	//돌아가기 버튼
	$('.btn.back').on('click', backClickHandler);
	//저장 버튼
	$('.btn.save').on('click', saveClickHandler);
}