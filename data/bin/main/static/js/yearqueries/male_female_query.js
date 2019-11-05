let dictSuicideRateContinent = [];

function getGenderData(p_gender) {
    dictSuicideRateContinent = []; // reset array
    let year = selectedYear;
    if (year === "1985 - 2015") {
        $.ajax({
            type: 'GET',
            async: false,
            url: '/visualization/data/years/sex/' + p_gender,
            success: function (data) {
                dictSuicideRateContinent = createDictGenderData(data);
            }
        });
    }
    else {
        $.ajax({
            type: 'GET',
            async: false,
            url: '/visualization/data/years/' + year + '/sex/' + p_gender,
            success: function (data) {
                dictSuicideRateContinent = createDictGenderData(data);
            }
        });
    }
    return dictSuicideRateContinent;
}

function createDictGenderData(p_data) {
    let continentValues, suicideRateValues;
    let dictSuicideRateContinentTemp = [];
    continentValues = Object.keys(p_data);
    suicideRateValues = Object.values(p_data);
    count = continentValues.length;
    for (i = 0; i < count; i++) {
        dictSuicideRateContinentTemp.push({
            continent: continentValues[i],
            suicidRate: suicideRateValues[i]
        })
    }
    return dictSuicideRateContinentTemp;
}

function drawBarChart() {
    let maleDict = getGenderData("male");
    let femaleDict = getGenderData("female");
    let options = newBarChartOptions();
    let chart = new google.visualization.ColumnChart(document.getElementById('bar_chart'));
    chart.draw(drawData(maleDict, femaleDict), options);
}

function drawData(listMen, listWomen) {

    let continents = [], suicideRateMale = [], suicideRateFemale = [];
    suicideRateMale.push(window.selectedYear);
    suicideRateFemale.push(window.selectedYear);
    continents.push("Continent");

    listMen.forEach(element => {
        continents.push(element.continent);
        suicideRateMale.push(element.suicidRate);

    });
    listWomen.forEach(element => {
        suicideRateFemale.push(element.suicidRate);

    });
    suicideRateMale.push("Male");
    suicideRateFemale.push("Female");
    continents.push({role: 'annotation'});
    return new google.visualization.arrayToDataTable([
        continents,
        suicideRateMale,
        suicideRateFemale
    ]);
}

function newBarChartOptions() {
    return {
        title: 'Suicide rate by continents in ' + selectedYear + ' sorted by male and female',
        height: 400,
        legend: {position: 'bottom', maxLines: 2},
        vAxis: {
            title: 'Number of suicides',
            textStyle: {fontSize: 10},
            scaleType: 'log'
        },
        hAxis: {
            title: 'Year',
            textStyle: {fontSize: 10}
            , scaleType: 'log'

        },
        titleTextStyle: {
            fontSize: 15
        }
    };
}

document.addEventListener("refreshColumnChart", function (e) {
    drawBarChart();
});

$(window).resize(function () {
    drawBarChart();
});

