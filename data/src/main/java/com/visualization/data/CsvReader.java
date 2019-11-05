package com.visualization.data;

import com.opencsv.CSVReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

// Order country,year,sex,age,suicides_no,population,suicides/100k pop,country-year,HDI for year, gdp_for_year ($) ,
// gdp_per_capita ($),generation
@Component
public class CsvReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvReader.class);

    private List<DataModel> dataModelList;

    List<DataModel> fetchCsv() {
        return dataModelList;
    }

    @PostConstruct
    public void createData() throws IOException, ParseException {


        dataModelList = new ArrayList<>();

        InputStream file = new ClassPathResource("data/master.csv").getInputStream();
        InputStream jsonFile = new ClassPathResource("data/countries.json").getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(jsonFile);
        CSVReader reader;
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(inputStreamReader);

        try {
            reader = new CSVReader(new InputStreamReader(file));
            reader.skip(1);
            String[] line;

            while ((line = reader.readNext()) != null) {
                Object[] regionAreaArray = newRegionAndArea(line[0], jsonArray);
                try {
                    dataModelList.add(
                            new DataModel()
                                    .country(line[0])
                                    .year(Integer.parseInt(line[1]))
                                    .sex(line[2])
                                    .age(line[3])
                                    .region(regionAreaArray[0].toString())
                                    .area(Double.parseDouble(regionAreaArray[1].toString()))
                                    .suicides_no(Integer.parseInt(line[4]))
                                    .population(Integer.parseInt(line[5]))
                                    .suicides_per_100k_population(Double.parseDouble(line[6]))
                                    .country_year(line[7])
                                    .hdi_per_year(line[8])
                                    .gdp_for_year(line[9])
                                    .gdp_per_capita(Integer.parseInt(line[10]))
                                    .generation(line[11])
                                    .create());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    private static Object[] newRegionAndArea(String country, JSONArray jsonArray) {
        Object[] result = null;

        for (Object o : jsonArray) {
            JSONObject object = (JSONObject) o;

            String name = (String) object.get("name");
            if (containsIgnoreCase(country, name)) {

                result = new Object[]{object.get("region"), object.get("area")};
            } else if (country == null || name == null) {
                LOGGER.info("Country or name is null");

            }
        }
        return result;

    }

    private static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) return false;

        final int length = searchStr.length();
        if (length == 0)
            return true;

        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length))
                return true;
        }
        return false;
    }
}
