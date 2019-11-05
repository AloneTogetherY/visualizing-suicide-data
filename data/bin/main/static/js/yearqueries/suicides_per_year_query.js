let dictSuicideRateInYear = [];
$.get('/visualization/data/suicide/rate', function (data) {
    data.forEach(element => {
        dictSuicideRateInYear.push({
            year: element.year,
            suicidesnr: element.suicides_nr
        })
    });
});

function renderTotalNumberOfSuicides(data) {
    let total = 0;
    for (let i = 0; i < data.getNumberOfRows(); i++)
        total = total + data.getValue(i, 1);
    document.getElementById('suicideNumberId').innerHTML = total;
}

function drawChart() {

    let data = new google.visualization.DataTable();
    data.addColumn('string', 'Year');
    data.addColumn('number', 'Suicides');
    dictSuicideRateInYear.forEach(element => {
        data.addRow([element.year, element.suicidesnr])
    });
    let options = {
        title: 'Number of suicides per year from 1985 till 2015',
        pieHole: 0.8,
        backgroundColor: 'transparent',
        chartArea: { top: 150, width: "100%", height: "100%"},
        titleTextStyle: {
            fontSize: 15
        },
        pieSliceTextStyle: {
            color: 'black',
            text: "hallo"
          },
          pieSliceText: "none",
          legend:{
              maxLines: 2
          }
          
    };

    function selectHandler() {
        let selectedItem = chart.getSelection()[0];
        if (selectedItem) {
            let suicides = data.getValue(selectedItem.row, 1);
            let year = data.getValue(selectedItem.row, 0);
            selectedYear = year;
            let element = document.getElementById('info-title1');
            element.innerHTML = suicides.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + " suicides across the world in " + year;
            element.classList.add("z-depth-1");

            document.dispatchEvent(new CustomEvent("openDataMap", {"detail": true}));
            document.dispatchEvent(new CustomEvent("refreshColumnChart", {"detail": true}));
            document.dispatchEvent(new CustomEvent("updateBarRadialLayout"));
        }
    }

    function mouseOverHandler() {
        $('#donutchart').css('cursor', 'pointer')
    }

    function mouseOutHandler() {
        $('#donutchart').css('cursor', 'default')
    }

    let chart = new google.visualization.PieChart(document.getElementById('donutchart'));
    google.visualization.events.addListener(chart, 'select', selectHandler);
    google.visualization.events.addListener(chart, 'onmouseover', mouseOverHandler);
    google.visualization.events.addListener(chart, 'onmouseout', mouseOutHandler);
    chart.draw(data, options);

    renderTotalNumberOfSuicides(data);
}


$(window).resize(function(){
    drawChart();
});