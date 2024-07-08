$(loadedHandler);
function loadedHandler(){
	AOS.init();
	// 버튼 호버 css
	$('.btn').hover(function(){
		$(this).css('background-color', 'var(--color_light_gray)');
		$(this).css('color', 'black');
	}, function(){
		$(this).css('background-color', 'black');
		$(this).css('color', 'white');
	});
	
	// 상품 선택 시 개수 표현
	$('input').on('click', itemNumHandler);
	
	// 글꼴 미리보기 창
	// $('.btn.font').on('click', modalOpenHandler);
	$('.btn.font').on('click', fontSwalHandler);
	
	// 글꼴 모달 닫기
	// $('.btn.modal-close').on('click', modalCloseHandler);
	
	// 장바구니에 담기
	$('.btn.cart').on('click', insertItemsHandler);
}