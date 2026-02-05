package edu.jsu.mcis.cs310;

import com.github.cliftonlabs.json_simple.*;
import com.opencsv.*;

public class Converter {
    
    /*
        
        Consider the following CSV data, a portion of a database of episodes of
        the classic "Star Trek" television series:
        
        "ProdNum","Title","Season","Episode","Stardate","OriginalAirdate","RemasteredAirdate"
        "6149-02","Where No Man Has Gone Before","1","01","1312.4 - 1313.8","9/22/1966","1/20/2007"
        "6149-03","The Corbomite Maneuver","1","02","1512.2 - 1514.1","11/10/1966","12/9/2006"
        
        (For brevity, only the header row plus the first two episodes are shown
        in this sample.)
    
        The corresponding JSON data would be similar to the following; tabs and
        other whitespace have been added for clarity.  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings and which values should be encoded as integers, as
        well as the overall structure of the data:
        
        {
            "ProdNums": [
                "6149-02",
                "6149-03"
            ],
            "ColHeadings": [
                "ProdNum",
                "Title",
                "Season",
                "Episode",
                "Stardate",
                "OriginalAirdate",
                "RemasteredAirdate"
            ],
            "Data": [
                [
                    "Where No Man Has Gone Before",
                    1,
                    1,
                    "1312.4 - 1313.8",
                    "9/22/1966",
                    "1/20/2007"
                ],
                [
                    "The Corbomite Maneuver",
                    1,
                    2,
                    "1512.2 - 1514.1",
                    "11/10/1966",
                    "12/9/2006"
                ]
            ]
        }
        
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
        
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including examples.
        
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String result = "{}"; // default return value; replace later!
        
        try {
        // Prase the CSV
        CSVReader reader = new CSVReader(new java.io.StringReader(csvString));
        java.util.List<String[]> rows = reader.readAll();

        // Getting header row
        String[] headings = rows.get(0);

        //setting up the array
        JsonArray prodNums = new JsonArray();
        JsonArray colHeadings = new JsonArray();
        JsonArray data = new JsonArray();

        //add headings
        for (String h : headings) {
            colHeadings.add(h);
        }

        //looping to get each row of data  
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);

            // first column for prodnum
            prodNums.add(row[0]);
            
            //episode data layering
            JsonArray episode = new JsonArray();
            episode.add(row[1]); //title of episode         
            episode.add(Integer.parseInt(row[2])); //season number
            episode.add(Integer.parseInt(row[3])); //episode number 
            episode.add(row[4]); //stardate number                     
            episode.add(row[5]); //airdate number                      
            episode.add(row[6]); //remastered airdate number                    

            data.add(episode);
        }

        JsonObject root = new JsonObject();
        root.put("ProdNums", prodNums);//production number 
        root.put("ColHeadings", colHeadings);//header
        root.put("Data", data);//data row

        result = root.toJson();
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result.trim();
        
    }
    
    @SuppressWarnings("unchecked")
    public static String jsonToCsv(String jsonString) {
        
        String result = ""; // default return value; replace later!
        
        try {
        // Prase the Json
        JsonObject root = (JsonObject) Jsoner.deserialize(jsonString);

        //setting up arrays
        JsonArray prodNums = (JsonArray) root.get("ProdNums");
        JsonArray colHeadings = (JsonArray) root.get("ColHeadings");
        JsonArray data = (JsonArray) root.get("Data");
        
        //Setting up the CSV writer
        java.io.StringWriter writer = new java.io.StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer);

       //writing the header row
        String[] header = new String[colHeadings.size()];
        for (int i = 0; i < colHeadings.size(); i++) {
            header[i] = colHeadings.get(i).toString();
        }
        csvWriter.writeNext(header);

     //Writing the data rows   
    for (int i = 0; i < data.size(); i++) {
    JsonArray episode = (JsonArray) data.get(i);
    String[] row = new String[episode.size() + 1];
    row[0] = prodNums.get(i).toString();
    for (int j = 0; j < episode.size(); j++) {if (j == 2) { 
        int epNum = Integer.parseInt(episode.get(j).toString());
        if (epNum < 10) {row[j + 1] = "0" + epNum;}
        else {row[j + 1] = Integer.toString(epNum);}}
        else {row[j + 1] = episode.get(j).toString();}
    }

    csvWriter.writeNext(row);
}


        csvWriter.close();
        result = writer.toString();            
        }
        
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result.trim();
        
    }
    
}
