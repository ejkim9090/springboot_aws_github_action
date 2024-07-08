$(loadedHandler);
function loadedHandler(){
	//버튼:hover css
	$('.btn').hover(function(){
		if(!$(this).prop('disabled')){
			$(this).css('background-color', 'black');
			$(this).css('color', 'white');
		}
	}, function(){
		if(!$(this).prop('disabled')){
			$(this).css('background-color', 'var(--color_light_gray)');
			$(this).css('color', 'black');
		}
	});
	//닉네임 검사
	$("input#memNick").on("input", nickCheckHandler);
	//비밀번호 검사
	$("input#memPassword").on("input", pwdCheckHandler);
	//비밀번호 보기
	$("#seePwd").on("click", seePwdHandler);
	//휴대폰 번호 입력
	$("#memTel").on("input", telCheckHandler);
	//생년월일 입력 검사
	$("input#memBirth").on("change", joinActive);
	//약관 동의 클릭 이벤트
	$("#agree-all").on("click", agreeAllHandler);
	$("input[name=agree]").on("click", agreeDivHandler);
	//구글 리캡차v2
	$("#frm-join").on("submit", robotHandler);
}