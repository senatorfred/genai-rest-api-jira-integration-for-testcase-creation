package com.asecurityguru.ollamarestapi.functions;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVFIleGenerator {


    public static void generateCsvFile(String filePath, List<String[]> data) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (String[] rowData : data) {
                writer.append(String.join(",", rowData));
                writer.append("\n");
            }
            writer.flush();
            System.out.println("CSV file created successfully at: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing CSV file: " + e.getMessage());
        }
    }

}
