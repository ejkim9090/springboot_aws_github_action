//회원가입 버튼 활성화
function setActive(){
	if($("#memPassword-no").hasClass('hide')){
		$("button.btn.set").attr("disabled", false);
	}else{
		$("button.btn.set").attr("disabled", true);
	}
};