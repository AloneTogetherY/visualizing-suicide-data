//event
var dom = document.getElementById("container");
var domBar = document.getElementById("bar-container");
var chart = echarts.init(domBar, null, {renderer: 'canvas'});
var myChart = echarts.init(dom, null, {renderer: 'canvas'});
var age_dom = document.getElementById("age-container");
var chart2 = echarts.init(age_dom, null, {renderer: 'canvas'});

option = {
    grid: {
        bottom: 0,
        containLabel: true,
        top: 100
    },

    xAxis: {
        type: 'value',
        scale: true,
        name: 'GDP per capita ($)',
        nameLocation: 'end',
        nameTextStyle: {
            fontSize: 9
        }
    },
    yAxis: {
        type: 'value',
        scale: true,
        name: 'Suicides\n per 100k population',
        nameLocation: 'end',
        nameTextStyle: {
            fontSize: 9
        }
    },
    dataZoom: {
        type: 'inside'
    },
    series: []
};

$.get('/visualization/data/suicide/countries', function (data) {
    var series = data.series;
    option.visualMap = {
        show: false,
        min: 0,
        max: 100,
        dimension: 1,
        color: ['#ff7a32', '#ebf400']
    };

    option.legend = {
        // padding: [5, 10]
        type: 'scroll',
        triggerEvent: true,
        data: data.countries,
        selectedMode: 'single'
    };

    data.countries.forEach(function (country) {
        var timeArray = [];
        var data = series.map(function (yearData) {
            var item = yearData.filter(function (item) {
                return item[3] === country;
            })[0];

            if (timeArray.indexOf(item[2]) === -1) timeArray.push(item[2]);
            return {
                label: {
                    normal: {
                        show: true,
                        fontStyle: 'bold',
                        formatter: data = function () {
                            let result = '';
                            if (timeArray[0] === item[4]) result = timeArray[0];
                            else if (timeArray[timeArray.length - 1] === item[2]) result = timeArray[timeArray.length - 1];
                            return result;
                        }
                    },
                    emphasis: {
                        fontStyle: 'normal',
                        position: 'top',
                        show: true,
                        fontSize: 15,
                        backgroundColor: 'white',
                        formatter: data = function () {
                            return 'Year: '
                                + item[2]
                                + '\n'
                                + 'GDP: '
                                + item[0].toLocaleString('en-GB')
                                + '\n'
                                + 'Suicides: '
                                + (item[1]).toFixed(2);
                        }
                    }
                },
                name: item[4],
                value: item
            };
        });


        var links = data.map(function (item, idx) {
            return {
                source: idx,
                target: idx + 1
            };
        });

        links.pop();

        option.series.push({
            axisLabel: {
                textStyle: {
                    fontSize: 200
                }
            },
            name: country,
            type: 'graph',
            coordinateSystem: 'cartesian2d',
            data: data,
            links: links,
            edgeSymbol: ['none', 'arrow'],
            edgeSymbolSize: 5,
            legendHoverLink: false,
            lineStyle: {
                normal: {
                    color: '#1d1d1d'
                }
            },
            itemStyle: {
                normal: {
                    borderWidth: 1,
                    borderColor: '#333'
                }
            },
            label: {
                normal: {
                    textStyle: {
                        color: '#333'
                    },
                    position: 'right'
                }
            },
            symbolSize: 13,
            animationDelay: function (idx) {
                return idx * 150;
            }
        });
    });

    myChart.setOption(option);

});

var females = [], males = [], countryName;

var years = ['years'];
var _15_24_years = ['15-24 years'];
var _75_years = ['75+ years'];
var _25_34_years = ['25-34 years'];
var _35_54_years = ['35-54 years'];
var _5_14_years = ['5-14 years'];
var _55_74_years = ['55-74 years'];


window.onload = function () {

    $.get('/visualization/data/country/albania/sex', function (data) {
        /**
         * Albania begin --> do request
         * @type {null}
         */

        countryName = 'Albania';
        document.getElementById('selectedCountry').innerHTML = "Country: " + countryName;
        females = data.females;
        males = data.males;
        var event = new CustomEvent("updateChart", {"detail": true});
        document.dispatchEvent(event);
    });
    $.get('/visualization/data/country/albania/age', function (data) {
        /**
         * Albania begin --> do request
         * @type {null}
         */
        sortDataForAgeChart(data);

        var event = new CustomEvent("updateAgeChart", {"detail": true});
        document.dispatchEvent(event);
    });
};


