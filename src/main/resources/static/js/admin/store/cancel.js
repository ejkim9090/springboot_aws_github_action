$(loaededHandler);
function loaededHandler(){
	//찾기(검색) 버튼
	//$('.btn-search').on("click",searchHandler);
	// 결제 취소 버튼
	$(".btn.cancel").on("click", payCancelHandler);
}

// 결제 취소
function payCancelHandler(){
	const buyId = $(this).parents(".col.list").data("id").toString();
	const memEmail = $(this).parents(".col.list").data("email");
	Swal.fire({
		title: "해당 주문번호의 모든 건이 취소됩니다.\n진행하시겠습니까?", 
		icon: "warning", 
		showCancelButton: true, 
		confirmButtonText: "결제취소", 
		confirmButtonColor: "#000000", 
		cancelButtonText: "돌아가기", 
		cancelButtonColor: "#ff0000"
	}).then((swal) => {
		if(swal.isConfirmed){
			$.ajax({
				beforeSend : csrfHandler,
				error : ajaxErrorHandler,
				url: contextPath + "admin/cancel", 
				type: "post", 
				data: {
					buyId: buyId, 
					memEmail: memEmail 
				}, 
				success: function(data){
					if(data > 0){
						Swal.fire({
							text: "결제 취소가 완료되었습니다.", 
							icon: "success", 
							confirmButtonColor: "#000000", 
							confirmButtonText: "확인"
						}).then((swal) => {
							if(swal.isConfirmed){
								location.reload();
							}
						});
					}else{
						Swal.fire({
							text: "결제 취소 중 오류가 발생했습니다.", 
							icon: "error", 
							confirmButtonColor: "#000000", 
							confirmButtonText: "확인"
						});
					}
				}
			});
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
				url:contextPath+"admin/member/search"
				, method : "get"
				, data : {
						seachMem : seachMem,
						currentpage : currentpage}
				, dataType : "json"
				, success : function(result){
					if(result.seachMem){
						$("[name=search]").val(result.seachMem);
					}
					memListHandler(result);
					$(".btn.cancel").on("click", payCancelHandler);
				}
			});
	}
	
//회원검색
function searchBtnHandler(thisElement){
	var targetPage = $(thisElement).data('targetpage');
	var searchMem = $("[name=search]").val().trim();
	$.ajax({
		beforeSend : csrfHandler,
		error : ajaxErrorHandler,
		url:contextPath+"admin/cancel/search",
		method:"post",
		data: {searchMem:searchMem
		 , page: targetPage},
		success : function(searchList) {
			 $('.wrap-list').replaceWith(searchList);
			 $(".btn.cancel").on("click", payCancelHandler);
			}
	});
}

function memListHandler(searchList){
	var htmlVal = '';
	for (var idx in searchList){
		var map = searchList[idx];
		htmlVal+=`
				<ul class="col list" th:each="map : ${list}">
							<li>${map.buyId}</li>
							<li>${map.memNick}</li>
							<li>${map.memEmail}</li>
							<li>${map.itemName}</li>
							<li>${map.buyDate}</li>
							<li>
								<button type="button" class="btn cancel">결제취소</button>
								<input type="hidden" value="${map.itemCode}">
							</li>
						</ul>
			`;
	}
	return htmlVal;
} 