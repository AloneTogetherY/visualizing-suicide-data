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

    return total
}

function drawChart() {

    let data = new google.visualization.DataTable();
    data.addColumn('string', 'Year');
    data.addColumn('number', 'Suicides');
    dictSuicideRateInYear.forEach(element => {
        data.addRow([element.year, element.suicidesnr])
    });
    let options = {
        title: 'Total: ' + renderTotalNumberOfSuicides(data).toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","),
        pieHole: 0.8,
        backgroundColor: 'transparent',
        colors: [
            '#a4d8c2', '#f3d999', '#d3758f', '#dcc392', '#2e4783',
            '#C1232B', '#27727B', '#FCCE10', '#E87C25', '#B5C334',
            '#FE8463', '#9BCA63', '#FAD860', '#F3A43B', '#60C0DD',
            '#D7504B', '#C6E579', '#F4E001', '#F0805A', '#26C0C0',
            '#c12e34', '#e6b600', '#0098d9', '#2b821d',
            '#E01F54', '#001852', '#f5e8c8', '#b8d2c7', '#c6b38e',
            '#82b6e9', '#ff6347', '#a092f1', '#0a915d', '#eaf889',
            '#6699FF', '#ff6666', '#3cb371', '#d5b158', '#38b6b6',
            '#005eaa', '#339ca8', '#cda819', '#32a487',
        ],
        titleTextStyle: {
            fontSize: 18
        },
        pieSliceTextStyle: {
            color: 'black',
            text: "hallo"
        },
        pieSliceText: "none",
        legend: {
            maxLines: 2,
            position: 'bottom',
            alignment: 'center',
            fontSize: 78
        }
    };


    function selectHandler() {
        let selectedItem = chart.getSelection()[0];
        if (selectedItem) {
            let suicides = data.getValue(selectedItem.row, 1);
            let year = data.getValue(selectedItem.row, 0);
            selectedYear = year;
            let element = document.getElementById('info-title1');
            element.innerHTML = suicides + " suicides across the world in " + year;
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
    centerTitleText();
}


function centerTitleText() {
    let $this = $('#donutchart > div > div:nth-child(1)');
    let titleDonutChart = $('#donutchart > div > div:nth-child(1) > div > svg > g:nth-child(2) > text');
    let width = $this.width();
    let height = $this.height();

    let centerX = width / 2.25;
    let centerY = height / 2;

    titleDonutChart.attr("x", centerX);
    titleDonutChart.attr("y", centerY);
}

$(window).resize(function () {
    drawChart();
    setTimeout(function () {
        centerTitleText();
    }, 1);
});