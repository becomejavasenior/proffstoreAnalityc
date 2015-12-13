//Load the Visualization API and the piechart package.
google.load('visualization', '1.1', {
	'packages' : [ 'corechart', 'bar', 'sankey' ]
});
// Set a callback to run when the Google Visualization API is loaded.
google.setOnLoadCallback(drawAll);

function drawAll() {
	$(".loading").show();
	// other
	$.ajax({
		type : 'GET',
		url : "/proffstore/getAvarageProjectAmount",
		cache : false,
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(data) {
			proffstoreMainChart(data);

			$.ajax({
				type : 'GET',
				url : "/proffstore/getSkillsPopularity",
				cache : false,
				contentType : "application/json; charset=utf-8",
				dataType : "json",
				success : function(data) {
					skillsPopularity(data);
					$(".loading").hide();
				}
			});

		}
	});

}

function proffstoreMainChart(stats) {
	var rows = [ [ 'Category', 'Average Budget' ] ];

	var i, len, maxStat = 0;
	for (i = 0, len = stats.length; i < len; i++) {
		if (stats[i][1] > maxStat) {
			maxStat = stats[i][1];
		}
	}
	var ticksArray = [];
	for (i = 0, len = maxStat / 1000; i < len; i++) {
		ticksArray.push(1000 * (i + 1));
	}

	rows = rows.concat(stats);
	console.log(stats);
	var data = google.visualization.arrayToDataTable(rows);

	var options = {
		title : 'Average budget per category',
		width : '100%',
		height : 600,
		hAxis : {
			title : 'AVG Budget($)',
			minValue : 0,
			ticks : ticksArray
		},
		vAxis : {
			title : 'Category'
		},
		chartArea : {
			left : '40%',
			width : '60%'
		}
	};
	var chart = new google.visualization.BarChart(document
			.getElementById('avg-budget-per-category'));
	chart.draw(data, options);
}

function skillsPopularity(stats) {
	var rows = [ [ 'Skills', 'Popularity' ] ];

	rows = rows.concat(stats);

	var data = google.visualization.arrayToDataTable(rows);

	var options = {
		title : 'Skills popularity',
		width : '100%',
		height : 600,
		sliceVisibilityThreshold : .01,
		pieResidueSliceLabel : "Other",
		//pieHole : 0.4
	};
	var chart = new google.visualization.PieChart(document
			.getElementById('skills-popularity'));
	chart.draw(data, options);
}