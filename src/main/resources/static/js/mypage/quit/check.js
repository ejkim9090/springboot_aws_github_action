//현재 비밀번호 검사
function pwdCheckHandler(){
	const pwdInput = $("input#memPassword").val();
	$.ajax({
		beforeSend : csrfHandler,
		error : ajaxErrorHandler,
		url: contextPath+'my/pwd/use', 
		type: 'post', 
		data: {memPassword: pwdInput}, 
		success: function(result){
			console.log(result);
			if(result == 1){
				$("#memPassword-yes").removeClass('hide');
				$("#memPassword-no").addClass('hide');
				quitActive();
			}else{
				$("#memPassword-yes").addClass('hide');
				$("#memPassword-no").removeClass('hide');
				quitActive();
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
