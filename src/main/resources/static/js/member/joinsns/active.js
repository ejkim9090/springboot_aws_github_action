//회원가입 버튼 활성화
function joinActive(){
	if($("#memNick-no").hasClass('hide') && 
			$("#memPassword-no").hasClass('hide') &&
			$("#memTel").val().length > 9 && 
			$("#memBirth").val() != '' &&
			$("#agree-all").is(":checked")){
		$("button.btn.join").attr("disabled", false);
	}else{
		$("button.btn.join").attr("disabled", true);
	}
};