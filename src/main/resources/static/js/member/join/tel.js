// 휴대폰 번호 입력
function telCheckHandler() {
	// 숫자만 남기고 모든 문자 제거
    var telNum = $(this).val().replace(/[^\d]/g, "");

    // 전화번호 형식에 맞게 하이푼 추가
    if(telNum.length >= 4 && telNum.length <= 7) {
        $(this).val(telNum.slice(0, 3) + "-" + telNum.slice(3));
    }else if(telNum.length >= 8 && telNum.length <= 10) {
        $(this).val(telNum.slice(0, 3) + "-" + telNum.slice(3, 6) + "-" + telNum.slice(6));
    }else if(telNum.length >= 11){
        $(this).val(telNum.slice(0, 3) + "-" + telNum.slice(3, 7) + "-" + telNum.slice(7));
    }
}