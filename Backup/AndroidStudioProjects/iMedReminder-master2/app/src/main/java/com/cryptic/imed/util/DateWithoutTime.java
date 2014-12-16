package com.cryptic.imed.util;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author sharafat
 */
public class DateWithoutTime implements Cloneable {
    public static final int YEAR = GregorianCalendar.YEAR;
    public static final int MONTH = GregorianCalendar.MONTH;
    public static final int DATE = GregorianCalendar.DATE;

    private int year, month, date;

    public DateWithoutTime() {
        setDate(Calendar.getInstance());
    }

    public DateWithoutTime(int year, int month, int date) {
        setDate(year, month, date);
    }

    public DateWithoutTime(Calendar calendar) {
        setDate(calendar);
    }

    public DateWithoutTime(Date date) {
        setDate(date);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public Calendar getDateAsCalendar() {
        return new GregorianCalendar(year, month, date);
    }

    public Date asDate() {
        return getDateAsCalendar().getTime();
    }

    public void setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.date = day;
    }

    public void setDate(Calendar calendar) {
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        date = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @SuppressWarnings("deprecation")
    public void setDate(Date date) {
        year = date.getYear() + 1900;
        month = date.getMonth();
        this.date = date.getDate();
    }

    public CharSequence format(String format) {
        return DateFormat.format(format, asDate());
    }

    public boolean before(DateWithoutTime dateWithoutTime) {
        return timeInMillis() < dateWithoutTime.timeInMillis();
    }

    public boolean beforeOrOn(DateWithoutTime dateWithoutTime) {
        return equals(dateWithoutTime) || before(dateWithoutTime);
    }

    public boolean after(DateWithoutTime dateWithoutTime) {
        return timeInMillis() > dateWithoutTime.timeInMillis();
    }

    public boolean onOrAfter(DateWithoutTime dateWithoutTime) {
        return equals(dateWithoutTime) || after(dateWithoutTime);
    }

    public long timeInMillis() {
        return new GregorianCalendar(year, month, date).getTimeInMillis();
    }

    public void add(int field, int value) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month, date);
        gregorianCalendar.add(field, value);

        year = gregorianCalendar.get(Calendar.YEAR);
        month = gregorianCalendar.get(Calendar.MONTH);
        date = gregorianCalendar.get(Calendar.DATE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DateWithoutTime that = (DateWithoutTime) o;

        if (date != that.date) return false;
        if (month != that.month) return false;
        if (year != that.year) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + month;
        result = 31 * result + date;
        return result;
    }

    @Override
    public String toString() {
        return "DateWithoutTime{" +
                "year=" + year +
                ", month=" + month +
                ", date=" + date +
                '}';
    }

    @Override
    public DateWithoutTime clone() {
        return new DateWithoutTime(year, month, date);
    }
}
