//인증코드 발송 및 입력창 표시
function codeSendHandler(){
	var memEmail = $("#memEmail").val();
	$.ajax({
		beforeSend : csrfHandler,
		error : ajaxErrorHandler,
		url: contextPath+'code/send/pwd', 
		type: "post", 
		async: false, 
		data: {memEmail: memEmail}, 
		success: function(result){
			if(result === "1"){
				Swal.fire({
					text: "인증번호가 발송되었습니다.", 
					icon: "success", 
					confirmButtonColor: "#000000", 
					confirmButtonText: "확인"
				}).then((swal) => {
					if(swal.isConfirmed){
						$(".inputbtn.check").removeClass('hide');
					}
				});
			}else if(result === "0"){
				Swal.fire({
					text: "인증번호 발송 중 오류가 발생했습니다.", 
					icon: "error", 
					confirmButtonColor: "#000000", 
					confirmButtonText: "확인"
				});
			}else if(result === "-1"){
				Swal.fire({
					text: "가입되지 않은 이메일입니다.", 
					icon: "info", 
					confirmButtonColor: "#000000", 
					confirmButtonText: "확인"
				});
			}
		}
	});
}
//인증코드 확인
function codeCheckHandler(){
	var inputCode = $("#code").val();
	$.ajax({
		beforeSend : csrfHandler,
		error : ajaxErrorHandler,
		url: contextPath+'code/check', 
		type: "post", 
		async: false, 
		data: {inputCode: inputCode}, 
		success: function(result){
			if(result == 1){
				Swal.fire({
					text: "이메일 인증에 성공하였습니다.", 
					icon: "success", 
					confirmButtonColor: "#000000", 
					confirmButtonText: "확인"
				}).then((swal) => {
					if(swal.isConfirmed){
						$(".inputbtn.check").addClass('hide');
						$(".btn.sendCode").addClass('hide');
						$("#memEmail").attr("readonly", true);
						$(".flex.pwd.find").addClass('hide');
						$(".flex.pwd.set").removeClass('hide');
						setActive();
					}
				});
			}else{
				Swal.fire({
					text: "인증번호가 일치하지 않습니다.", 
					icon: "warning", 
					confirmButtonColor: "#000000", 
					confirmButtonText: "확인"
				});
			}
		}
	});
}