$(document).ready(function() {
    // CKEditor 초기화 - param div/textarea id
    let ckInstance1 = makeTripAntCkeditor("editor");
    // CKEditor 입력값을 넣어 줄 변수 지정 
	let editor1;
    ckInstance1.then( b => {editor1 = b; } );

	//폼 제출 시 처리
	$('#diaryForm .btn_submit').click(function(event) {
		//event.preventDefault(); //폼의 기본 제출 동작을 막음

		//필수 입력값 검사
		var diaryPlanId = $("select[name=diaryPlanId]").val();
		var diaryId = $("select[name=diaryId]").val();
		var diaryTitle = $("input[name=diaryTitle]").val().trim();
		var diaryDate = $("input[name=diaryDate]").val();
		var diaryTheme = $("select[name=diaryTheme]").val();
		var diaryOpen = $("input[name=diaryOpen]:checked").val();
		var diaryContent = editor1.getData(); // CKEditor 에서 내용 가져오기
		var diaryImage = "";  // image - 대표
		var diaryPreview = "";  // 300 자 이내
		
		if (diaryPlanId === "") {
			alert("일정을 선택해주세요.");
			return;
		}
		if (diaryTitle == "") {
			alert("제목을 입력해주세요.");
			return;
		}
		
		if (diaryContent.trim() == "") {
			alert("내용을 입력해주세요.");
			return;
		} else {
			var jImgElement = $(".ck.ck-editor__main").find("img");
			$(jImgElement).each(function(idx, thisElement){
				if(idx>0){ 
					return false;// each 더 이상 안돌게 return false 함
				}// img 태그 1개만 꺼내서 넣고 2번째 each 더 이상 안돌게 return true 함
				var imgSrc = $(thisElement).prop("src");
				diaryImage = imgSrc;
			});
			
			var diaryPreview = "";
			var diaryPreviewMaxByteSize = 0;
			$(".ck.ck-editor__main span, .ck.ck-editor__main p, .ck.ck-editor__main td, .ck.ck-editor__main h1").each(function(idx, thisElement) {
				var temp = $(thisElement).text();
				if(temp.length > 0){
					var tempByteSize = getUTF8ByteSize(temp);
					if(diaryPreviewMaxByteSize + tempByteSize >= 1400) {
						diaryPreview += cutStringToMaxBytes(temp , 1400 - diaryPreviewMaxByteSize);
						return false;  // each 더 이상 안돌게 return false 함
					} else {
						diaryPreviewMaxByteSize += tempByteSize;
						diaryPreview += temp; 
						diaryPreview += "\n"; 
					}
				}
			});
		}
		
		console.log(diaryImage);
		console.log(diaryPreview);
		$.ajax({
			beforeSend : csrfHandler,
			type: "post",
			url: contextPath +  "my/post",
			contentType: "application/json",
			data: JSON.stringify({
				diaryPlanId: diaryPlanId,
				diaryDiaryId: diaryId,
				diaryTitle: diaryTitle,
				diaryDate: diaryDate,
				diaryTheme: diaryTheme,
				diaryOpen: diaryOpen,
				diaryContent: diaryContent,
				diaryImage: diaryImage,
				diaryPreview: diaryPreview
			}),
			success: function(response) {
				//서버로 부터 응답을 받았을 때 처리 (예: 성공 메시지 출력 등)
				console.log("글 등록 성공:", response);
				alert("글이 성공적으로 등록되었습니다.");
				// 글 등록 성공시 이동
				location.href = contextPath + "diary"; 
			},
			error: function(xhr, status, error) {
				//오류 발생 시 처리
				console.error("글 등록 오류:", status, error);
				alert("글 등록 중 오류가 발생했습니다. 다시 시도해 주세요");
			}
		});
	});
});

// AL32UTF8 방식(UTF-8 인코딩) 에 따라 String bytes 크기 구하기
function cutStringToMaxBytes(input, maxBytes) {
	console.log(input);
	console.log(maxBytes);
	
    let byteCount = 0;
    let result = '';

    for (let i = 0; i < input.length; i++) {
        let char = input.charAt(i);
        let charCode = char.charCodeAt(0);

		if (charCode <= 0x007F) {
            // 1바이트 (ASCII 문자)
            byteCount += 1;
        } else if (charCode <= 0x07FF) {
            // 2바이트
            byteCount += 2;
        } else if (charCode <= 0xFFFF) {
            // 3바이트
            byteCount += 3;
        } else {
            // 4바이트 (서로게이트 쌍으로 처리)
            byteCount += 4;
            i++; // 서로게이트 쌍을 하나로 처리하므로 인덱스를 하나 더 증가
        }
        
        // 최대 바이트를 초과하면 중단
        if (byteCount > maxBytes) {
            break;
        }

        result += char;
    }
    console.log("cutStringToMaxBytes : result");
	console.log(result);
    return result;
}

// AL32UTF8 방식(UTF-8 인코딩) 에 따라 String bytes 크기 구하기
function getUTF8ByteSize(str) {
    let byteSize = 0;

    for (let i = 0; i < str.length; i++) {
        let charCode = str.charCodeAt(i);

        if (charCode <= 0x007F) {
            // 1바이트 (ASCII 문자)
            byteSize += 1;
        } else if (charCode <= 0x07FF) {
            // 2바이트
            byteSize += 2;
        } else if (charCode <= 0xFFFF) {
            // 3바이트
            byteSize += 3;
        } else {
            // 4바이트 (서로게이트 쌍으로 처리)
            byteSize += 4;
            i++; // 서로게이트 쌍을 하나로 처리하므로 인덱스를 하나 더 증가
        }
    }

    return byteSize;
}


