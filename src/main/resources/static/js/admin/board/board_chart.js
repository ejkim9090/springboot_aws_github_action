$(document).ready(function(){
	boardChart();
})
function boardChart(){
var ctx = document.getElementById('myChart').getContext('2d');
var myChart = new Chart(ctx, {
		  type: 'polarArea',
		  data: {        
				datasets: [{           
					label: 'polarArea',            
					 data:[100,25,50,80],            
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
		  backgroundColor: [                
				'rgba(255, 99, 132, 0.2)',                
				'rgba(54, 162, 235, 0.2)',                
				'rgba(255, 206, 86, 0.2)',                
				'rgba(75, 192, 192, 0.2)',                
				'rgba(153, 102, 255, 0.2)',               
				'rgba(255, 159, 64, 0.2)'            
				],     
		  options: {
		    responsive: false,
		    plugins: {
		      legend: {
		        position: 'top',
		      },
		      title: {
		        display: true,
		        text: '게시글 태그별 통계'
		      }
		    }
		  },
		});

 const DATA_COUNT = 5;
const NUMBER_CFG = {count: DATA_COUNT, min: 0, max: 100};

const labels = ['Red', 'Orange', 'Yellow', 'Green', 'Blue'];
const data = {
  labels: labels,
  datasets: [
    {
      label: 'Dataset 1',
      data: Utils.numbers(NUMBER_CFG),
      backgroundColor: [
        Utils.transparentize(Utils.CHART_COLORS.red, 0.5),
        Utils.transparentize(Utils.CHART_COLORS.orange, 0.5),
        Utils.transparentize(Utils.CHART_COLORS.yellow, 0.5),
        Utils.transparentize(Utils.CHART_COLORS.green, 0.5),
        Utils.transparentize(Utils.CHART_COLORS.blue, 0.5),
      ]
    }
  ]
};
}
