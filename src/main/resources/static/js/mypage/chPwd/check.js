//비밀번호 검사
function pwdCheckHandler(){
	const pwdExp1 = /^[0-9A-Za-z]{8,20}$/;
	const pwdExp2 = /^.*[0-9].*$/;
	const pwdExp3 = /^.*[A-Z].*$/;
	const pwdExp4 = /^.*[a-z].*$/;
	const pwdInput = $("input#memPassword").val();
	$.ajax({
		beforeSend : csrfHandler,
		error : ajaxErrorHandler,
		url: contextPath+'my/pwd/use', 
		type: 'post', 
		data: {memPassword: pwdInput}, 
		success: function(result){
			if(pwdExp1.test(pwdInput) == true && 
			   pwdExp2.test(pwdInput) == true && 
			   pwdExp3.test(pwdInput) == true && 
			   pwdExp4.test(pwdInput) == true){
				if(result == 1){
					$('#currPwd').removeClass('hide');
					$("#memPassword-yes").addClass('hide');
					$("#memPassword-no").removeClass('hide');
					chPwdActive();
				}else{
					$('#currPwd').addClass('hide');
					$("#memPassword-yes").removeClass('hide');
					$("#memPassword-no").addClass('hide');
					chPwdActive();
				}
			}else{
				$('#currPwd').addClass('hide');
				$("#memPassword-yes").addClass('hide');
				$("#memPassword-no").removeClass('hide');
				chPwdActive();
			}
		}, 
	});
}
//비밀번호 보기
function seePwdHandler(){
	if($(this).is(":checked")){
		$("#memPassword").prop("type", "text");
	}else{
		$("#memPassword").prop("type", "password");
	}
}
