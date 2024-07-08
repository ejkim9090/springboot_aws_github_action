//마이페이지 돌아가기
function backClickHandler(){
	location.href = contextPath+'my/home';
}
//회원 탈퇴
function quitClickHandler(){
	const memPassword = $('#memPassword').val();
	Swal.fire({
		text: "정말 탈퇴하시겠습니까?", 
		icon: "question", 
		showCancelButton: true, 
		confirmButtonText: "탈퇴하기", 
		confirmButtonColor: "#000000", 
		cancelButtonText: "돌아가기", 
		cancelButtonColor: "#ff0000"
	}).then((swal) => {
		if(swal.isConfirmed){
			$.ajax({
				beforeSend : csrfHandler,
				error : ajaxErrorHandler,
				url: contextPath+'my/quit', 
				type: 'post', 
				data: {memPassword: memPassword}, 
				success: function(result){
					if(result > 0){
						Swal.fire({
							text: "탈퇴 처리되었습니다.\n메인 페이지로 이동합니다.", 
							icon: "success", 
							confirmButtonColor: "#000000", 
							confirmButtonText: "확인"
						}).then((swal) => {
							if(swal.isConfirmed){
								let form = document.getElementById('frm-quit');
								form.action = contextPath+'logout';
								form.method = 'POST';
								form.submit();
							}
						});
					}else{
						Swal.fire({
							text: "회원 탈퇴 중 오류가 발생했습니다.\n관리자에게 문의해주시기 바랍니다.", 
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
