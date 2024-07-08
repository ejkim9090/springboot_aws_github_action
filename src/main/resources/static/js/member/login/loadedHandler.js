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
}