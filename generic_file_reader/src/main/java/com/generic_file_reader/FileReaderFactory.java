package com.generic_file_reader;

import java.io.File;
import java.util.List;

/**
 *
 * @author arsalan
 */
public class FileReaderFactory {

    private final List<String> fileExtensions;

    public FileReaderFactory() {
        this.fileExtensions = Util.FILE_EXTENSIONS;
    }

    /**
     * Factory method to get respective file reader
     *
     * @param file
     * @return
     * @throws InvalidFileExtensionException
     */
    public MAFileReader getFileReader(File file) throws InvalidFileExtensionException {
        String fileExtension = Util.getFileExtension(file);
        MAFileReader fileReader = null;

        switch (fileExtensions.indexOf(fileExtension)) {
            case 0:
                fileReader = new CsvFileReader(file);
                break;
            case 1:
                fileReader = new MASpreadSheetReader(file);
                break;
            case 2:
                fileReader = new MASpreadSheetReader(file);
                break;
            default:
                throw new InvalidFileExtensionException("Invalid File Extension. Please use file with extensions: " + fileExtensions);
        }

        return fileReader;
    }

}
