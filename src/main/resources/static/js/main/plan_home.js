$(loadedHandler);
function loadedHandler() {
	$(".btn.find").on("click", btnFindClickHandler);

	$(".btn.make").on("click", btnMakeClickHandler);
	$("#selectbox").on("click", changeSelectAreaHandler);
}

//모달 열기-------------------------
function btnMakeClickHandler() {
	var auth = $(".auth").attr("value");

	if (!auth) {
		alert("로그인을 한 후에 이용해주세요.");
	} else {
		$(".modal").addClass("show");

		$(".keep_btn").attr("disabled", true);
		//일정 계속 만들기 활성화
		$("#planForm input").on('input', function() {
			if ($("#planForm input").val() == '')
				$(".keep_btn").prop("disabled", true);
			else
				$(".keep_btn").prop("disabled", false);
		});
		//일정 계속 만들기 
		$(".keep_btn").on("click", btnKeepClickHandler);
	}
}


// 외부영역 클릭 시 모달 닫기
$(document).mouseup(function(e) {
	var LayerModal = $(".modal");
	if (LayerModal.has(e.target).length === 0) {
		LayerModal.removeClass("show");
	}
});

// 지역 선택시 값 변경-------------------------
function changeSelectAreaHandler() {

	var area = $("#selectbox option:selected").attr('value');

	if (area.length < 3) {
		$.ajax({
			beforeSend : csrfHandler,
			error : ajaxErrorHandler,
			url: contextPath + "plan/area"
			, method: "post"
			, data: {
				areaCode: area
			}
			, dataType: 'json'
			, success: function(result) {
				if (result != null) {
					displayAreaInfo(result);
				}
			}
		});
	}
}

// 지역 선택시 값 넣기
function displayAreaInfo(data) {
	if (data != null) {
		var aName = data.areaEngName;
		$("#planForm h2").text(aName);
		var aExplain = data.areaExplain;
		$("#planForm h4").text(aExplain);
		$("#infoImg")[0].src = contextPath + "images/area/" + data.areaFileName;
	}
}

//일정 게속 만들기
function btnKeepClickHandler() {
	planForm.action = contextPath + "plan/keep";
	planForm.method = "post";
	planForm.submit();
}

//지역 검색-------------------------
function btnFindClickHandler() {
	var findArea = $("[name=find]").val().trim();

	if (findArea.length == 0) {
		alert("빈문자열만 입력할 수 없습니다. 검색할 지역명을 입력해주세요.");
		return;
	}

	$.ajax({
		beforeSend : csrfHandler,
		error : ajaxErrorHandler,
		url: contextPath+"find/area"
		, method: "post"
		, context: this
		, data: { findArea: findArea }
		//, dataType: 'json'
	}).done(function(wrap_area) {
		$(this).parents(".wrap-area").find(".wrap-areaList").replaceWith(wrap_area);
	});
}

function areaBoxBtnClickHandler(thisElement){
	var areaCode = $(thisElement).attr("value");
	console.log(areaCode);
	location.href = contextPath + "diary?areacode="+areaCode;

}