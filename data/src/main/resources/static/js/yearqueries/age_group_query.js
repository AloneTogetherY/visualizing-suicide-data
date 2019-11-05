let initialAgeData = [];

$.ajax({
    type: 'GET',
    async: false,
    url: '/visualization/data/years/ages',
    success: function (data) {
        Object.keys(data)
        .forEach(function eachKey(key) {
            initialAgeData.push(
                {
                    age: key,
                    suicideRate: data[key]
                }
            );
        });
    }
});
document.addEventListener("updateBarRadialLayout", function (e) {
    let ageData = [];
    $.ajax({
        type: 'GET',
        async: false,
        url: '/visualization/data/years/' + selectedYear + '/age',
        success: function (data) {
            Object.keys(data)
            .forEach(function eachKey(key) {
                ageData.push(
                    {
                        age: key,
                        suicideRate: data[key]
                    }
                );
            });
        }
    });

    if(selectedYear === "1985 - 2015")
    {
        radialChart.data = initialAgeData;
    }
    else
    {
        radialChart.data = ageData;
    }
    updateTitle = getRadialAgeTitle();

    updateTitle.text = "Suicidrate per age worldwide in " + selectedYear;
    radialChart.validateData();
});
am4core.ready(function () {
    // Themes begin
    am4core.useTheme(am4themes_kelly);
    am4core.useTheme(am4themes_animated);
    // Themes end
    radialChart = am4core.create("chartdiv", am4charts.RadarChart);
    radialChart.hiddenState.properties.opacity = 0; // this creates initial fade-in
    radialChart.innerRadius = am4core.percent(20);
    radialChart.radius = am4core.percent(90);
    radialChart.startAngle = -80;
    radialChart.endAngle = 260;
    radialChart.data = initialAgeData;

    var categoryAxis = radialChart.xAxes.push(new am4charts.CategoryAxis());
    categoryAxis.renderer.grid.template.location = 0;
    categoryAxis.dataFields.category = "age";
    categoryAxis.renderer.labels.template.location = 0.5;
    categoryAxis.renderer.grid.template.strokeOpacity = 0.08;
    categoryAxis.renderer.tooltipLocation = 0.5;
    categoryAxis.tooltip.disabled = true;
    categoryAxis.renderer.labels.template.bent = true;
    categoryAxis.renderer.labels.template.padding(0, 0, 0, 0);
    categoryAxis.renderer.labels.template.radius = 7;
    var valueAxis = radialChart.yAxes.push(new am4charts.ValueAxis());
    valueAxis.renderer.minGridDistance = 30;
    valueAxis.renderer.grid.template.strokeOpacity = 0.08;
    valueAxis.tooltip.disabled = true;
    // axis break
    var axisBreak = valueAxis.axisBreaks.create();
    axisBreak.startValue = 0;
    axisBreak.endValue = 0;
    axisBreak.breakSize = 0.02;
    // make break expand on hover
    var hoverState = axisBreak.states.create("hover");
    hoverState.properties.breakSize = 3;
    hoverState.properties.opacity = 0.1;
    hoverState.transitionDuration = 1500;
    axisBreak.defaultState.transitionDuration = 1000;

    let series = radialChart.series.push(new am4charts.RadarColumnSeries());

    series.dataFields.categoryX = "age";
    series.dataFields.valueY = "suicideRate";
    series.columns.template.fill = am4core.color("#CDA2AB");

    series.columns.template.tooltipText = "{valueY.value}";
    series.columns.template.tooltipY = 0;
    series.columns.template.strokeOpacity = 0;

    radialChart.seriesContainer.zIndex = -1;
    // as by default columns of the same series are of the same color, we add adapter which takes colors from chart.colors color set
    series.columns.template.adapter.add("fill", (fill, target) => {
        return radialChart.colors.getIndex(target.dataItem.index);
    });
    let cursor = new am4charts.RadarCursor();
    cursor.innerRadius = am4core.percent(10);
    cursor.lineY.disabled = true;
    cursor.xAxis = categoryAxis;
    radialChart.cursor = cursor;
    let title = radialChart.titles.create();
    title.text = "Suicidrate per age across the world in " + selectedYear;
    title.fontSize = 15;
    title.bold = true;
    title.marginBottom = 30;
    setRadialAgeTitle(title);
}); // end am4core.ready()
function setRadialAgeTitle(p_title) {
    radialTitle = p_title;
}

function getRadialAgeTitle() {
    return radialTitle;
}