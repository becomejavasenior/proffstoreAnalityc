//Load the Visualization API and the piechart package.
google.load('visualization', '1.1', {
	'packages' : [ 'corechart', 'bar', 'sankey' ]
});
// Set a callback to run when the Google Visualization API is loaded.
google.setOnLoadCallback(drawAll);

var SKILLS_PROFFSTORE = null;
var SKILLS_ELANCE = null;
var SKILLS_FREELANCER = null;

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
					SKILLS_PROFFSTORE = data;

					$
							.ajax({
								type : 'GET',
								url : "/static-elance",
								cache : false,
								contentType : "application/json; charset=utf-8",
								dataType : "json",
								success : function(data) {
									proffstoreMainChartElance(data);

									$
											.ajax({
												type : 'GET',
												url : "/static-elance-skills",
												cache : false,
												contentType : "application/json; charset=utf-8",
												dataType : "json",
												success : function(data) {
													skillsPopularityElance(data);
													SKILLS_ELANCE = data;
													$
															.ajax({
																type : 'GET',
																url : "/freelancer/getAvarageProjectAmount",
																cache : false,
																contentType : "application/json; charset=utf-8",
																dataType : "json",
																success : function(data) {
																	proffstoreMainChartFreelancer(data);

																	$
																			.ajax({
																				type : 'GET',
																				url : "/freelancer/getSkillsPopularity",
																				cache : false,
																				contentType : "application/json; charset=utf-8",
																				dataType : "json",
																				success : function(
																						data) {
																					skillsPopularityFreelancer(data);
																					SKILLS_FREELANCER = data;

																					skillsCompare();
																					$(".loading").hide();
																				}
																			});

																}
															});

												}
											});

								}
							});
					
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
		sliceVisibilityThreshold : .05,
		pieResidueSliceLabel : "Other",
	// pieHole : 0.4
	};
	var chart = new google.visualization.PieChart(document
			.getElementById('skills-popularity'));
	chart.draw(data, options);
}

function proffstoreMainChartElance(stats) {
	var rows = [ [ 'Category', 'Average Budget' ] ];

	var i, len, maxStat = 0;
	for (i = 0, len = stats.length; i < len; i++) {
		if (stats[i][1] > maxStat) {
			maxStat = stats[i][1];
		}
	}
	var ticksArray = [];
	for (i = 0, len = maxStat / 500; i < len; i++) {
		ticksArray.push(500 * (i + 1));
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
			.getElementById('avg-budget-per-category-elance'));
	chart.draw(data, options);
}

function skillsPopularityElance(stats) {
	var rows = [ [ 'Skills', 'Popularity' ] ];

	rows = rows.concat(stats);

	var data = google.visualization.arrayToDataTable(rows);

	var options = {
		title : 'Skills popularity',
		width : '100%',
		height : 600,
		sliceVisibilityThreshold : .05,
		pieResidueSliceLabel : "Other",
	// pieHole : 0.4
	};
	var chart = new google.visualization.PieChart(document
			.getElementById('skills-popularity-elance'));
	chart.draw(data, options);
}

function proffstoreMainChartFreelancer(stats) {
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
			.getElementById('avg-budget-per-category-freelancer'));
	chart.draw(data, options);
}

function skillsPopularityFreelancer(stats) {
	var rows = [ [ 'Skills', 'Popularity' ] ];

	rows = rows.concat(stats);

	var data = google.visualization.arrayToDataTable(rows);

	var options = {
		title : 'Skills popularity',
		width : '100%',
		height : 600,
		sliceVisibilityThreshold : .05,
		pieResidueSliceLabel : "Other",
	// pieHole : 0.4
	};
	var chart = new google.visualization.PieChart(document
			.getElementById('skills-popularity-freelancer'));
	chart.draw(data, options);
}

function ArrNoDupe(a) {
	var temp = {};
	for (var i = 0; i < a.length; i++)
		temp[a[i]] = true;
	var r = [];
	for ( var k in temp)
		r.push(k);
	return r;
}

