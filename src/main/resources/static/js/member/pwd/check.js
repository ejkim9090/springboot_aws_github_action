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
		setActive();
	}else{
		$("#memPassword-yes").addClass('hide');
		$("#memPassword-no").removeClass('hide');
		setActive();
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