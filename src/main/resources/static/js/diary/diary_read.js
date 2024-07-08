$(loaededHandler);
function loaededHandler() {
	//공유-모달창
	$('.share').on("click", shareHandler);
	//신고하기
	$('.report').on("click", reportHandler);
	// 삭제하기
	$('.delete').on("click", deleteHandler);

	//케밥 아이콘 이벤트
	/*$('.menu-menu').on("click", miniModalBtnHandler);
	$(document).on('click', function(event) {
		// 클릭한 요소가 '.info' 클래스의 버튼 내부 요소나 '.mini-modal' 클래스가 아닌 경우에만 실행
		if (!$(event.target).closest('.menu-menu').length && !$(event.target).closest('.mini-modal').length) {
			$('.mini-modal').addClass('hide');
		}
	});*/
}

function reportHandler() {
	var diaryId = $(this).data('diary-id');
	
	console.log(diaryId);
	console.log("report ID: ", diaryId);

	Swal.fire({
		title: "이 글을 신고하시겠습니까?",
		text: "신고한 후 취소을 원하는 경우, 고객센터로 문의해주세요.",
		showCancelButton: true,
		confirmButtonColor: "#000000",
		cancelButtonColor: "#d33",
		confirmButtonText: "확인",
		cancelButtonText: "취소",
		animation: false
	}).then((result) => {
		if (result.isConfirmed) {
			$.ajax({
				beforeSend : csrfHandler,
				error : ajaxErrorHandler,
				url: contextPath + "my/diary/report/" + diaryId
				, method: "post"
				, success: function(result) {
					if (result == 1) {
						Swal.fire({
							title: "성공",
							text: "신고되었습니다",
							confirmButtonText: 'Ok'
						}).then(() => {
							location.href = "/diary";
						});
					} else  if(result == 1) {
						Swal.fire({
							title: "오류",
							text: "이미 신고한 글입니다.",
							confirmButtonText: 'Ok'
						});
					}else {
						Swal.fire({
							title: "오류",
							text: "오류가 발생하여 신고하지 못했습니다.",
							confirmButtonText: 'Ok'
						});
					}
				}
			});//ajax
		}//if
	});
}
// 삭제하기 

function deleteHandler() {
	// 현재 요소의 data 속석에서 'delete' 값을 가져옴
	var diaryId = $(this).data('diary-id');
	console.log(diaryId);
	console.log("delete ID: ", diaryId);

	//sweetAlert2를 사용하여 확인 다이얼 로그를 표시
	Swal.fire({
		title: "이 글을 삭제하시겠습니까?",
		text: "삭제하실 경우 되돌릴 수 없습니다.",
		showCancelButton: true,
		confirmButtonColor: "#000000",
		cancelButtonColor: "#d33",
		confirmButtonText: "확인",
		cancelButtonText: "취소",
		animation: false
	}).then((result) => {
		// '확인' 버튼이 클릭된 경우
		if (result.isConfirmed) {
			// ajax요청을 사용하여 서버에 삭제요청을 보냄
			$.ajax({
				beforeSend : csrfHandler,
				error : ajaxErrorHandler,
				url:contextPath +  "my/diary/delete/"+diaryId,
				method: "post",
				//success: 1이면 업데이트 완료 0이면 실패
				success: function(result) {
					if (result == 1) {
						Swal.fire({
							title: "성공",
							text: "삭제되었습니다",
							confirmButtonText: 'Ok'
						}).then(() => {
							location.href = "/diary";
						});
					} else {
						Swal.fire({
							title: "실패",
							text: "삭제에 실패하였습니다.",
							confirmButtonText: 'Ok'
						});
					}
				}
			});

		}
	});
}
// 공유하기
function shareHandler() {
    var shareId = $(this).data('share');
    console.log(shareId);
    // 공유하기 모달 표시
    Swal.fire({
        title: "나의 여행기 공유하기",
        html: `
            <p>해당 아이콘을 클릭하여 공유하세요</p>
            <br>
            <div class='share-links'>
                <a href='#' class='facebook-link'>
                    <img src='/images/icons/facebook.png' alt='Facebook' width='20px;'> 페이스북으로 공유하기
                </a>
                <a href='#' class='twitter-link'>
                    <img src='/images/icons/x.png' alt='X' width='18px;'> 공유하기
                </a>
            </div>
        `,
        showCancelButton: true,
        confirmButtonColor: "#000000",
        cancelButtonColor: "#d33",
        confirmButtonText: "확인",
        cancelButtonText: "취소",
        animation: false
    }).then((result) => {
        if (result.isConfirmed) {
            // 여행기 공유 처리를 수행하는 코드 추가
        }
    });

    // 페이스북으로 공유 링크 클릭 시
    $('.facebook-link').click(function(event) {
        event.preventDefault(); // 기본 동작 방지
        var shareUrl = "https://www.facebook.com/sharer/sharer.php?u=" + encodeURIComponent(window.location.href);
        window.open(shareUrl, '_blank');
    });

    // 트위터로 공유 링크 클릭 시
    $('.twitter-link').click(function(event) {
        event.preventDefault(); // 기본 동작 방지
        var shareUrl = "https://twitter.com/intent/tweet?url=" + encodeURIComponent(window.location.href);
        window.open(shareUrl, '_blank');
    });
}

/*좋아요 누르기  */
function btnLikeClickHandler(thisElement, diaryId) {
	clicknum = 0;
	console.log("btnLikeClickHandler 눌림");
	console.log(diaryId);
	/*	console.log(thisElement);*/
	if ($(thisElement).attr('src') === '/images/diary/diary_like_none.png') {
		// 현재 이미지가 '좋아요 없음' 이미지라면 '좋아요 있음' 이미지로 변경
		// ajax 요청
		$.ajax({
			beforeSend : csrfHandler,
			error : ajaxErrorHandler,
			url: contextPath + "my/diary/like/" + diaryId
			, method: "post"
		}).done(function(result) {
			if (result > 0)
				$(thisElement).attr('src', '/images/diary/diary_like_icon.png');
		})
	} else {
		// 현재 이미지가 '좋아요 있음' 이미지라면 '좋아요 없음' 이미지로 변경
		// ajax 요청
		$.ajax({
			beforeSend : csrfHandler,
			url: contextPath + "my/diary/unlike/" + diaryId, 
			method: "post", 
			error: ajaxErrorHandler
		}).done(function(result) {
			if (result > 0)
				$(thisElement).attr('src', '/images/diary/diary_like_none.png');
		})
	}
}
