//탈퇴하기 버튼 활성화
function quitActive(){
	if($("#memPassword-no").hasClass('hide')){
		$(".btn.quit").attr("disabled", false);
	}else{
		$(".btn.quit").attr("disabled", true);
	}
};