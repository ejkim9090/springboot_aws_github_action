// 버튼 활성화
function btnActiveHandler() {
	let checkNum = $('input[name=cart-pick]:checked').length;
	if (checkNum == 0) {
		$('.btn').prop('disabled', true);
	} else {
		$('.btn').prop('disabled', false);
	}
}