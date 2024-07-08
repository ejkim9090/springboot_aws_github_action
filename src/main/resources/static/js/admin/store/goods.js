$(loadedHandler);

function loadedHandler() {
	// 상품 추가 버튼
	$(".btn.insert").on("click", itemInsertHandler);
	
	// 상품 수정 버튼
	$(".btn.update").on("click", itemUpdateHandler);
	
	//상품삭제버튼
	$(".btn.delete").on("click", itemDeleteHandler);
	
	//상품검색
	//$(".btn-search").on("click",searchHandler);
}

// 상품 수정
async function itemUpdateHandler(){
	let itemCode = $(this).parents(".col.list").children("li:first-child").text();
	let itemName = "";
	let itemPrice = "";
	let itemDur = "";
	let itemSale = "";
	let itemColor = "";
	let itemSrc = "";
	const itemInfo = await $.ajax({
		beforeSend : csrfHandler,
		url: contextPath + "admin/goods/info", 
		type: "post", 
		data: {
			itemCode: itemCode
		}, 
	}).done(function(data){
		itemName = data.ITEM_NAME;
		itemPrice = data.ITEM_PRICE;
		itemDur = data.ITEM_DUR;
		itemSale = data.ITEM_SALE;
		if(data.ITEM_COLOR != null){
			itemColor = data.ITEM_COLOR;
		}else{
			itemColor = "";
		}
		if(data.ITEM_SRC != null){
			itemSrc = data.ITEM_SRC;
		}else{
			itemSrc = "";
		}
	}).fail(ajaxErrorHandler);
	
	if(itemInfo){
		const { value: formValues } = await Swal.fire({
			title: "<div style='font-size: var(--font2);'>상품 수정</div>",
			confirmButtonText:"수정",
			confirmButtonTextFont:"Binggrae",
			confirmButtonColor:"black",
			width:"390px",
			html: `
			<div class="item-update flex">
				<div class="item-info flex">
					<div>상품코드</div>
					<div><input type="text" id="item-code" name="item-code" value="${itemCode}" readonly></div>
				</div>
				<div class="item-info flex">
					<div>상품명</div>
					<div><input type="text" id="item-name" name="item-name" value="${itemName}"></div>
				</div>
				<div class="item-info flex">
					<div>상품가격(원)</div>
					<div><input type="number" min="0" id="item-price" name="item-price" value="${itemPrice}"></div>
				</div>
				<div class="item-info flex">
					<div>상품기간(일)</div>
					<div><input type="number" min="0" id="item-dur" name="item-dur" value="${itemDur}"></div>
				</div>
				<div class="item-info flex">
					<div>상품할인율(%)</div>
					<div><input type="number" min="0" id="item-sale" name="item-sale" value="${itemSale}"></div>
				</div>
				<div class="item-info flex">
					<div>테마색상(헥사코드)</div>
					<div><input type="text" id="item-color" name="item-color" value="${itemColor}"></div>
				</div>
				<div class="item-info flex">
					<div>이미지경로</div>
					<div><input type="text" id="item-src" name="item-src" value="${itemSrc}"></div>
				</div>
			</div>
		  `,
		  focusConfirm: false,
		  preConfirm: () => {
			return [
				$("#item-code").val()
				, $("#item-name").val()
				, $("#item-price").val()
				, $("#item-dur").val()
				, $("#item-sale").val()
				, $("#item-color").val()
				, $("#item-src").val()
		    ];
		  }
		});
		if(formValues[0].trim().length > 0
				&& formValues[1].trim().length > 0
				&& formValues[2].trim().length > 0) {
			$.ajax({
				beforeSend : csrfHandler,
				url: contextPath + "admin/goods/update", 
				type: "post", 
				data: {
					itemCode: formValues[0]
					, itemName: formValues[1]
					, itemPrice: formValues[2] 
					, itemDur: formValues[3] 
					, itemSale: formValues[4]
					, itemColor: formValues[5]
					, itemSrc: formValues[6]
				}, 
				error: function(request, status, error){
					Swal.fire({
						text: "상품 수정 중 오류가 발생했습니다.", 
						icon: "warning", 
					});
				}, 
				success: function(data){
					if(data > 0){
						Swal.fire({
							text: "상품 수정이 완료되었습니다.", 
							icon: "success", 
						}).then((swal) => {
							if(swal.isConfirmed){
								location.reload();
							}
						});
					}else{
						Swal.fire({
							text: "알 수 없는 이유로 상품 수정이 완료되지 않았습니다.", 
							icon: "question", 
						});
					}
				}
			});
		}else{
			Swal.fire({
				text: "상품코드, 상품명, 상품가격은 필수 입력 사항입니다.", 
				icon: "error"
			});
		}
	}
}

