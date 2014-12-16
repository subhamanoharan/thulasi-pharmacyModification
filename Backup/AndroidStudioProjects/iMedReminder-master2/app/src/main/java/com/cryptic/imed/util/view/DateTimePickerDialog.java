package com.cryptic.imed.util.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;

/**
 * @author sharafat
 */
public class DateTimePickerDialog extends AlertDialog implements DialogInterface.OnClickListener,
        DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener {

    /**
     * The callback interface used to indicate the user is done filling in
     * the date and time (they clicked on the 'Set' button).
     */
    public interface OnDateTimeSetListener {

        /**
         * @param datePickerView The DatePicker view associated with this listener.
         * @param timePickerView The TimePicker view associated with this listener.
         * @param year           The year that was set.
         * @param monthOfYear    The month that was set (0-11) for compatibility
         *                       with {@link java.util.Calendar}.
         * @param dayOfMonth     The day of the month that was set.
         * @param hourOfDay      The hour that was set.
         * @param minute         The minute that was set.
         */
        void onDateTimeSet(DatePicker datePickerView, TimePicker timePickerView,
                           int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute);
    }

    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String DAY = "day";
    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";
    private static final String IS_24_HOUR = "is24hour";

    private static final CharSequence TITLE = "Set date & time";
    private static final CharSequence SET = "Set";
    private static final CharSequence CANCEL = "Cancel";

    private static final int DATE_PICKER_VIEW_ID = 100;
    private static final int TIME_PICKER_VIEW_ID = 200;

    private final DatePicker mDatePicker;
    private final TimePicker mTimePicker;
    private final OnDateTimeSetListener mCallBack;

    private int mDialogTitleResId;
    private int mDialogSetButtonTextResId;
    private int mDialogCancelButtonTextResId;

    /**
     * @param context      The context the dialog is to run in.
     * @param theme        the theme to apply to this dialog
     * @param callBack     How the parent is notified that the date is set.
     * @param year         The initial year of the dialog.
     * @param monthOfYear  The initial month of the dialog. (Starts from 0)
     * @param dayOfMonth   The initial day of the dialog.
     * @param hourOfDay    The initial hour.
     * @param minute       The initial minute.
     * @param is24HourView Whether this is a 24 hour view, or AM/PM.
     */
    public DateTimePickerDialog(Context context, int theme, OnDateTimeSetListener callBack,
                                int year, int monthOfYear, int dayOfMonth,
                                int hourOfDay, int minute, boolean is24HourView) {
        super(context, theme);

        mCallBack = callBack;

        Context themeContext = getContext();
        setButton(BUTTON_POSITIVE, mDialogSetButtonTextResId != 0
                ? themeContext.getText(mDialogSetButtonTextResId) : SET, this);
        setButton(BUTTON_NEGATIVE, mDialogCancelButtonTextResId != 0
                ? themeContext.getText(mDialogCancelButtonTextResId) : CANCEL, (OnClickListener) null);
        setIcon(0);
        setTitle(mDialogTitleResId != 0 ? themeContext.getText(mDialogTitleResId) : TITLE);

        View view = getDialogView();
        setView(view);

        mDatePicker = (DatePicker) view.findViewById(DATE_PICKER_VIEW_ID);
        mTimePicker = (TimePicker) view.findViewById(TIME_PICKER_VIEW_ID);

        mDatePicker.init(year, monthOfYear, dayOfMonth, this);
        mTimePicker.setCurrentHour(hourOfDay);
        mTimePicker.setCurrentMinute(minute);
        mTimePicker.setIs24HourView(is24HourView);
        mTimePicker.setOnTimeChangedListener(this);
    }

    private View getDialogView() {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        DatePicker datePicker = new DatePicker(getContext());
        datePicker.setId(DATE_PICKER_VIEW_ID);

        TimePicker timePicker = new TimePicker(getContext());
        timePicker.setId(TIME_PICKER_VIEW_ID);

        linearLayout.addView(datePicker);
        linearLayout.addView(timePicker);

        return linearLayout;
    }

    /**
     * @param context      The context the dialog is to run in.
     * @param callBack     How the parent is notified that the date is set.
     * @param year         The initial year of the dialog.
     * @param monthOfYear  The initial month of the dialog. (Starts from 0)
     * @param dayOfMonth   The initial day of the dialog.
     * @param hourOfDay    The initial hour.
     * @param minute       The initial minute.
     * @param is24HourView Whether this is a 24 hour view, or AM/PM.
     */
    public DateTimePickerDialog(Context context, OnDateTimeSetListener callBack,
                                int year, int monthOfYear, int dayOfMonth,
                                int hourOfDay, int minute, boolean is24HourView) {
        this(context, 0, callBack, year, monthOfYear, dayOfMonth, hourOfDay, minute, is24HourView);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (mCallBack != null) {
            mDatePicker.clearFocus();
            mCallBack.onDateTimeSet(mDatePicker, mTimePicker,
                    mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth(),
                    mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute());
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mDatePicker.init(year, monthOfYear, dayOfMonth, null);
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        /* do nothing */
    }

    /**
     * Sets the current date and time.
     *
     * @param year         The date year.
     * @param monthOfYear  The date month.
     * @param dayOfMonth   The date day of month.
     * @param hourOfDay    The hour.
     * @param minuteOfHour The minute.
     */
    public void updateDateTime(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour) {
        mDatePicker.updateDate(year, monthOfYear, dayOfMonth);
        mTimePicker.setCurrentHour(hourOfDay);
        mTimePicker.setCurrentMinute(minuteOfHour);
    }

    /**
     * Gets the {@link DatePicker} contained in this dialog.
     *
     * @return The DatePicker view.
     */
    public DatePicker getDatePicker() {
        return mDatePicker;
    }

    /**
     * Gets the {@link TimePicker} contained in this dialog.
     *
     * @return The TimePicker view.
     */
    public TimePicker getTimePicker() {
        return mTimePicker;
    }

    public void setDialogTitleResId(int dialogTitleResId) {
        this.mDialogTitleResId = dialogTitleResId;
    }

    public void setDialogSetButtonTextResId(int dialogSetButtonTextResId) {
        this.mDialogSetButtonTextResId = dialogSetButtonTextResId;
    }

    public void setDialogCancelButtonTextResId(int dialogCancelButtonTextResId) {
        this.mDialogCancelButtonTextResId = dialogCancelButtonTextResId;
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();

        state.putInt(YEAR, mDatePicker.getYear());
        state.putInt(MONTH, mDatePicker.getMonth());
        state.putInt(DAY, mDatePicker.getDayOfMonth());
        state.putInt(HOUR, mTimePicker.getCurrentHour());
        state.putInt(MINUTE, mTimePicker.getCurrentMinute());
        state.putBoolean(IS_24_HOUR, mTimePicker.is24HourView());

        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        int year = savedInstanceState.getInt(YEAR);
        int month = savedInstanceState.getInt(MONTH);
        int day = savedInstanceState.getInt(DAY);
        int hour = savedInstanceState.getInt(HOUR);
        int minute = savedInstanceState.getInt(MINUTE);
        boolean is24HourView = savedInstanceState.getBoolean(IS_24_HOUR);

        mDatePicker.init(year, month, day, this);
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minute);
        mTimePicker.setIs24HourView(is24HourView);
    }
}
