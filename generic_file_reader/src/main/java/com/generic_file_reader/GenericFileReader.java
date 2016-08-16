package com.generic_file_reader;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arsalan
 */
public class GenericFileReader {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Open file
        File file = new File("testFile.xlsx");

        FileReaderFactory factory = new FileReaderFactory();
        MAFileReader maFileReader;
        try {
            //Get file reader
            maFileReader = factory.getFileReader(file);

            //Read File Contents
            MAFile maFile = maFileReader.getFileContent();

            System.out.println(maFile.getFileName());

            //Get groups of unique sheets having same headers
            Map<Integer, List<FileSheet>> map = Util.createGroups(maFile);
            System.out.println(map.size());

            //Conversion to object to JSON
            String json = Util.getJson(maFile);
            System.out.println("JSON:");
            System.out.println(json);
            MAFile convertedObj = Util.parseJson(json);
            System.out.println(convertedObj.getFileName());

        } catch (Exception ex) {
            Logger.getLogger(GenericFileReader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
