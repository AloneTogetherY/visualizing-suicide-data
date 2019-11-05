package com.visualization.data;

import com.visualization.data.ageforcountry.Age;
import com.visualization.data.ageforcountry.AgeWrapper;
import com.visualization.data.malefemaleforcountry.Sex;
import com.visualization.data.malefemaleforcountry.Wrapper;
import com.visualization.data.yearcountryquery.PersonalData;
import com.visualization.data.yearcountryquery.SuicidesForYearAndCountry;
import com.visualization.data.yearcountryquery.SuicidesForYearAndCountryIntermediate;
import com.visualization.data.yearquery.SuicidePerYear;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class DataController {

    private CsvReader csvReader;
    private List<DataModel> dataModelList;

    @Autowired
    public DataController(CsvReader csvReader) {
        this.csvReader = csvReader;
    }

    @PostConstruct
    public void init() {
        dataModelList = csvReader.fetchCsv();
    }

    @RequestMapping(value = {"/", "/index"}, produces = MediaType.TEXT_HTML_VALUE)
    public final java.lang.String index() {
        return "index";
    }

    @RequestMapping(value = {"/countries"}, produces = MediaType.TEXT_HTML_VALUE)
    public final java.lang.String textIndex() {
        return "country_view";
    }

    @RequestMapping(value = "/visualization/data",
            produces = {"application/json"},
            method = RequestMethod.GET)
    public final ResponseEntity getVisualizationData() {
        return ResponseEntity.ok().cacheControl(CacheControl.noCache()).body(dataModelList);
    }

    @RequestMapping(value = "/visualization/data/country/{country}/sex",
            produces = {"application/json"},
            method = RequestMethod.GET)
    public final ResponseEntity getVisualizationDataByCountryForSex(@PathVariable String country) {

        Map<Integer, Double> males = new HashMap<>();
        Map<Integer, Double> females = new HashMap<>();
        List<Sex> f = new ArrayList<>();
        List<Sex> m = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#.##");

        dataModelList.stream()
                .filter(a -> a.getYear() != 2016 && a.getCountry().equalsIgnoreCase(country))
                .forEach(a -> {
                    if (a.getSex().equalsIgnoreCase("female")) {
                        if (females.containsKey(a.getYear())) {
                            females.put(a.getYear(),

                                    Double.parseDouble(df.format(females.get(a.getYear())
                                            + a.getSuicides_per_100k_population())
                                            .replace(',', '.')));
                        } else {
                            females.put(a.getYear(),
                                    Double.parseDouble(df.format(a.getSuicides_per_100k_population())
                                            .replace(',', '.')));
                        }
                    } else if (a.getSex().equalsIgnoreCase("male")) {
                        if (males.containsKey(a.getYear())) {
                            males.put(a.getYear(),
                                    Double.parseDouble(df.format(males.get(a.getYear())
                                            + a.getSuicides_per_100k_population())
                                            .replace(',', '.')));
                        } else {
                            males.put(a.getYear(),
                                    Double.parseDouble(df.format(a.getSuicides_per_100k_population())
                                            .replace(',', '.')));
                        }
                    }
                });
        males.keySet().forEach(k -> m.add(new Sex(k, males.get(k))));

        females.keySet().forEach(k -> f.add(new Sex(k, females.get(k))));

        return ResponseEntity.ok().body(new Wrapper(m, f));
    }

    @RequestMapping(value = "/visualization/data/country/{country}/age",
            produces = {"application/json"},
            method = RequestMethod.GET)
    public final ResponseEntity getVisualizationDataByCountryForAge(@PathVariable String country) {

        Map<Age, Double> ages = new HashMap<>();
        Map<Integer, AgeWrapper> multiMap = MultiValueMap.decorate(new HashMap<Integer, AgeWrapper>());

        DecimalFormat df = new DecimalFormat("#.##");
        dataModelList.stream()
                .filter(a -> a.getYear() != 2016 && a.getCountry().equalsIgnoreCase(country))
                .forEach(a -> {

                    Age g = new Age(a.getAge(), a.getYear());
                    if (ages.containsKey(g)) {
                        ages.put(g,
                                Double.parseDouble(df.format(ages.get(g)
                                        + a.getSuicides_per_100k_population())
                                        .replace(',', '.')));
                    } else {
                        ages.put(g,
                                Double.parseDouble(df.format(a.getSuicides_per_100k_population())
                                        .replace(',', '.')));
                    }
                });

        ages.forEach((k, v) -> {
            multiMap.put(k.getYear(), new AgeWrapper(v, k.getAge()));
        });
        return ResponseEntity.ok().body(multiMap);
    }

    @RequestMapping(value = "/visualization/data/suicide/rate",
            produces = {"application/json"},
            method = RequestMethod.GET)
    public final ResponseEntity getSuicideRateForYear() {

        List<SuicidePerYear> suicidePerYears = new ArrayList<>();

        range().forEach(
                year ->
                        suicidePerYears.add(new SuicidePerYear(
                                dataModelList.stream()
                                        .filter(p -> p.getYear() == year).map(DataModel::getSuicides_no)
                                        .collect(Collectors.toList()), Integer.toString(year)))
        );

        List<Integer> temp = suicidePerYears.stream()
                .map(SuicidePerYear::getSuicides_nr)
                .sorted()
                .collect(Collectors.toList());

        suicidePerYears.forEach(a ->
        {

            a.setNormalized_value((
                    (a.getSuicides_nr() - temp.get(0) / temp.get(temp.size() - 1) - temp.get(0))
                            /
                            (100 - 1) + 1
            ));
        });

        return ResponseEntity.ok().body(suicidePerYears);
    }

    @RequestMapping(value = "/visualization/data/suicide/year/{year}/country/{country}",
            produces = {"application/json"},
            method = RequestMethod.GET)
    public final ResponseEntity getSuicideRateForYear(@PathVariable int year,
                                                      @PathVariable String country) {

        double female = 0, male = 0, sum = 0;
        SuicidesForYearAndCountryIntermediate suicidesForYearAndCountryIntermediate = new SuicidesForYearAndCountryIntermediate();
        dataModelList.stream()
                .filter(p -> p.getCountry()
                        .equalsIgnoreCase(country) && p.getYear() == year)
                .collect(Collectors.toList()).forEach(
                a -> suicidesForYearAndCountryIntermediate.putInAgeToSuicidesMap(new PersonalData(a.getAge(),
                        a.getSex(), a.getYear()), a.getSuicides_per_100k_population())
        );

        sum = suicidesForYearAndCountryIntermediate.getAgeToSuicidesMap().values().stream().mapToInt(Double::intValue).sum();

        SuicidesForYearAndCountry forYearAndCountry = new SuicidesForYearAndCountry();

        for (Map.Entry<PersonalData, Double> entry :
                suicidesForYearAndCountryIntermediate.getAgeToSuicidesMap().entrySet()) {
            if (entry.getKey().getSex().equalsIgnoreCase("female")) {
                female += entry.getValue();
            } else if (entry.getKey().getSex().equalsIgnoreCase("male")) {
                male += entry.getValue();
            }
            forYearAndCountry.put(entry.getKey().getAge(), entry.getValue());
        }
        DecimalFormat df = new DecimalFormat("#.#");

        forYearAndCountry.setMaleUndFemale(Double.parseDouble(df.format((male / sum) * 100)
                        .replace(',', '.'))
                , Double.parseDouble(df.format((female / sum) * 100).replace(',', '.')));

        return ResponseEntity.ok().body(forYearAndCountry);
    }

    @RequestMapping(value = "/visualization/data/suicide/countries",
            produces = {"application/json"},
            method = RequestMethod.GET)
    public final ResponseEntity getSuicidesAndGdpForCountryAllYears() throws IOException {

        //Use only to create new data!!!!!

        //Following countries were excluded: "Macau", "Bosnia and Herzegovina", "Montenegro",
        //                "Dominica", "Bahamas", "Grenada", "Maldives", "Jamaica", "Saint Kitts and Nevis", "Aruba", "Kiribati",
        //                "Fiji", "Sri Lanka", "United Arab Emirates", "San Marino", "Oman", "Cabo Verde",
        //                "Antigua and Barbuda", "Azerbaijan"

      /*  final JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        ObjectNode rootNode = nodeFactory.objectNode();
        ArrayNode arrayNode = nodeFactory.arrayNode();
        ArrayNode countries = nodeFactory.arrayNode();
        ArrayNode years = nodeFactory.arrayNode();
        rootNode.set("countries", countries);
        rootNode.set("timeline", years);
        rootNode.set("series", arrayNode);

        Set<Integer> y = new HashSet<>();
        Set<String> c = new HashSet<>();
        Map<IntermediateAllYears, Double> yearToSuicideNo = new HashMap<>();
        List<String> excludedCountries = new ArrayList<>(Arrays.asList("Macau", "Bosnia and Herzegovina", "Montenegro",
                "Dominica", "Bahamas", "Grenada", "Maldives", "Jamaica", "Saint Kitts and Nevis", "Aruba", "Kiribati",
                "Fiji", "Sri Lanka", "United Arab Emirates", "San Marino", "Oman", "Cabo Verde",
                "Antigua and Barbuda", "Azerbaijan"));


        csvReader.fetchCsv()
                .forEach(a ->
                {
                    if (a.getYear() != 2016 && !excludedCountries.contains(a.getCountry())) {
                        y.add(a.getYear());
                        c.add(a.getCountry());

                        IntermediateAllYears temp = new IntermediateAllYears(a.getCountry(), a.getYear(),
                                a.getPopulation(),a.getGdp_per_capita());
                        if (yearToSuicideNo.containsKey(temp)) {
                            yearToSuicideNo.put(temp, yearToSuicideNo.get(temp) + a.getSuicides_per_100k_population());
                        } else {
                            yearToSuicideNo.put(temp, a.getSuicides_per_100k_population());
                        }
                    }
                });

        Map<Integer, List<Object>> multiMap = MultiValueMap.decorate(new HashMap<Integer, List<Object>>());
        Map<Integer, List<Object>> multiMap2 = MultiValueMap.decorate(new HashMap<Integer, List<Object>>());

        for (Map.Entry<IntermediateAllYears, Double> entry : yearToSuicideNo.entrySet()) {

            multiMap.put(entry.getKey().getTimeline(), Arrays.asList(
                    entry.getKey().getGpd(), entry.getValue(), entry.getKey().getTimeline(),
                    entry.getKey().getCountry(), entry.getKey().getTimeline()
            ));
            multiMap2.put(entry.getKey().getTimeline(), Arrays.asList(
                    entry.getKey().getGpd(), entry.getValue(), entry.getKey().getTimeline(),
                    entry.getKey().getCountry(), entry.getKey().getTimeline()
            ));
        }

        itr(c, multiMap, multiMap2, 1985);
        itr(c, multiMap, multiMap2, 1986);
        itr(c, multiMap, multiMap2, 1987);
        itr(c, multiMap, multiMap2, 1988);
        itr(c, multiMap, multiMap2, 1989);
        itr(c, multiMap, multiMap2, 1990);
        itr(c, multiMap, multiMap2, 1991);
        itr(c, multiMap, multiMap2, 1992);
        itr(c, multiMap, multiMap2, 1993);
        itr(c, multiMap, multiMap2, 1994);
        itr(c, multiMap, multiMap2, 1995);
        itr(c, multiMap, multiMap2, 1996);
        itr(c, multiMap, multiMap2, 1997);
        itr(c, multiMap, multiMap2, 1998);
        itr(c, multiMap, multiMap2, 1999);
        itr(c, multiMap, multiMap2, 2000);
        itr(c, multiMap, multiMap2, 2001);
        itr(c, multiMap, multiMap2, 2002);
        itr(c, multiMap, multiMap2, 2003);
        itr(c, multiMap, multiMap2, 2004);
        itr(c, multiMap, multiMap2, 2005);
        itr(c, multiMap, multiMap2, 2006);
        itr(c, multiMap, multiMap2, 2007);
        itr(c, multiMap, multiMap2, 2008);
        itr(c, multiMap, multiMap2, 2009);
        itr(c, multiMap, multiMap2, 2010);
        itr(c, multiMap, multiMap2, 2011);
        itr(c, multiMap, multiMap2, 2012);
        itr(c, multiMap, multiMap2, 2013);
        itr(c, multiMap, multiMap2, 2014);
        itr(c, multiMap, multiMap2, 2015);

        List<String> mainList = new ArrayList<>(c);
        Collections.sort(mainList);
        y.forEach(years::add);
        mainList.forEach(countries::add);
        Set<Integer> keys = multiMap.keySet();

        keys.forEach(
                a ->
                {
                    ValueNode dataNode = nodeFactory.pojoNode(multiMap.get(a));
                    arrayNode.add(dataNode);
                }
        );        return ResponseEntity.ok().cacheControl(CacheControl.noCache()).body(rootNode);
*/
        Resource resourceHappyCase = new ClassPathResource("data/countries_gpdpercapita_suicides.json");
        return ResponseEntity.ok().body(IOUtils.toString(resourceHappyCase
                .getInputStream(), "utf-8"));
    }

    private void itr(Set<String> c, Map<Integer, List<Object>> multiMap,
                     Map<Integer, List<Object>> multiMap2, int year) {
        List<String> cur = new ArrayList<>();
        for (String cu : c) {

            for (Object a : multiMap2.get(year)) {
                List l = (List) a;
                cur.add((String) l.get(3));
            }
            if (!cur.contains(cu)) {
                multiMap.put(year, Arrays.asList(0, 0, 0, cu, year));
            }
        }
    }

    @RequestMapping(value = "/visualization/data/countries/{year}/file.csv",
            method = RequestMethod.GET)
    public final void getVisualizationDataForCountries(
            final @PathVariable
                    int year, HttpServletResponse response) throws IOException {

        Map<String, Integer> countries = new HashMap<>();
        dataModelList.stream()
                .filter(p -> p.getYear() == year).forEach(k
                -> {
            String country = k.getCountry();
            country = getPlotlyCountryName(country);
            if (countries.containsKey(country)) {
                countries.put(country,
                        countries.get(country) + k.getSuicides_no());
            } else {
                countries.put(country, k.getSuicides_no());
            }
        });
        StringBuilder sb = new StringBuilder();
        sb.append("country");
        sb.append(",");
        sb.append("suicides");
        sb.append("\n");
        for (Map.Entry<String, Integer> entry : countries.entrySet()) {
            sb.append(entry.getKey());
            sb.append(",");
            sb.append(entry.getValue());
            sb.append("\n");
        }
        response.setContentType("text/plain; charset=utf-8");
        response.getWriter().print(sb.toString());

    }

    private String getPlotlyCountryName(String country) {
        //These countries are different to the plotly
        if (country.equalsIgnoreCase("Saint Vincent and Grenadines")) country = "Saint Vincent and the Grenadines";
        if (country.equalsIgnoreCase("Russian Federation")) country = "Russia";
        if (country.equalsIgnoreCase("Republic of Korea")) country = "South Korea";
        return country;
    }

    @RequestMapping(value = "/visualization/data/global/suicide/rate",
            produces = {"application/json"},
            method = RequestMethod.GET)
    public final ResponseEntity getVisualizationDataGlobalTrend() {
        DecimalFormat df = new DecimalFormat("#.##");

        Map<Integer, Double> globalTrendMap = new HashMap<>();
        range().forEach(year ->
                dataModelList.stream()
                        .filter(p -> p.getYear() == year).forEach(k
                        -> {
                    if (globalTrendMap.containsKey(year)) {
                        globalTrendMap.put(year,
                                Double.parseDouble(df.format(
                                        globalTrendMap.get(year) +
                                                k.getSuicides_per_100k_population())
                                        .replace(',', '.')));
                    } else {
                        globalTrendMap.put(year, Double.parseDouble(
                                df.format(k.getSuicides_per_100k_population())
                                        .replace(',', '.')));
                    }
                }));

        return ResponseEntity.ok().cacheControl(CacheControl.noCache()).body(globalTrendMap);
    }

    @RequestMapping(value = "/visualization/data/years/{year}/sex/{sex}",
            produces = {"application/json"},
            method = RequestMethod.GET)
    public final ResponseEntity getVisualizationDataForYearAndSex(final @PathVariable
                                                                          int year, final @PathVariable String sex
    ) {
        Map<String, Integer> regionSuicidesMap = new HashMap<>();
        dataModelList
                .stream().filter(p -> p.getYear() == year
                && p.getSex().equalsIgnoreCase(sex)
        ).forEach(k
                -> {
            String region = k.getRegion();

            if (regionSuicidesMap.containsKey(k.getRegion())) {
                regionSuicidesMap.put(region,
                        regionSuicidesMap.get(region) +
                                k.getSuicides_no());
            } else {
                regionSuicidesMap.put(region,
                        k.getSuicides_no());
            }
        });

        return ResponseEntity.ok().cacheControl(CacheControl.noCache()).body(regionSuicidesMap);
    }

    @RequestMapping(value = "/visualization/data/years/sex/{sex}",
            produces = {"application/json"},
            method = RequestMethod.GET)
    public final ResponseEntity getVisualizationDataForAllYearAndSexPerContinent(final @PathVariable String sex
    ) {
        Map<String, Integer> regionSuicidesMap = new HashMap<>();

        dataModelList
                .stream()
                .filter(p -> p.getSex().equalsIgnoreCase(sex))
                .forEach(k
                        -> {
                    String region = k.getRegion();

                    if (regionSuicidesMap.containsKey(k.getRegion())) {
                        regionSuicidesMap.put(region,
                                regionSuicidesMap.get(region) +
                                        k.getSuicides_no()
                        );
                    } else {
                        regionSuicidesMap.put(region,
                                k.getSuicides_no());
                    }
                });
        return ResponseEntity.ok().cacheControl(CacheControl.noCache()).body(regionSuicidesMap);
    }

    @RequestMapping(value = "/visualization/data/years/{year}/age",
            produces = {"application/json"},
            method = RequestMethod.GET)
    public final ResponseEntity getVisualizationDataForSelectedYearAndAllAges(@PathVariable int year) {
        Map<String, Integer> integerHashMap = new HashMap<>();

        if (year != 2016) {
            ageStream(integerHashMap, year);
        }
        return ResponseEntity.ok().cacheControl(CacheControl.noCache()).body(integerHashMap);
    }

    @RequestMapping(value = "/visualization/data/years/ages",
            produces = {"application/json"},
            method = RequestMethod.GET)
    public final ResponseEntity getVisualizationDataForAllYearsForAllAges() {
        Map<String, Integer> integerHashMap = new HashMap<>();

        range().forEach(
                year -> ageStream(integerHashMap, year)
        );

        return ResponseEntity.ok().cacheControl(CacheControl.noCache()).body(integerHashMap);
    }

    private void ageStream(Map<String, Integer> integerHashMap, Integer year) {
        dataModelList.stream()
                .filter(p -> p.getYear() == year)
                .forEach(a -> {
                    String age = a.getAge();
                    if (integerHashMap.containsKey(age)) {

                        integerHashMap.put(age,
                                integerHashMap.get(age) + a.getSuicides_no());
                    } else {
                        integerHashMap.put(age, a.getSuicides_no());

                    }
                });
    }

    private static List<Integer> range() {
        int cur = Integer.parseInt("1985");
        int stop = Integer.parseInt("2015");
        List<Integer> list = new ArrayList<>();
        while (cur <= stop) {
            list.add(cur++);
        }
        return list;
    }
}