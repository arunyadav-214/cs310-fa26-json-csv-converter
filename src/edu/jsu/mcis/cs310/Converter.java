package edu.jsu.mcis.cs310;

import com.github.cliftonlabs.json_simple.*;
import com.opencsv.*;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

public class Converter {

    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {

        String result = "{}";

        try {

            CSVReader csvReader =
                    new CSVReader(new StringReader(csvString));

            List<String[]> rows = csvReader.readAll();

            JsonObject json = new JsonObject();
            JsonArray prodNums = new JsonArray();
            JsonArray colHeadings = new JsonArray();
            JsonArray data = new JsonArray();

            // Get the column headings from the first CSV row
            String[] headings = rows.get(0);

            for (String heading : headings) {
                colHeadings.add(heading);
            }

            // Convert each remaining CSV row
            for (int i = 1; i < rows.size(); i++) {

                String[] row = rows.get(i);

                // Production number is stored separately
                prodNums.add(row[0]);

                JsonArray dataRow = new JsonArray();

                dataRow.add(row[1]);
                dataRow.add(Integer.parseInt(row[2]));
                dataRow.add(Integer.parseInt(row[3]));
                dataRow.add(row[4]);
                dataRow.add(row[5]);
                dataRow.add(row[6]);

                data.add(dataRow);
            }

            json.put("ProdNums", prodNums);
            json.put("ColHeadings", colHeadings);
            json.put("Data", data);

            result = Jsoner.serialize(json);

            csvReader.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return result.trim();
    }


    @SuppressWarnings("unchecked")
    public static String jsonToCsv(String jsonString) {

        String result = "";

        try {

            JsonObject json =
                    Jsoner.deserialize(jsonString, new JsonObject());

            JsonArray prodNums =
                    (JsonArray) json.get("ProdNums");

            JsonArray colHeadings =
                    (JsonArray) json.get("ColHeadings");

            JsonArray data =
                    (JsonArray) json.get("Data");

            StringWriter stringWriter = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(stringWriter);

            // Create CSV header
            String[] headings =
                    new String[colHeadings.size()];

            for (int i = 0; i < colHeadings.size(); i++) {
                headings[i] =
                        colHeadings.get(i).toString();
            }

            csvWriter.writeNext(headings);

            // Create CSV data rows
            for (int i = 0; i < data.size(); i++) {

                JsonArray dataRow =
                        (JsonArray) data.get(i);

                String[] row =
                        new String[colHeadings.size()];

                row[0] = prodNums.get(i).toString();
                row[1] = dataRow.get(0).toString();
                row[2] = dataRow.get(1).toString();

                int episode =
                        ((Number) dataRow.get(2)).intValue();

                row[3] =
                        String.format("%02d", episode);

                row[4] = dataRow.get(3).toString();
                row[5] = dataRow.get(4).toString();
                row[6] = dataRow.get(5).toString();

                csvWriter.writeNext(row);
            }

            csvWriter.close();

            result = stringWriter.toString();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return result.trim();
    }

}