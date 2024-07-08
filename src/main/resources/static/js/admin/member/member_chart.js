
$(document).ready(function(){
	memebrChart();
})
function memebrChart(){
var ctx2 = document.getElementById('myChart2').getContext('2d');
var myChart2 = new Chart(ctx2, {
		  type: 'bubble',
		  data: {        
				labels: ["Red", "Blue", "Yellow", "Green", "Purple", "Orange"],        
				datasets: [{           
					label: '회원수',       
					 data: [{x:20,y:30,r:15},{x:30,y:10,r:10}],            
					backgroundColor: [                
						'rgba(255, 99, 132, 0.2)',                
						'rgba(54, 162, 235, 0.2)',                
						'rgba(255, 206, 86, 0.2)',                
						'rgba(75, 192, 192, 0.2)',                
						'rgba(153, 102, 255, 0.2)',               
						'rgba(255, 159, 64, 0.2)'            
						],            
							}]    
						}
		 ,
		  options: {
		    responsive: true,
		    plugins: {
		    	legend: {
		    	position: 'top',
		      },
		    title: {
		    	display: true,
		    	text: '월 가입자수/일정생성 통계'
		      }
		    }
		  },
		});
}