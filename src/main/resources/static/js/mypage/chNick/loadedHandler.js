$(loadedHandler);
function loadedHandler(){
	//버튼:hover css
	$('.btn').hover(function(){
		if(!$(this).prop('disabled')){
			$(this).css('background-color', 'lightgray');
			$(this).css('color', 'black');
		}
	}, function(){
		if(!$(this).prop('disabled')){
			$(this).css('background-color', 'black');
			$(this).css('color', 'white');
		}
	});
	//닉네임 검사
	$("input#memNick").on("input", nickCheckHandler);
	//돌아가기 버튼
	$('.btn.back').on('click', backClickHandler);
	//저장 버튼
	$('.btn.save').on('click', saveClickHandler);
}