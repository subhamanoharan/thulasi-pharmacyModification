package com.cryptic.imed.common;

/**
 * @author sharafat
 */
public interface Constants {
    int PHOTO_SIZE = 64;

    int ID_CONTEXT_MENU_EDIT = 101;
    int ID_CONTEXT_MENU_DELETE = 102;

    int ID_OPTIONS_MENU_ADD = 201;

    String GENERAL_DATE_FORMAT = "EEE MMM dd, yyyy";
    String PRESCRIPTION_DETAILS_DATE_FORMAT = "dd MMMM yyyy";
    String GENERAL_TIME_FORMAT = "hh:mm a";
    String GENERAL_DATE_TIME_FORMAT = GENERAL_DATE_FORMAT + " " + GENERAL_TIME_FORMAT;
}
