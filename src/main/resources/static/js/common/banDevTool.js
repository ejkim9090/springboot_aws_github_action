!function() {
	  function detectDevTool(allow) {
	    if(isNaN(+allow)) allow = 100;
	    var start = +new Date(); // Validation of built-in Object tamper prevention.
	    debugger;
	    var end = +new Date(); // Validates too.
	    if(isNaN(start) || isNaN(end) || end - start > allow) {
			Swal.fire({
					text: "개발자 도구가 감지되었습니다.\n메인 페이지로 이동합니다.", 
					icon: "warning",
					showConfirmButton: false, 
					timer: 2000,
  					timerProgressBar: true
				}).then(() => {
			    	location.href = '/';
				});
	    }
	  }
	  if(window.attachEvent) {
	    if (document.readyState === "complete" || document.readyState === "interactive") {
	        detectDevTool();
	      window.attachEvent('onresize', detectDevTool);
	      window.attachEvent('onmousemove', detectDevTool);
	      window.attachEvent('onfocus', detectDevTool);
	      window.attachEvent('onblur', detectDevTool);
	    } else {
	        setTimeout(argument.callee, 0);
	    }
	  } else {
	    window.addEventListener('load', detectDevTool);
	    window.addEventListener('resize', detectDevTool);
	    window.addEventListener('mousemove', detectDevTool);
	    window.addEventListener('focus', detectDevTool);
	    window.addEventListener('blur', detectDevTool);
	  }
}();