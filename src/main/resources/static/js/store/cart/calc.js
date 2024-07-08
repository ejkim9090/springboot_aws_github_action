// 총 가격 계산
function sumCalcHandler() {
	let sum = 0;
	$('input[name=cart-pick]:checked').each(function() {
		sum += $(this).next().val() * 1;
	});
	$('input[name=pay]').val(sum);
	let sum2 = sum.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
	let text = '총 가격 : ' + sum2 + ' 원';
	$('.sum').text(text);
}