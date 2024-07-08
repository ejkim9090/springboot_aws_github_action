$(loadedHandler);
function loadedHandler() {
	//버튼:hover css
	$('.btn').hover(function() {
		if (!$(this).prop('disabled')) {
			$(this).css('background-color', 'lightgray');
			$(this).css('color', 'black');
		}
	}, function() {
		if (!$(this).prop('disabled')) {
			$(this).css('background-color', 'black');
			$(this).css('color', 'white');
		}
	});
	//인증번호 입력창 숨김
	$(".inputbtn.check").addClass('hide');
	//이메일 공란 시 경고 문구
	$("input#memEmail").on("input", writeEmailHandler);
	//인증코드 발송
	$(".btn.sendCode").on("click", codeSendHandler);
	//인증코드 확인
	$(".btn.checkCode").on("click", codeCheckHandler);
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