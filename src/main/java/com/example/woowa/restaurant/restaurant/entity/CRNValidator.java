package com.example.woowa.restaurant.restaurant.entity;

/**
 * From https://gist.github.com/hansonkim/4782acd48d5d703ab8c853a4c1a2015b
 */

public final class CRNValidator {

    private final static int[] LOGIC_NUM = {1, 3, 7, 1, 3, 7, 1, 3, 5, 1};

    public static boolean isValid(String regNum) {
        regNum = regNum.replace("-", "");

        if (!isNumeric(regNum) || regNum.length() != 10)
            return false;

        int sum = 0;
        int j;
        for (int i = 0; i < 9; i++) {
            j = Character.getNumericValue(regNum.charAt(i));
            sum += j * LOGIC_NUM[i];
        }

        sum += Character.getNumericValue(regNum.charAt(8)) * 5 /10;

        int checkNum = (10 - sum % 10) % 10 ;
        return (checkNum == Character.getNumericValue(regNum.charAt(9)));
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
