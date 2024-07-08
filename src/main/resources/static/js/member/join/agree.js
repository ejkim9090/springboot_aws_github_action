//전체 동의 클릭
function agreeAllHandler(){
	if($("#agree-all").is(":checked")){
		$("input[name=agree]").prop("checked", true);
		joinActive();
	}else{
		$("input[name=agree]").prop("checked", false);
		joinActive();
	}
}
//개별 동의 클릭
function agreeDivHandler(){
	var total = $("input[name=agree]").length;
	var checked = $("input[name=agree]:checked").length;
	if(total != checked){
		$("#agree-all").prop("checked", false);
		joinActive();
	}else{
		$("#agree-all").prop("checked", true); 
		joinActive();
	}
}