$(loadedHandler);
function loadedHandler() {
	//버튼:hover css
	$('.btn').hover(function() {
		if (!$(this).prop('disabled')) {
			$(this).css('background-color', 'lightgray');
			$(this).css('color', 'black');
		}
	}, function() {
		if (!$(this).prop('disabled')) {
			$(this).css('background-color', 'black');
			$(this).css('color', 'white');
		}
	});

	// 총 가격 계산
	$('input[name=cart-pick]').on('click', sumCalcHandler);

	// 버튼 활성화
	$('input[name=cart-pick]').on('click', btnActiveHandler);

	// 장바구니 삭제
	$('.btn.del').on('click', cartDelHandler);

	// 장바구니 구매
	$('.btn.pay').on('click', requestPayment);

}