var indexOf = function(needle) {
    if(typeof Array.prototype.indexOf === 'function') {
        indexOf = Array.prototype.indexOf;
    } else {
        indexOf = function(needle) {
            var i = -1, index = -1;

            for(i = 0; i < this.length; i++) {
                if(this[i] === needle) {
                    index = i;
                    break;
                }
            }

            return index;
        };
    }

    return indexOf.call(this, needle);
};

function intersect_safe(a, b)
{
  var ai=0, bi=0;
  var result = new Array();

  while( ai < a.length && bi < b.length )
  {
     if      (a[ai] < b[bi] ){ ai++; }
     else if (a[ai] > b[bi] ){ bi++; }
     else /* they're equal */
     {
       result.push(a[ai]);
       ai++;
       bi++;
     }
  }

  return result;
}

function intersection_destructive(a, b)
{
  var result = new Array();
  while( a.length > 0 && b.length > 0 )
  {  
     if      (a[0] < b[0] ){ a.shift(); }
     else if (a[0] > b[0] ){ b.shift(); }
     else /* they're equal */
     {
       result.push(a.shift());
       b.shift();
     }
  }

  return result;
}
function skillsCompare() {

	var temp1 = [], temp2 = [], temp3 = [];
	var i, j, len, allSkills = [ 'Skills' ];
	for (i = 0, len = SKILLS_PROFFSTORE.length; i < len; i++) {
		temp1.push(SKILLS_PROFFSTORE[i][0].toLowerCase());
	}

	for (i = 0, len = SKILLS_ELANCE.length; i < len; i++) {
		temp2.push(SKILLS_ELANCE[i][0].toLowerCase());
	}

	for (i = 0, len = SKILLS_FREELANCER.length; i < len; i++) {
		temp3.push(SKILLS_FREELANCER[i][0].toLowerCase());
	}
	var temp4 = intersection_destructive(temp1, temp2);
	//allSkills = allSkills.concat(intersection_destructive(temp3, temp4));
	allSkills.push("php");
	allSkills.push("java");
	allSkills.push("html");

	// allSkills = ArrNoDupe(allSkills);
	allSkills.push({ role: 'annotation' });
	var rows = [];
	rows.push(allSkills);
	var proffstoreValues = [ 'Proffstore' ];
	var elanceValues = [ 'Elance' ];
	var freelancerValues = [ 'Freelancer' ];

	for(i = 1, len = allSkills.length-1; i < len; i++) {
		for(j = 0; j < SKILLS_PROFFSTORE.length;j++) {
			if(SKILLS_PROFFSTORE[j][0].toLowerCase() === allSkills[i]) {
				proffstoreValues[i] = SKILLS_PROFFSTORE[j][1];
				break;
			}
		}
		if(!proffstoreValues[i]) {
			proffstoreValues[i] = 1;
		}
	}
	proffstoreValues.push('');
	

	for(i = 1, len = allSkills.length-1; i < len; i++) {
		for(j = 0; j < SKILLS_ELANCE.length; j++) {
			if(SKILLS_ELANCE[j][0].toLowerCase() === allSkills[i]) {
				elanceValues[i] = SKILLS_ELANCE[j][1];
				break;
			}
		}
		if(!elanceValues[i]) {
			elanceValues[i] = 1;
		}
	}
	elanceValues.push('');
	
	for(i = 1, len = allSkills.length-1; i < len; i++) {
		for(j = 0; j < SKILLS_FREELANCER.length; j++) {
			if(SKILLS_FREELANCER[j][0].toLowerCase() === allSkills[i]) {
				freelancerValues[i] = SKILLS_FREELANCER[j][1];
				break;
			}
		}
		if(!freelancerValues[i]) {
			freelancerValues[i] = 1;
		}
	}
	freelancerValues.push('');
	
	rows.push(proffstoreValues);
	rows.push(elanceValues);
	rows.push(freelancerValues);
	
	 var data = google.visualization.arrayToDataTable(rows);

     var options = {
       width : '100%',
       height : 600,
       bar: { groupWidth: '75%' },
       isStacked: true,
     };
	     
    var chart = new google.charts.Bar(document.getElementById('skills-compare'));

	chart.draw(data, options);
	
}