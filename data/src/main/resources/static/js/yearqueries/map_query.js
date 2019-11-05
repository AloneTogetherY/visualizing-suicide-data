let countries = [], suicides = [];
countries.push("Countries");
suicides.push("Suicides");
let flag = false;
let initData = [{
    type: 'choropleth',
    locationmode: 'country names',
    autocolorscale: false,

}];

let layout = {
    yaxis: {
        fixedrange: true
    },
    xaxis: {
        fixedrange: true
    },
    title: '<b>Suicide rate per country across the world</b>',
    titlefont: {
        size: 16
    },

    geo: {
        showframe: false,
        resolution: 40,
        projection: {
            type: 'natural earth',
        }
    }
};
Plotly.newPlot(dataMap, initData, layout, {
    showLink: false, displayModeBar: false, scrollZoom: false, responsive: true
});


dataMap.on('plotly_relayout', function () {

    if (flag) {
        updatePlotlyMap();
    }
});


function updatePlotlyMap() {
    let data = [];

    Plotly.d3.csv('/visualization/data/countries/' + selectedYear + '/file.csv', function (err, rows) {
        function unpack(rows, key) {
            return rows.map(function (row) {
                return row[key];
            });
        }

        let countries = unpack(rows, 'country');
        let suicides = unpack(rows, 'suicides');
        data = [{
            type: 'choropleth',
            locationmode: 'country names',
            locations: countries,
            z: suicides,
            autocolorscale: false,
            colorscale: 'YIGnBu',
            reversescale: true,
            hovermode: 'closest',
            marker: {
                line: {
                    color: 'rgb(180,180,180)',
                    width: 0.3
                }
            },
            tick0: 0,
            zmin: 1,
            dtick: 1000,
            colorbar: {
                autotic: false,
                title:
                    'Info:<br>White countries<br>have no data<br><br>Suicides',
                titlefont: {
                    size: 10
                },
            }
        }];
        let layout = {
            yaxis: {
                fixedrange: true
            },
            xaxis: {
                fixedrange: true
            },
            title: '<br>Suicide rate per country across the world in ' + selectedYear + '</br>',
            titlefont: {
                size: 16
            },
            geo: {
                showframe: false,
                showcountries: true,
                resolution: 40,
                projection: {
                    type: 'natural earth',
                }
            }
        };

        Plotly.react(dataMap, data, layout);
    });
}

document.addEventListener("openDataMap", function (e) {
    flag = true;
    updatePlotlyMap();
});