package com.cryptic.imed.util;

import android.os.Build;
import android.util.Patterns;
import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * @author sharafat
 */
public class Validation {
    public static final String EMAIL_PATTERN = "[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})";
    public static final String URL_PATTERN = "https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    private static final int COMPARISON_EQUAL = 0;
    private static final int COMPARISON_NOT_EQUAL = 1;
    private static final int COMPARISON_LESS_THAN = 2;
    private static final int COMPARISON_LESS_THAN_OR_EQUAL = 3;
    private static final int COMPARISON_GREATER_THAN = 4;
    private static final int COMPARISON_GREATER_THAN_OR_EQUAL = 5;

    public static boolean validateRequired(EditText editText, String errorMsg) {
        if ("".equals(editText.getText().toString().trim())) {
            editText.setError(errorMsg);
            return false;
        } else {
            editText.setError(null);
            return true;
        }
    }

    /*
        WARNING: Returns true if input is empty.
     */
    public static boolean validateRegex(String regex, EditText editText, String errorMsg) {
        if ("".equals(editText.getText().toString())) {
            return true;
        } else if (Pattern.compile(regex).matcher(editText.getText().toString()).matches()) {
            editText.setError(null);
            return true;
        } else {
            editText.setError(errorMsg);
            return false;
        }
    }

    public static boolean validateEmail(EditText editText, String errorMsg) {
        return validateRegex(Build.VERSION.SDK_INT >= 8 ? Patterns.EMAIL_ADDRESS.pattern() : EMAIL_PATTERN,
                editText, errorMsg);
    }

    public static boolean validateUrl(EditText editText, String errorMsg) {
        return validateRegex(Build.VERSION.SDK_INT >= 8 ? Patterns.WEB_URL.pattern() : URL_PATTERN, editText, errorMsg);
    }

    public static boolean validatePasswordsMatch(EditText passwordEditText, EditText confirmPasswordEditText,
                                                 String errorMsg) {
        if (!passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())) {
            passwordEditText.setError(errorMsg);
            passwordEditText.setText("");
            confirmPasswordEditText.setText("");
            return false;
        } else {
            passwordEditText.setError(null);
            return true;
        }
    }

    public static boolean validateValueEquals(double value, EditText editText, String errorMsg) {
        return validateValueEquals(value, editText, errorMsg, null);
    }

    public static boolean validateValueEquals(double value, EditText editText, String errorMsg,
                                                String errorMsgIfInputValueIsEmpty) {
        return validateValueComparison(COMPARISON_EQUAL, value, editText, errorMsg, errorMsgIfInputValueIsEmpty);
    }

    public static boolean validateValueNotEquals(double value, EditText editText, String errorMsg) {
        return validateValueNotEquals(value, editText, errorMsg, null);
    }

    public static boolean validateValueNotEquals(double value, EditText editText, String errorMsg,
                                              String errorMsgIfInputValueIsEmpty) {
        return validateValueComparison(COMPARISON_NOT_EQUAL, value, editText, errorMsg, errorMsgIfInputValueIsEmpty);
    }

    public static boolean validateValueLeassThan(double value, EditText editText, String errorMsg) {
        return validateValueLessThan(value, editText, errorMsg, null);
    }

    public static boolean validateValueLessThan(double value, EditText editText, String errorMsg,
                                                   String errorMsgIfInputValueIsEmpty) {
        return validateValueComparison(COMPARISON_LESS_THAN, value, editText, errorMsg, errorMsgIfInputValueIsEmpty);
    }

    public static boolean validateValueLessThanOrEqual(double value, EditText editText, String errorMsg) {
        return validateValueLessThanOrEqual(value, editText, errorMsg, null);
    }

    public static boolean validateValueLessThanOrEqual(double value, EditText editText, String errorMsg,
                                                          String errorMsgIfInputValueIsEmpty) {
        return validateValueComparison(COMPARISON_LESS_THAN_OR_EQUAL, value, editText, errorMsg,
                errorMsgIfInputValueIsEmpty);
    }

    public static boolean validateValueGreaterThan(double value, EditText editText, String errorMsg) {
        return validateValueGreaterThan(value, editText, errorMsg, null);
    }

    public static boolean validateValueGreaterThan(double value, EditText editText, String errorMsg,
                                                   String errorMsgIfInputValueIsEmpty) {
        return validateValueComparison(COMPARISON_GREATER_THAN, value, editText, errorMsg, errorMsgIfInputValueIsEmpty);
    }

    public static boolean validateValueGreaterThanOrEqual(double value, EditText editText, String errorMsg) {
        return validateValueGreaterThanOrEqual(value, editText, errorMsg, null);
    }

    public static boolean validateValueGreaterThanOrEqual(double value, EditText editText, String errorMsg,
                                                          String errorMsgIfInputValueIsEmpty) {
        return validateValueComparison(COMPARISON_GREATER_THAN_OR_EQUAL, value, editText, errorMsg,
                errorMsgIfInputValueIsEmpty);
    }

    private static boolean validateValueComparison(int comparisonType, double value, EditText editText,
                                                   String errorMsg, String errorMsgIfInputValueIsEmpty) {
        String editTextTrimmedValue = editText.getText().toString().trim();

        if (errorMsgIfInputValueIsEmpty != null && "".equals(editTextTrimmedValue)) {
            return validateRequired(editText, errorMsgIfInputValueIsEmpty);
        }

        switch (comparisonType) {
            case COMPARISON_EQUAL:
                if (Double.parseDouble(editTextTrimmedValue) != value) {
                    editText.setError(errorMsg);
                    return false;
                }
                break;
            case COMPARISON_NOT_EQUAL:
                if (Double.parseDouble(editTextTrimmedValue) == value) {
                    editText.setError(errorMsg);
                    return false;
                }
                break;
            case COMPARISON_LESS_THAN:
                if (Double.parseDouble(editTextTrimmedValue) >= value) {
                    editText.setError(errorMsg);
                    return false;
                }
                break;
            case COMPARISON_LESS_THAN_OR_EQUAL:
                if (Double.parseDouble(editTextTrimmedValue) > value) {
                    editText.setError(errorMsg);
                    return false;
                }
                break;
            case COMPARISON_GREATER_THAN:
                if (Double.parseDouble(editTextTrimmedValue) <= value) {
                    editText.setError(errorMsg);
                    return false;
                }
                break;
            case COMPARISON_GREATER_THAN_OR_EQUAL:
                if (Double.parseDouble(editTextTrimmedValue) < value) {
                    editText.setError(errorMsg);
                    return false;
                }
                break;
        }

        editText.setError(null);
        return true;
    }
}
