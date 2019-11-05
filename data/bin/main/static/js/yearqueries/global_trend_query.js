let dictSuicideRateGlobal = [];

function drawBasic() {
    $.ajax({
        type: 'GET',
        async: false,
        url: '/visualization/data/global/suicide/rate',
        success: function (data) {
            let yearValues = Object.keys(data);
            let suicideValues = Object.values(data);
            let count = yearValues.length;
            for (i = 0; i < count; i++) {
                dictSuicideRateGlobal.push({
                    year: yearValues[i],
                    suicidesnr: suicideValues[i]
                })
            }
        }
    });

    let data = new google.visualization.DataTable();
    data.addColumn('string', 'Year');
    data.addColumn('number', 'Suicides per 100k');
    dictSuicideRateGlobal.forEach(element => {
        data.addRow([element.year, element.suicidesnr])
    });
    let options = {
        title: 'Global Suicide Trend',
        hAxis: {
            title: 'Year'
        },
        vAxis: {
            title: 'Suicides per 100k'
        },
        titleTextStyle: {
            fontSize: 15
        },
        height: 250,
        legend: 'none'
    };
    let chart = new google.visualization.LineChart(document.getElementById('trend_chart'));
    chart.draw(data, options);
}

$(window).resize(function () {
    drawBasic();
});
