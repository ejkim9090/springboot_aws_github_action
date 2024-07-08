//닉네임 검사
function nickCheckHandler(){
	const nickExp = /^[가-힣]{2,8}$/;
	const nickInput = $("input#memNick").val();
	$.ajax({
		beforeSend : csrfHandler,
		error : ajaxErrorHandler,
		url: contextPath+'join/nick/check', 
		type: 'post', 
		data: {memNick: nickInput}, 
		success: function(result){
			if(nickExp.test(nickInput) == true){
				if(result == 0){
					$("#memNick-yes").removeClass('hide');
					$("#memNick-no").addClass('hide');
					$("#exist-nick").addClass('hide');
					joinActive();
				}else{
					$("#memNick-yes").addClass('hide');
					$("#memNick-no").removeClass('hide');
					$("#exist-nick").removeClass('hide');
					joinActive();
				}
			}else{
				$("#memNick-yes").addClass('hide');
				$("#memNick-no").removeClass('hide');
				$("#exist-nick").addClass('hide');
				joinActive();
			}
		}
	});
}
//비밀번호 검사
function pwdCheckHandler(){
	const pwdExp1 = /^[0-9A-Za-z]{8,20}$/;
	const pwdExp2 = /^.*[0-9].*$/;
	const pwdExp3 = /^.*[A-Z].*$/;
	const pwdExp4 = /^.*[a-z].*$/;
	const pwdInput = $("input#memPassword").val();
	if(pwdExp1.test(pwdInput) == true && 
	   pwdExp2.test(pwdInput) == true && 
	   pwdExp3.test(pwdInput) == true && 
	   pwdExp4.test(pwdInput) == true){
		$("#memPassword-yes").removeClass('hide');
		$("#memPassword-no").addClass('hide');
		joinActive();
	}else{
		$("#memPassword-yes").addClass('hide');
		$("#memPassword-no").removeClass('hide');
		joinActive();
	}
}
//비밀번호 보기
function seePwdHandler(){
	if($(this).is(":checked")){
		$("#memPassword").prop("type", "text");
	}else{
		$("#memPassword").prop("type", "password");
	}
}