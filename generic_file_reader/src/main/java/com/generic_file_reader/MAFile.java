package com.generic_file_reader;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author arsalan
 */
public class MAFile {

    private final String fileName;
    private final List<FileSheet> sheets;

    public MAFile(String fileName) {
        this.fileName = fileName;
        sheets = new ArrayList<>();
    }

    public String getFileName() {
        return fileName;
    }

    public List<FileSheet> getSheets() {
        return sheets;
    }

    public void addSheet(FileSheet page) {
        sheets.add(page);
    }

    public int getNumberOfSheets() {
        return sheets.size();
    }

}
