function sortDataForAgeChart(data) {
    for (var key in data) {
        if (data.hasOwnProperty(key)) {
            years.push(key);
            var keys = data[key];
            for (var k in keys) {
                switch (keys[k].age) {
                    case _15_24_years[0]:
                        _15_24_years.push(keys[k].suicides);
                        break;
                    case _75_years[0]:
                        _75_years.push(keys[k].suicides);
                        break;
                    case _35_54_years[0]:
                        _35_54_years.push(keys[k].suicides);
                        break;
                    case _25_34_years[0]:
                        _25_34_years.push(keys[k].suicides);
                        break;
                    case _5_14_years[0]:
                        _5_14_years.push(keys[k].suicides);
                        break;
                    case _55_74_years[0]:
                        _55_74_years.push(keys[k].suicides);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}

function resetArrays() {
    years.length = 0;
    _15_24_years.length = 0;
    _75_years.length = 0;
    _25_34_years.length = 0;
    _35_54_years.length = 0;
    _5_14_years.length = 0;
    _55_74_years.length = 0;

    years = ['years'];
    _15_24_years = ['15-24 years'];
    _75_years = ['75+ years'];
    _25_34_years = ['25-34 years'];
    _35_54_years = ['35-54 years'];
    _5_14_years = ['5-14 years'];
    _55_74_years = ['55-74 years'];
}
