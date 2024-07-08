//드래그 앤 드랍
let after_i;
let after_j;
function dragAndDrop(){
/**
 * [x] 엘리먼트의 .draggable, .container의 배열로 선택자를 지정합니다.
 * [x] draggables를 전체를 루프하면서 dragstart, dragend를 이벤트를 발생시킵니다.
 * [x] dragstart, dragend 이벤트를 발생할때 .dragging라는 클래스를 토글시킨다.
 * [x] dragover 이벤트가 발생하는 동안 마우스 드래그하고 마지막 위치해놓은 Element를 리턴하는 함수를 만듭니다.
 */
 
 	//$자체에 함수? 선언 $() 안의 선택자를 가진 모든 요소들을 선택함
    //const $ = (select) => document.querySelectorAll(select);
    const draggables = document.querySelectorAll('.edit-tourlist .draggable');
    const containers = document.querySelectorAll('.edit-tourlist .container');
    
    draggables.forEach(el => {
        el.addEventListener('dragstart', (e) => {
			console.log("dragstart");
            //e.target.classList.add('dragging');
            el.classList.add('dragging');
        });

        el.addEventListener('dragend', (e)=>  {
			console.log("dragend");
			console.log(e.target);
			//var el = e.target;
		    el.classList.remove('dragging')
			console.log("************el");
			console.log(el);
		
		    console.log("--------- prev");
		    console.log($(el).prev().get(0));
			
			var el_i =$(el).data("i");
			var el_j =$(el).data("j");
			
			// 같은 컬럼
			// 아래 -> 위 (맨앞)
			// 위-> 아래 (맨뒤)
			
			// 다른 컬럼
			// (맨앞)
			// (맨뒤)
			            
			var prev_i =$(el).prev().data("i");
			var prev_j =$(el).prev().data("j");
			
			console.log("======");
			console.log("el_i: "+ el_i);
			console.log("el_j: "+ el_j);
			console.log("prev_i: "+ prev_i);
			console.log("prev_j: "+ prev_j);
			console.log("after_i: "+ after_i);
			console.log("after_j: "+ after_j);
			console.log("======");
						
						
			var temp_i = ((prev_i) ? prev_i : after_i); 
			//drop 위치가 같은 column인가
			if( temp_i== el_i){
				var details = detailListEditMode[el_i];
				var daylength = details.dayDetailInfoEntity.length;  
				// 같은 컬럼
				var temp = JSON.parse(JSON.stringify( detailListEditMode[el_i].dayDetailInfoEntity[el_j]));
				
				if(prev_j >= 0){
					//index 크기 비교
					if(el_j > prev_j){
						for(var j=el_j-1; j > prev_j; j-- ){
							details.dayDetailInfoEntity[j+1] =  details.dayDetailInfoEntity[j];
						}
					}else{
						for(var j=el_j; j < prev_j; j++ ){
							details.dayDetailInfoEntity[j] = details.dayDetailInfoEntity[j+1];
						}
					}
					// 옮겨진 객체 el --> prev 다음j 위치에 대입 
					detailListEditMode[el_i].dayDetailInfoEntity[prev_j+1] = temp;
				} else {
					//drop 위치가 0번째인가 
					// 맨앞
					//index 크기 비교 불필요
					for(var j=el_j-1; j >= 0; j-- ){
						details.dayDetailInfoEntity[j+1] =  details.dayDetailInfoEntity[j];
					}
					detailListEditMode[el_i].dayDetailInfoEntity[0] = temp;
				}
			}else{
				//컬럼이 다를 때
				if(prev_j >= 0){
					var details = detailListEditMode[prev_i];
					var daylength = details.dayDetailInfoEntity.length
					// prev 다음j 모든 객체를 +1 위치로 대입
					for(var j=daylength-1; j > prev_j; j-- ){
						details.dayDetailInfoEntity[j+1] =  details.dayDetailInfoEntity[j];
					}
		
					// 옮겨진 객체 el --> prev 다음j 위치에 대입 
					detailListEditMode[temp_i].dayDetailInfoEntity[prev_j+1] = 
						detailListEditMode[el_i].dayDetailInfoEntity[el_j];
		
					// 옮겨진 객체 el 다음 모든 객체를 -1 위치로 대입 
					var details = detailListEditMode[el_i];
					var daylength = details.dayDetailInfoEntity.length
					for(var j=el_j+1; j < daylength; j++ ){
						details.dayDetailInfoEntity[j-1] =  details.dayDetailInfoEntity[j];
					}
					details.dayDetailInfoEntity.pop();
				} else {
					var details = detailListEditMode[temp_i];
					var daylength = details.dayDetailInfoEntity.length
					//drop 위치가 0번째인가 
					// 맨앞
					for(var j=daylength-1; j >= 0; j-- ){
						details.dayDetailInfoEntity[j+1] =  details.dayDetailInfoEntity[j];
					}
					// 옮겨진 객체 el --> prev 다음j 위치에 대입 
					detailListEditMode[temp_i].dayDetailInfoEntity[0] = 
						detailListEditMode[el_i].dayDetailInfoEntity[el_j];
					// 옮겨진 객체 el 다음 모든 객체를 -1 위치로 대입 
					var details = detailListEditMode[el_i];
					var daylength = details.dayDetailInfoEntity.length
					for(var j=el_j+1; j < daylength; j++ ){
						details.dayDetailInfoEntity[j-1] =  details.dayDetailInfoEntity[j];
					}
					details.dayDetailInfoEntity.pop();
				}
			}
			console.log("detailListEditMode===2");
			console.log(detailListEditMode);
			// *** 편집된 내용 다시 display
			displayEditModeAfterDragEnd();
			//일차별 동그라미 색 변경
			circleColorHandler();
			//maker display - TODO
			displayMarker();
			//드래그 앤 드랍
			dragAndDrop();
		});
    });

    containers.forEach(container => {
        container.addEventListener('dragover', e => {
            console.log("dragover");
            e.preventDefault()
            const afterElement = getDragAfterElement(container, e.clientY);
            const draggable = document.querySelector('.dragging')
            // container.appendChild(draggable)
			console.log(draggable);
			console.log(afterElement);
			console.log("========");
			after_i = $(afterElement).data("i");
			after_j = $(afterElement).data("j");
            container.insertBefore(draggable, afterElement)
        })
    });
    
    function getDragAfterElement(container, y) {
        const draggableElements = [...container.querySelectorAll('.draggable:not(.dragging)')]
        

        return draggableElements.reduce((closest, child) => {
          const box = child.getBoundingClientRect() //해당 엘리먼트에 top값, height값 담겨져 있는 메소드를 호출해 box변수에 할당
          const offset = y - box.top - box.height / 2 //수직 좌표 - top값 - height값 / 2의 연산을 통해서 offset변수에 할당
          if (offset < 0 && offset > closest.offset) { // (예외 처리) 0 이하 와, 음의 무한대 사이에 조건
            return { offset: offset, element: child } // Element를 리턴
          } else {
            return closest
          }

        }, { offset: Number.NEGATIVE_INFINITY }).element
    };
    
}// dragAndDrop()


