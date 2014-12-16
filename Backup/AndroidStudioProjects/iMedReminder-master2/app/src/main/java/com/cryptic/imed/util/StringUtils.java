package com.cryptic.imed.util;

import android.widget.EditText;

/**
 * @author sharafat
 */
public class StringUtils {
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static String getNonEmptyString(String str, String defaultValueIfStrIsEmpty) {
        return isEmpty(str) ? defaultValueIfStrIsEmpty : str;
    }

    public static String dropDecimalIfRoundNumber(float val) {
        if (val == 0) {
            return "0";
        } else if (val % (int) val == 0) {
            return Integer.toString((int) val);
        } else {
            return Float.toString(val);
        }
    }

    public static int parseInt(EditText editText) {
        return parseInt(editText, 0);
    }

    public static int parseInt(EditText editText, int defaultValueIfInputIsEmpty) {
        String inputText = editText.getText().toString();
        return "".equals(inputText) ? defaultValueIfInputIsEmpty : Integer.parseInt(inputText);
    }

    public static float parseFloat(EditText editText) {
        return parseFloat(editText, 0);
    }

    public static float parseFloat(EditText editText, float defaultValueIfInputIsEmpty) {
        String inputText = editText.getText().toString();
        return "".equals(inputText) ? defaultValueIfInputIsEmpty : Float.parseFloat(inputText);
    }

    public static double parseDouble(EditText editText) {
        return parseDouble(editText, 0);
    }

    public static double parseDouble(EditText editText, double defaultValueIfInputIsEmpty) {
        String inputText = editText.getText().toString();
        return "".equals(inputText) ? defaultValueIfInputIsEmpty : Double.parseDouble(inputText);
    }
}
