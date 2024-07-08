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
					chNickActive();
				}else{
					$("#memNick-yes").addClass('hide');
					$("#memNick-no").removeClass('hide');
					$("#exist-nick").removeClass('hide');
					chNickActive();
				}
			}else{
				$("#memNick-yes").addClass('hide');
				$("#memNick-no").removeClass('hide');
				$("#exist-nick").addClass('hide');
				chNickActive();
			}
		}
	});
}
