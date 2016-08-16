package com.generic_file_reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author arsalan
 */
public class MASpreadSheetReader implements MAFileReader {

    private final File file;
    private Workbook workbook;
    private Sheet sheet;
    private FileInputStream fileInpStream;
    private FormulaEvaluator formulaEvaluator;
    private final DataFormatter objDefaultFormat;
    private Iterator<Row> rowIterator;

    public MASpreadSheetReader(File file) {
        this.file = file;
        this.objDefaultFormat = new DataFormatter();
    }

    @Override
    public MAFile getFileContent() {
        MAFile maFile = null;
        FileSheet page;

        try {
            int totalSheets = 0;
            if (openWorkbook()) {
                maFile = new MAFile(file.getName());
                totalSheets = getTotalSheets();
                for (int sheetNo = 0; sheetNo < totalSheets; sheetNo++) {
                    setSheet(sheetNo);
                    page = new FileSheet(getCurrentSheetName());

                    rowIterator = getRowIterator();
                    getSheetHeaders(rowIterator, page);

                    getSheetRows(rowIterator, page);

                    maFile.addSheet(page);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                close();
            } catch (IOException ex) {
                Logger.getLogger(MASpreadSheetReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return maFile;
    }

    /**
     * Read sheet contents
     *
     * @param rowIterator
     * @param page
     */
    private void getSheetRows(Iterator<Row> rowIterator, FileSheet page) {
        Row row;
        Iterator<Cell> cellIterator;
        List<String> rowContent;
        Cell cell;

        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            cellIterator = getCellIterator(row);
            rowContent = new ArrayList<>();
            while (cellIterator.hasNext()) {
                cell = cellIterator.next();
                rowContent.add(getCellValue(cell));
            }
            page.addValidContent(rowContent);
        }
    }

    /**
     * Read sheet headers
     *
     * @param rowIterator
     * @param page
     */
    private void getSheetHeaders(Iterator<Row> rowIterator, FileSheet page) {
        Row row;
        Iterator<Cell> cellIterator;
        Cell cell;

        if (rowIterator.hasNext()) {
            row = rowIterator.next();
            cellIterator = getCellIterator(row);
            while (cellIterator.hasNext()) {
                cell = cellIterator.next();
                page.addHeader(getCellValue(cell));
            }
        }
    }

    /**
     * Convert cell value to string
     *
     * @param cell
     * @return
     */
    private String getCellValue(Cell cell) {
        formulaEvaluator.evaluate(cell);
        return objDefaultFormat.formatCellValue(cell, formulaEvaluator);
    }

    /**
     * Open workbook
     *
     * @return
     * @throws Exception
     */
    private boolean openWorkbook() throws Exception {
        boolean isOpen = false;
        try {
            fileInpStream = new FileInputStream(file);
            String fileExtension = Util.getFileExtension(file);
            if (fileExtension.equals(".xls")) {
                workbook = new HSSFWorkbook(fileInpStream);
                formulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
            } else if (fileExtension.equals(".xlsx")) {
                workbook = new XSSFWorkbook(fileInpStream);
                formulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
            } else {
                throw new Exception("Invalid File");
            }
            setSheet(0);
            isOpen = true;
        } catch (Exception e) {
            throw new FileNotFoundException(file.getAbsolutePath() + " not found");
        }
        return isOpen;
    }

    /**
     * Get current sheet name
     *
     * @return
     */
    private String getCurrentSheetName() {
        return sheet.getSheetName();
    }

    /**
     * Get cell iterator
     *
     * @param row
     * @return
     */
    private Iterator<Cell> getCellIterator(Row row) {
        return row.cellIterator();
    }

    /**
     * Get row Iterator
     *
     * @return
     */
    private Iterator<Row> getRowIterator() {
        return sheet.rowIterator();
    }

    /**
     * Total number of sheets in a file
     *
     * @return
     */
    private int getTotalSheets() {
        return workbook.getNumberOfSheets();
    }

    /**
     * Change sheet in a file
     *
     * @param index
     */
    private void setSheet(int index) {
        sheet = workbook.getSheetAt(index);
    }

    /**
     * Close file
     *
     * @throws IOException
     */
    private void close() throws IOException {
        fileInpStream.close();
    }
}
