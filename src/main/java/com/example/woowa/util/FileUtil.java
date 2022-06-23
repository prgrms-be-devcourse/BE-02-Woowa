package com.example.woowa.util;

import com.example.woowa.common.AreaCode;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static List<AreaCode> parseAreaCodeList() {

        final String fileUrl = "src/main/resources/file/AreaCodeList.txt";
        BufferedReader reader = null;
        String line = null;
        List<AreaCode> areaCodeList = new ArrayList<>();

        try {
            File file = new File(fileUrl);
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "euc-kr"));
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] split = line.split("\t");
                areaCodeList.add(new AreaCode(split[0], split[1], parseAbolish(split[2])));
            }
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return areaCodeList;
    }

    public static boolean parseAbolish(String s) {

        Boolean abolish = null;

        switch (s) {
            case "존재":
                abolish = true;
                break;
            case "폐지":
                abolish = false;
        }

        return Boolean.TRUE.equals(abolish);
    }

}