// 상품 추가
async function itemInsertHandler(){
	const { value: formValues } = await Swal.fire({
		title: "<div style='font-size: var(--font2);'>상품 추가</div>",
		confirmButtonText:"추가",
		confirmButtonTextFont:"Binggrae",
		confirmButtonColor:"black",
		width:"390px",
		html: `
		<div class="item-insert flex">
			<div class="item-info flex">
				<div>상품코드</div>
				<div><input type="text" id="item-code" name="item-code"></div>
			</div>
			<div class="item-info flex">
				<div>상품명</div>
				<div><input type="text" id="item-name" name="item-name"></div>
			</div>
			<div class="item-info flex">
				<div>상품가격(원)</div>
				<div><input type="number" min="0" id="item-price" name="item-price"></div>
			</div>
			<div class="item-info flex">
				<div>상품기간(일)</div>
				<div><input type="number" min="0" id="item-dur" name="item-dur"></div>
			</div>
			<div class="item-info flex">
				<div>상품할인율(%)</div>
				<div><input type="number" min="0" id="item-sale" name="item-sale"></div>
			</div>
			<div class="item-info flex">
				<div>테마색상(헥사코드)</div>
				<div><input type="text" id="item-color" name="item-color"></div>
			</div>
			<div class="item-info flex">
				<div>이미지경로</div>
				<div><input type="text" id="item-src" name="item-src"></div>
			</div>
		</div>
	  `,
	  focusConfirm: false,
	  preConfirm: () => {
		return [
			$("#item-code").val()
			, $("#item-name").val()
			, $("#item-price").val()
			, $("#item-dur").val()
			, $("#item-sale").val()
			, $("#item-color").val()
			, $("#item-src").val()
	    ];
	  }
	});
	if(formValues[0].trim().length > 0
			&& formValues[1].trim().length > 0
			&& formValues[2].trim().length > 0) {
		$.ajax({
			beforeSend : csrfHandler,
			url: contextPath + "admin/goods/insert", 
			type: "post", 
			data: {
				itemCode: formValues[0], 
				itemName: formValues[1], 
				itemPrice: formValues[2], 
				itemDur: formValues[3], 
				itemSale: formValues[4], 
				itemSrc: formValues[5], 
			}, 
			success: function(data){
				if(data > 0){
					Swal.fire({
						text: "상품 추가가 완료되었습니다.", 
						icon: "success"
					}).then((swal) => {
						if(swal.isConfirmed){
							location.reload();
						}
					});
				}else{
					Swal.fire({
						text: "알 수 없는 이유로 상품 추가가 완료되지 않았습니다.", 
						icon: "question"
					});
				}
			}, 
			error: function(request, status, error){
				Swal.fire({
					text: "상품 추가 중 오류가 발생했습니다.", 
					icon: "warning"
				});
			}
		});
	}else{
		Swal.fire({
			text: "상품코드, 상품명, 상품가격은 필수 입력 사항입니다.", 
			icon: "error"
		});
	}
}

//상품삭제
function itemDeleteHandler(){
	var itemCode=$(this).parents('.col.list').data('item-code');
	console.log(itemCode);
	$.ajax({
		beforeSend : csrfHandler,
		url:contextPath+"admin/goods/delete",
		method:"post",
		data: {itemCode:itemCode},
		success : function(result) {
			 		location.reload();
				},
	 	error : function(request, status, error) {
		 	if(request.responseText.indexOf('ORA-02292')){
		 		Swal.fire({
					text: "장바구니 혹은 구매내역에 존재하는 상품은 삭제할 수 없습니다.\n운영팀에 문의해주시기 바랍니다.", 
					icon: "error", 
					confirmButtonColor: "#000000", 
					confirmButtonText: "확인"
				});
		 	}else{
		 		Swal.fire({
		 			text: "알 수 없는 오류가 발생했습니다.\n관리자에게 문의해주시기 바랍니다.", 
		 			confirmButtonColor: "#000000", 
		 			confirmButtonText: "확인"
		 		});
	 		}
		}
	});
}

var currentPage = 1;
var totalPageCount = null;
var startPageNum = null;
var endPageNum = null;

/* 페이징 이동 함수 */
function goPageHandler() {
			var currentpage = $(this).data("targetpage");
			$.ajax({
				beforeSend : csrfHandler,
				error : ajaxErrorHandler,
				url:contextPath+"admin/cancel/search"
				, method : "get"
				, data : {
						currentpage : currentpage,
						itemCode:itemCode}
				, dataType : "json"
				, success : function(result){
					if(result.seachMem){
						$("[name=search]").val(result.seachMem);
					}
					memListHandler(result);
					// 상품 추가 버튼
					$(".btn.insert").on("click", itemInsertHandler);
	
					// 상품 수정 버튼
					$(".btn.update").on("click", itemUpdateHandler);
	
					//상품삭제버튼
					$(".btn.delete").on("click", itemDeleteHandler);
				}
			});
	}

//상품검색
function searchBtnHandler(thisElement){
	var targetPage = $(thisElement).data('targetpage');
	var itemCode = $("[name=search]").val().trim();
	$.ajax({
		beforeSend : csrfHandler,
		error : ajaxErrorHandler,
		url:contextPath+"admin/goods/search",
		method:"post",
		context:this,
		data: {itemCode:itemCode, page: targetPage}
		}).done( function(goodsList) {
			 $('.wrap-list').replaceWith(goodsList);
			 // 상품 추가 버튼
			$(".btn.insert").on("click", itemInsertHandler);
	
			// 상품 수정 버튼
			$(".btn.update").on("click", itemUpdateHandler);
	
			//상품삭제버튼
			$(".btn.delete").on("click", itemDeleteHandler);
				});
}
