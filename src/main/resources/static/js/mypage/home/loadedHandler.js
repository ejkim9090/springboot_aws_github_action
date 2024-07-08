$(loadedHandler);
function loadedHandler(){
	//버튼 호버 css
	$('.btn').hover(function(){
		$(this).css('background-color', 'lightgray');
		$(this).css('color', 'black');
	}, function(){
		$(this).css('background-color', 'black');
		$(this).css('color', 'white');
	});
	//닉네임 변경 페이지로 이동
	$('.btn.chNick').on('click', function(){
		location.href = '/my/nick';
	});
	//비밀번호 변경 페이지로 이동
	$('.btn.chPwd').on('click', function(){
		location.href = '/my/pwd';
	});
	
	// 카카오 연동 해제
	$(".button.unlink-kakao").on("click", unlinkKakaoHandler);
	// 네이버 연동 해제
	$(".button.unlink-naver").on("click", unlinkNaverHandler);
	// 구글 연동 해제
	$(".button.unlink-google").on("click", unlinkGoogleHandler);
}