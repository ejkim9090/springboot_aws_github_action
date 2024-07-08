//회원가입 버튼 활성화
function chNickActive(){
	if($("#memNick-no").hasClass('hide')){
		$(".btn.save").attr("disabled", false);
	}else{
		$(".btn.save").attr("disabled", true);
	}
};