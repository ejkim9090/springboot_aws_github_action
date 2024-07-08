$(loaededHandler);
function loaededHandler(){
	//$('.btn-search').on("click",searchBtnHandler);
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
						searchMem : searchMem,
						currentpage : currentpage}
				, dataType : "json"
				, success : function(result){
					if(result.searchMem){
						$("[name=search]").val(result.searchMem);
					}
					memListHandler(result);
				}
			});
	}

/*검색+페이징1*/
function searchBtnHandler(thisElement){
	var targetPage = $(thisElement).data('targetpage');
	var searchMem = $("[name=search]").val().trim();
	$.ajax({
		beforeSend : csrfHandler,
		error : ajaxErrorHandler,
		url:contextPath+"admin/member/search",
		 method:"post",
		 data: {
			searchMem:searchMem
		 , page: targetPage
		 },
		 success : function(searchList) {
			$('.wrap-list').replaceWith(searchList);
		}
	});
}

function memListHandler(searchList){
	var htmlVal = '';
	for (var idx in searchList){
		var memList = searchList[idx];
		htmlVal+=`
			<ul class="col list">
				<li>${memList.memNick}</li>
				<li>${memList.memEmail}</li>
				<li>${memList.memJoinDate}</li>
				<li>${memList.memQuitDate}</li>
				<li>${memList.memRole}</li>
				<li><button type="button" class="btn">편집</button></li>
			</ul>
			`;
	}
	return htmlVal;
} 

