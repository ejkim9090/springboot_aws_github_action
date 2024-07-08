// 상품 선택 시 개수 표현
function itemNumHandler(){
	let itemNum = $('input:checked').length;
	if(itemNum > 0){
		$('.checked').removeClass('hide');
		$('#itemNum').text(itemNum);
	}else{
		$('.checked').addClass('hide');
	}
}