function dragendHandler(e)  {
	console.log("dragend");
	console.log(e.target);
	var el = e.target;
    el.classList.remove('dragging')
	console.log("************el");
	console.log(el);

    console.log("--------- prev");
    console.log($(el).prev().get(0));
	
	var el_i =$(el).data("i");
	var el_j =$(el).data("j");
	
	// 같은 컬럼
	// 아래 -> 위 (맨앞)
	// 위-> 아래 (맨뒤)
	
	// 다른 컬럼
	// (맨앞)
	// (맨뒤)
	            
	var prev_i =$(el).prev().data("i");
	var prev_j =$(el).prev().data("j");
	
	console.log("======");
	console.log("el_i: "+ el_i);
	console.log("el_j: "+ el_j);
	console.log("prev_i: "+ prev_i);
	console.log("prev_j: "+ prev_j);
	console.log("after_i: "+ after_i);
	console.log("after_j: "+ after_j);
	console.log("======");
				
	var details = detailListEditMode[el_i];
	var daylength = details.dayDetailInfoEntity.length;
				
	var temp_i = ((prev_i) ? prev_i : after_i); 
	//drop 위치가 같은 column인가
	if( temp_i== el_i){  
		// 같은 컬럼
		var temp = JSON.parse(JSON.stringify( detailListEditMode[el_i].dayDetailInfoEntity[el_j]));
		
		if(prev_j){
			//index 크기 비교
			if(el_j > prev_j){
				for(var j=el_j-1; j > prev_j; j-- ){
					details.dayDetailInfoEntity[j+1] =  details.dayDetailInfoEntity[j];
				}
			}else{
				for(var j=el_j; j < prev_j; j++ ){
					details.dayDetailInfoEntity[j] = details.dayDetailInfoEntity[j+1];
				}
			}
			// 옮겨진 객체 el --> prev 다음j 위치에 대입 
			detailListEditMode[el_i].dayDetailInfoEntity[prev_j+1] = temp;
		} else {
			//drop 위치가 0번째인가 
			// 맨앞
			//index 크기 비교 불필요
			for(var j=el_j-1; j >= 0; j-- ){
				details.dayDetailInfoEntity[j+1] =  details.dayDetailInfoEntity[j];
			}
			detailListEditMode[el_i].dayDetailInfoEntity[0] = temp;
		}
	}else{
		//컬럼이 다를 때
		if(prev_j){
			// prev 다음j 모든 객체를 +1 위치로 대입
			for(var j=daylength-1; j > prev_j; j-- ){
				details.dayDetailInfoEntity[j+1] =  details.dayDetailInfoEntity[j];
			}

			// 옮겨진 객체 el --> prev 다음j 위치에 대입 
			detailListEditMode[temp_i].dayDetailInfoEntity[prev_j+1] = 
				detailListEditMode[el_i].dayDetailInfoEntity[el_j];

			// 옮겨진 객체 el 다음 모든 객체를 -1 위치로 대입 
			details = detailListEditMode[el_i];
			var daylength = details.dayDetailInfoEntity.length
			for(var j=el_j+1; j < daylength; j++ ){
				details.dayDetailInfoEntity[j-1] =  details.dayDetailInfoEntity[j];
			}
			details.dayDetailInfoEntity.pop();
		} else {
			//drop 위치가 0번째인가 
			// 맨앞
			for(var j=daylength-1; j >= 0; j-- ){
				details.dayDetailInfoEntity[j+1] =  details.dayDetailInfoEntity[j];
			}
			// 옮겨진 객체 el --> prev 다음j 위치에 대입 
			detailListEditMode[temp_i].dayDetailInfoEntity[0] = 
				detailListEditMode[el_i].dayDetailInfoEntity[el_j];
			// 옮겨진 객체 el 다음 모든 객체를 -1 위치로 대입 
			details = detailListEditMode[el_i];
			var daylength = details.dayDetailInfoEntity.length
			for(var j=el_j+1; j < daylength; j++ ){
				details.dayDetailInfoEntity[j-1] =  details.dayDetailInfoEntity[j];
			}
			details.dayDetailInfoEntity.pop();
		}
	}
	console.log("detailListEditMode===2");
	console.log(detailListEditMode);
	//편집된 내용 다시 display
	displayEditModeAfterDragEnd();
	//일차별 동그라미 색 변경
	circleColorHandler();
	//maker display - TODO
	displayMarker();
	//드래그 앤 드랍
	dragAndDrop();
}// dragendHandler()
