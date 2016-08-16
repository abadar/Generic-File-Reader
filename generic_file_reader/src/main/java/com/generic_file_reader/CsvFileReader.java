package com.generic_file_reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arsalan
 */
public class CsvFileReader implements MAFileReader {

    private final File file;

    public CsvFileReader(File file) {
        this.file = file;
    }

    @Override
    public MAFile getFileContent() {
        BufferedReader inp = null;
        MAFile maFile = new MAFile(file.getName());
        try {
            FileSheet page = new FileSheet("Default");
            inp = new BufferedReader(new FileReader(file));
            String line;
            line = inp.readLine();

            addHeaderRow(line, page);

            addRows(inp, page);

            maFile.addSheet(page);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CsvFileReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CsvFileReader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                inp.close();
            } catch (IOException ex) {
                Logger.getLogger(CsvFileReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return maFile;
    }

    private void addRows(BufferedReader inp, FileSheet page) throws IOException {
        String line;
        List<String> list;
        String[] cols;
        int i = 0;

        while ((line = inp.readLine()) != null) {
            list = new ArrayList<>();
            cols = line.split(",");
            //Fetch Columns
            for (i = 0; i < cols.length; i++) {
                list.add(cols[i]);
            }
            page.addValidContent(list);
        }
    }

    private void addHeaderRow(String line, FileSheet page) {
        String[] cols = line.split(",");

        for (String col : cols) {
            page.addHeader(col);
        }
    }

}
