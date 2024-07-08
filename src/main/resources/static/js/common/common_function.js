//ajax error 부분
function ajaxErrorHandler(request, status, error) {
	alert("code: " + request.status + "\n" + "message: "
		+ request.responseText + "\n"
		+ "error: " + error);
	Swal.fire({
		text: "알 수 없는 오류가 발생했습니다. 관리자에게 문의해주시기 바랍니다.",
		icon: "error",
		confirmButtonColor: "#000000",
		confirmButtonText: "확인"
	});
}
const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content");
//csrf
function csrfHandler(xhr) {
	xhr.setRequestHeader(header, token);
}

function areacode2tabnum(areacode){
	var tabnum = 0;
	switch(areacode){
		case "1":			tabnum = 2;			break; // 서울
		case "2":			tabnum = 5;			break; // 인전
		case "3":			tabnum = 7;			break; //대전
		case "4":			tabnum = 4;			break;  //대구
		case "5":			tabnum = 6;			break; //광주
		case "6":			tabnum = 3;			break; //부산
		case "7":			tabnum = 8;			break;     // 울산
		case "8":			tabnum = 9;			break;     // 세종  
		case "31":			tabnum = 10;			break; // 경기  
		case "32":			tabnum = 11;			break; // 강원  
		case "33":			tabnum = 12;			break; // 충북  
		case "34":			tabnum = 13;			break; // 충남  
		case "35":			tabnum = 16;			break; // 경북  
		case "36":			tabnum = 17;			break; // 경남  
		case "37":			tabnum = 14;			break; // 전북  
		case "38":			tabnum = 15;			break; // 전남  
		case "39":			tabnum = 18;			break; // 제주  
		default:			tabnum = 1;			break; // 전체
	}
	
	return tabnum - 1;
}