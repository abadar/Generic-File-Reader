package com.generic_file_reader;

import com.google.gson.Gson;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author arsalan
 */
public class Util {

    public static final List<String> FILE_EXTENSIONS = Arrays.asList(new String[]{".csv", ".xls", ".xlsx"});

    public static final String INVALID_FILE_EXTENSION = "Invalid File Extension. Please use file with extensions: " + FILE_EXTENSIONS;

    /**
     * Function to return extension of a file
     *
     * @param file
     * @return
     */
    public static String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf("."));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Function to validate phone number
     *
     * @param number
     * @return
     */
    public static boolean isValidPhoneNumber(String number) {
        boolean valid = true;
        try {
            number = number.replaceAll(" ", "");
            number = number.replaceAll("\\(", "");
            number = number.replaceAll("\\)", "");
            number = number.replaceAll("-", "");
            int len = number.length();
            if (len >= 10 && len <= 15) {
                if (number.charAt(0) == '+') {
                    number = number.substring(1, len);
                    len--;
                }
                for (int i = 0; i < len && valid; i++) {
                    if ((number.charAt(i) >= '0' && number.charAt(i) <= '9')) {
                        valid = true;
                    } else {
                        valid = false;
                    }
                }
            }
        } catch (Exception e) {
            valid = false;
        }
        return valid;
    }

    /**
     * Function to create groups based on headers of sheets
     *
     * @param mAFile
     * @return
     */
    public static Map<Integer, List<FileSheet>> createGroups(MAFile mAFile) {
        Map<Integer, List<FileSheet>> map = new HashMap();
        List<FileSheet> pages = mAFile.getSheets();
        List<FileSheet> mapList;
        FileSheet page;
        boolean found = false;
        int mapCounter = 1;
        int key = 0;

        mapList = new ArrayList<>();
        mapList.add(pages.get(0));
        map.put(mapCounter, mapList);

        for (int i = 1; i < pages.size(); i++) {
            page = pages.get(i);
            found = false;
            Iterator it = map.entrySet().iterator();
            while (it.hasNext() && !found) {
                Map.Entry pair = (Map.Entry) it.next();
                key = Integer.parseInt(pair.getKey().toString());
                mapList = map.get(key);
                if (page.equals(mapList.get(0))) {
                    mapList.add(page);
                    map.put(key, mapList);
                    found = true;
                }
            }
            if (!found) {
                mapCounter++;
                mapList = new ArrayList<>();
                mapList.add(page);
                map.put(mapCounter, mapList);
            }
        }

        return map;
    }

    /**
     * Function to convert file data into JSON
     *
     * @param mAFile
     * @return String
     */
    public static String getJson(MAFile mAFile) {
        Gson gson = new Gson();
        return gson.toJson(mAFile);
    }

    /**
     * Function to parse JSON data into MAFile object
     *
     * @param json
     * @return MAFile
     */
    public static MAFile parseJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, MAFile.class);
    }
}
