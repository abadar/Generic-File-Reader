package com.generic_file_reader;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author arsalan
 */
public class FileSheet {

    private final String sheetName;
    private final List<String> headers;
    private final List<List<String>> contents;

    public FileSheet(String pageName) {
        this.sheetName = pageName;
        this.headers = new ArrayList<>();
        this.contents = new ArrayList<>();
    }

    public String getSheetName() {
        return sheetName;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void addHeader(String header) {
        this.headers.add(header);
    }

    public List<List<String>> getContents() {
        return contents;
    }

    public void addValidContent(List<String> content) {
        this.contents.add(content);
    }

    public int getNumberOfColumns() {
        return headers.size();
    }

    public int getNumberOfRows() {
        return contents.size();
    }

    @Override
    public boolean equals(Object o) {
        FileSheet filePage = (FileSheet) o;
        int headerSize = this.headers.size();
        boolean isEquals = (headerSize == filePage.headers.size());

        for (int i = 0; i < headerSize && isEquals; i++) {
            isEquals = filePage.headers.contains(this.headers.get(i));
        }

        return isEquals;
    }
}
