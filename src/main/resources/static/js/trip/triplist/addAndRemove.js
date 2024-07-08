function addAndRemoveHandler(){
	//유저삭제
	$('.btn.remove').on('click',removeHandler);
	//유저추가
	$('.btn.add').on('click',addHandler);
}
//삭제
function removeHandler(){
	var removeNick =$(this).prev().text();
	var planId =$(this).parents(".trip-list.wfull").data('plan-id');
	$.ajax({
		beforeSend : csrfHandler,
		error : ajaxErrorHandler,
		url: contextPath+"trip/remove/nick",
		method:"post",
		context:this,//.btn.add
    	data: {planId:planId,removeNick: removeNick},
		success : function(result) {
			$(this).removeClass('remove');
			$(this).addClass('add');
			$(this).text('추가');
			addAndRemoveHandler();
		}
	});
}
//추가
function addHandler(){
	var addNick =$(this).prev().text();
	var planId =$(this).parents(".trip-list.wfull").data('plan-id');
	$.ajax({
		beforeSend : csrfHandler,
		error : ajaxErrorHandler,
		url: contextPath+"trip/add/nick",
		method:"post",
		context:this,//.btn.add
    	data: {planId:planId,addNick: addNick},
		success : function(result) {
			$(this).removeClass('add');
			$(this).addClass('remove');
			$(this).text('삭제');
			addAndRemoveHandler();
		}
	});
}