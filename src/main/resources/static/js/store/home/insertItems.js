// 장바구니에 담기
function insertItemsHandler(){
	let items = [];
	$('input:checked').each(function(index, element){
		items.push(element.value);
	});
	console.log(items);
	$.ajax({
		beforeSend : csrfHandler,
		error : ajaxErrorHandler,
		url: contextPath+'store/insert', 
		type: 'post', 
		data: {items : items}, 
		success: function(result){
			if(result == 1){
				Swal.fire({
					text: "선택하신 상품이 장바구니에 담겼습니다.\n장바구니로 이동하시겠습니까?", 
					icon: "question", 
					showCancelButton: true, 
					confirmButtonText: "이동하기", 
					confirmButtonColor: "#000000", 
					cancelButtonText: "돌아가기", 
					cancelButtonColor: "#ff0000"
				}).then((swal) => {
					if(swal.isConfirmed){
						location.href = contextPath + "store/cart";
					}else{
						location.reload();					
					}
				});
			}else{
				Swal.fire({
					text: "장바구니에 상품을 담는 중 오류가 발생했습니다.\n관리자에게 문의해주시기 바랍니다.", 
					icon: "error", 
					confirmButtonColor: "#000000", 
					confirmButtonText: "확인"
				});
			}
		}
	});
}