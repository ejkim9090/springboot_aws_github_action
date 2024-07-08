//장바구니 구매
async function requestPayment() {
	let items = [];
	$('input:checked').each(function() {
		items.push($(this).val());
	});
	let orderName;
	let itemName = $('input:checked').first().parent().prev().prev().text();
	let itemNum = $('input:checked').length;
	const totalAmount = $('input[name=pay]').val();
	if (itemNum > 1) {
		orderName = itemName + ' 외 ' + (itemNum - 1) + '건';
	} else {
		orderName = itemName;
	}

	// 결제창 표시
	const response = await PortOne.requestPayment({
		storeId : storeId, // 고객사 storeId로 변경해주세요.
		paymentId : buyId,
		orderName : orderName,
		totalAmount : totalAmount,
		currency : "CURRENCY_KRW",
		channelKey : channelKey, // 콘솔 결제 연동 화면에서 채널 연동 시 생성된 채널 키를 입력해주세요.
		payMethod : "CARD",
		customer : {
			fullName : memNick,
			phoneNumber : memTel,
			email : memEmail,
		}
	});

	if (response.code) {
		// 오류 발생
		return Swal.fire({
			text: "결제 중 오류가 발생했습니다.",
			icon: "error", 
			confirmButtonColor: "#000000", 
			confirmButtonText: "확인"
		});
	} else {
		// 결제 검증
		$.ajax({
			beforeSend : csrfHandler,
			error : ajaxErrorHandler,
			url : contextPath + "store/payment",
			type : "post",
			data : {
				paymentId : response.paymentId,
				totalAmount : totalAmount,
				items : items,
				buyId : buyId
			}, 
			success : async function(data) {
				if (data == 1) {
					Swal.fire({
						text: "결제가 완료되었습니다.\n구매내역으로 이동하시겠습니까?", 
						icon: "success", 
						showCancelButton: true, 
						confirmButtonText: "이동하기", 
						confirmButtonColor: "#000000", 
						cancelButtonText: "돌아가기", 
						cancelButtonColor: "#ff0000"
					}).then((swal) => {
						if(swal.isConfirmed){
							location.href = contextPath + "store/buy";
						}
					});
					return;
				} else {
					Swal.fire({
						text: "결제 금액과 지불 금액이 일치하지 않거나 알 수 없는 오류가 발생했습니다.", 
						icon: "error", 
						confirmButtonText: "확인", 
						confirmButtonColor: "#000000", 
					});
					return;
				}
			}
		});
	}
}