myChart.on('legendselectchanged', function (params) {
    countryName = params.name;

    var element = document.getElementById('selectedCountry');
    element.innerHTML = "Country: " + countryName;

    $.get('/visualization/data/country/' + params.name + '/sex', function (data) {

        females = data.females;
        males = data.males;
        var event = new CustomEvent("updateChart", {"detail": true});
        document.dispatchEvent(event);
    });

    $.get('/visualization/data/country/' + params.name + '/age', function (data) {
        /**
         * Albania begin --> do request
         * @type {null}
         */
        resetArrays();

        sortDataForAgeChart(data);

        var event = new CustomEvent("updateAgeChart", {"detail": true});
        document.dispatchEvent(event);
    });
});
var optionBar = null;

document.addEventListener("updateChart", function (e) {

    var xAxisData = [];
    var data1 = [];
    var data2 = [];


    for (var key in males) {
        xAxisData.push(males[key].year);
        data1.push(males[key].suicides);
    }
    for (var k in females) {
        data2.push(females[k].suicides);
    }

    optionBar = {
        color: [
            '#C6E579', '#ff5e59', '#F4E001', '#F0805A', '#26C0C0'],
        legend: {
            data: ['female', 'male'],
            type: 'scroll'
        },
        tooltip: {},
        xAxis: {
            data: xAxisData,
            silent: false,
            splitLine: {
                show: false
            },
            name: 'Year',
            nameLocation: 'end',
            nameTextStyle: {
                fontSize: 9
            }
        },
        yAxis: {
            name: 'Suicides\n per 100k population',
            nameLocation: 'end',
            nameTextStyle: {
                fontSize: 9
            }
        },
        series: [{
            name: 'male',
            type: 'bar',
            data: data1,
            animationDelay: function (idx) {
                return idx * 10;
            }
        }, {
            name: 'female',
            type: 'bar',
            data: data2,
            animationDelay: function (idx) {
                return idx * 10 + 100;
            }
        }],
        animationEasing: 'elasticOut',
        animationDelayUpdate: function (idx) {
            return idx * 5;
        }
    };
    if (optionBar && typeof optionBar === "object") {
        chart.setOption(optionBar, true);
    }
});

var optionAge = null;
document.addEventListener("updateAgeChart", function (e) {

    optionAge = {
        color: ['#C1232B', '#27727B', '#FCCE10', '#E87C25', '#B5C334',
            '#FE8463', '#9BCA63', '#FAD860', '#F3A43B', '#60C0DD',
            '#D7504B', '#C6E579', '#F4E001', '#F0805A', '#26C0C0'],
        //https://echarts.baidu.com/download-theme.html <--here are more color combinations
        legend: {
            padding: [5, -10]//position of legend
        },
        tooltip: {
            trigger: 'axis',
            showContent: true
        },
        dataset: {
            source: [
                years,
                _5_14_years, _15_24_years, _25_34_years, _35_54_years, _55_74_years, _75_years
            ]
        },
        xAxis: {
            type: 'category',
            name: 'Year',
            nameLocation: 'end',
            nameTextStyle: {
                fontSize: 9
            }
        },
        yAxis: {
            gridIndex: 0,
            name: 'Suicides\n per 100k population',
            nameLocation: 'end',
            nameTextStyle: {
                fontSize: 9
            }
        },
        grid: {top: '55%'},
        series: [
            {type: 'line', smooth: true, seriesLayoutBy: 'row'},
            {type: 'line', smooth: true, seriesLayoutBy: 'row'},
            {type: 'line', smooth: true, seriesLayoutBy: 'row'},
            {type: 'line', smooth: true, seriesLayoutBy: 'row'},
            {type: 'line', smooth: true, seriesLayoutBy: 'row'},
            {type: 'line', smooth: true, seriesLayoutBy: 'row'},
            {
                type: 'pie',
                id: 'pie',
                radius: '30%',
                center: ['50%', '30%'], //position of pie chart

                label: {
                    formatter: '({d}%)'
                },
                encode: {
                    itemName: 'year'
                }
            }
        ]
    };

    chart2.on('updateAxisPointer', function (event) {
        var xAxisInfo = event.axesInfo[0];
        if (xAxisInfo) {
            var dimension = xAxisInfo.value + 1;
            chart2.setOption({
                series: {
                    id: 'pie',
                    label: {
                        formatter: '({d}%)'
                    },
                    encode: {
                        value: dimension,
                        tooltip: dimension
                    }
                }
            });
        }
    });

    chart2.setOption(optionAge);
});

function reset() {
    window.location = window.location
}

window.addEventListener('resize', function (event) {
    chart.resize();
    myChart.resize();
    chart2.resize();
});


document.getElementById("countries-refresh-btn").addEventListener("click", reset);
