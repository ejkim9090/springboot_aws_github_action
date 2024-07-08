$(loadedHandler);
function loadedHandler(){
	//로그인 유저의 role에 따른 공유 버튼 display 설정
	divisionRoleHandler();
	
	//여행일정 삭제
	$('.delete-plan').on("click",deleteHandler);
	
	//유저검색
	$('.btn.find').on("click",findNickHandler);
	
	//공유-모달창
	$('.share-plan.modal').on("click",shareModalHandler);
	$('.confimed').on("click",closeModalHandler)
	
	//케밥 아이콘 이벤트
	$('.info').on("click",miniModalBtnHandler);
}
