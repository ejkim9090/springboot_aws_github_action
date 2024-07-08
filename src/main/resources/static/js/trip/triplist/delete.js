//여행일정 삭제
function deleteHandler() {
	//data로 id와 title받아오기
	var planId =$(this).parents(".trip-list.wfull").data('plan-id');
	var planTitle = $(this).data('title');
	//data ajax로 넘기기
	Swal.fire({
		  title: "<h2>"+planTitle+"</h2>",
		  text: "삭제하시겠습니까?",
		  showCancelButton: true,
		  confirmButtonColor: "#000000",
		  cancelButtonColor: "#d33",
		  confirmButtonText: "삭제",
		  cancelButtonText: "취소",
	   	  confirmButtonTextFont:"Binggrae"
		}).then((result) => {
		  if (result.isConfirmed) {
	         $.ajax({
				beforeSend : csrfHandler,
				error : ajaxErrorHandler,
	        	 url:contextPath+"trip/list/delete",
	        	 method:"post",
	        	 data: {planId:planId},
				 //success: 1이면 업데이트 완료 0이면 실패
				 success : function(result) {
					if(result == 1){
							Swal.fire({
					     	title: "성공",
					      	text: "삭제되었습니다",
	  		     		    confirmButtonColor: "#000000",
			                animation:false		      	
					    }).then(() => {
							location.reload();
						});
					}else if(result == 0){
							Swal.fire({
					     	title: "실패",
					      	text: "삭제에 실패했습니다. 다시 한 번 확인해주세요",
			                confirmButtonText: 'Ok'		      	
					    })
					}
				 }
         	});//ajax
		  }//if
		});
}
