//Load the Visualization API and the piechart package.
google.load('visualization', '1.1', {
	'packages' : [ 'corechart', 'bar', 'sankey' ]
});
// Set a callback to run when the Google Visualization API is loaded.
google.setOnLoadCallback(drawAll);

function drawAll() {

	// other
	$.ajax({
		type : 'GET',
		url : "/proffstore/getAvarageProjectAmount",
		cache : false,
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(data) {
			proffstoreMainChart(data);
		}
	});
}

function proffstoreMainChart(stats) {
	var rows = [ [ 'Category', 'Average Budget' ] ];
	
	rows = rows.concat(stats);
	console.log(stats);
	var data = google.visualization.arrayToDataTable(rows);

	var options = {
		title : 'Average budget per category',
		width : '100%',
		height : 600,
		hAxis : {
			title : 'AVG Budget',
			minValue : 0,
		},
		vAxis : {
			title : 'Category'
		}
	};
	var chart = new google.visualization.BarChart(document
			.getElementById('avg-budget-per-category'));
	chart.draw(data, options);
}