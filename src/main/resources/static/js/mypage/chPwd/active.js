//회원가입 버튼 활성화
function chPwdActive(){
	if($("#memPassword-no").hasClass('hide') &&
		$('#currPwd').hasClass('hide')){
		$(".btn.save").attr("disabled", false);
	}else{
		$(".btn.save").attr("disabled", true);
	}
};