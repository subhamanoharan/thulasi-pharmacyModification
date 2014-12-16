package com.cryptic.imed.controller;

import android.util.MonthDisplayHelper;
import com.cryptic.imed.app.DbHelper;
import com.cryptic.imed.domain.Dosage;
import com.cryptic.imed.domain.Prescription;
import com.cryptic.imed.domain.PrescriptionMedicine;
import com.cryptic.imed.service.SqlQueryService;
import com.cryptic.imed.util.DateWithoutTime;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author sharafat
 */
@Singleton
public class ScheduleController {
    private static final Logger log = LoggerFactory.getLogger(ScheduleController.class);

    private final RuntimeExceptionDao<Prescription, Integer> prescriptionDao;

    @Inject
    private SqlQueryService sqlQueryService;

    public ScheduleController() {
        prescriptionDao = DbHelper.getHelper().getRuntimeExceptionDao(Prescription.class);
    }

    public void refresh(Prescription prescription) {
        prescriptionDao.refresh(prescription);
    }

    public Map<DateWithoutTime, List<PrescriptionMedicine>> list(int year, int month, int weekStartDay) {
        MonthDisplayHelper monthDisplayHelper = new MonthDisplayHelper(year, month, weekStartDay);

        int firstDayDisplayedOnCalendarView = monthDisplayHelper.getDigitsForRow(0)[0];
        int lastDayDisplayedOnCalendarView = monthDisplayHelper.getDigitsForRow(
                monthDisplayHelper.getRowOf(monthDisplayHelper.getNumberOfDaysInMonth()))[6];
        log.debug("firstDayDisplayedOnCalendarView: {}, lastDayDisplayedOnCalendarView: {}",
                firstDayDisplayedOnCalendarView, lastDayDisplayedOnCalendarView);

        DateWithoutTime calendarStartDate = new DateWithoutTime(year,
                firstDayDisplayedOnCalendarView == 1 ? month : month - 1,
                firstDayDisplayedOnCalendarView);
        DateWithoutTime calendarEndDate = new DateWithoutTime(year,
                lastDayDisplayedOnCalendarView == monthDisplayHelper.getNumberOfDaysInMonth() ? month : month + 1,
                lastDayDisplayedOnCalendarView);

        GenericRawResults<PrescriptionMedicine> prescriptionMedicines =
                sqlQueryService.listPrescriptionMedicinesHavingSchedulesBetweenDates(calendarStartDate, calendarEndDate);

        return prepareSchedules(calendarStartDate, calendarEndDate, prescriptionMedicines);
    }

    private Map<DateWithoutTime, List<PrescriptionMedicine>> prepareSchedules(
            DateWithoutTime calendarStartDate, DateWithoutTime calendarEndDate,
            GenericRawResults<PrescriptionMedicine> prescriptionMedicines) {

        Map<DateWithoutTime, List<PrescriptionMedicine>> schedules = new HashMap<DateWithoutTime, List<PrescriptionMedicine>>();

        for (PrescriptionMedicine prescriptionMedicine : prescriptionMedicines) {
            DateWithoutTime startingDate = new DateWithoutTime(prescriptionMedicine.getStartDate());
            DateWithoutTime endingDate = new DateWithoutTime(prescriptionMedicine.getEndDate());
            log.debug("startingDate: {}, endingDate: {}", startingDate, endingDate);

            DateWithoutTime date = startingDate.clone();

            if (startingDate.before(calendarStartDate)) {
                do {
                    date.add(DateWithoutTime.DATE, prescriptionMedicine.getDayInterval() + 1);
                } while (date.before(calendarStartDate));
            }

            log.debug("date: {}", date);

            do {
                if (!schedules.containsKey(date)) {
                    schedules.put(date.clone(), new ArrayList<PrescriptionMedicine>());
                }

                schedules.get(date).add(prescriptionMedicine);
                date.add(DateWithoutTime.DATE, prescriptionMedicine.getDayInterval() + 1);
            } while (date.beforeOrOn(endingDate) && date.beforeOrOn(calendarEndDate));
        }

        return schedules;
    }

    public Calendar getClosestUpcomingReminderScheduleTime() {
        Map<DateWithoutTime, List<PrescriptionMedicine>> closestUpcomingScheduleDates = getClosestUpcomingScheduleDates();
        return getClosestUpcomingReminderScheduleTime(closestUpcomingScheduleDates);
    }

    public Map<DateWithoutTime, List<PrescriptionMedicine>> getClosestUpcomingScheduleDates() {
        /*
        Algorithm:
        1. closestPrescriptionMedicineStartDate := startDate of PrescriptionMedicine whose start day is on or closest after today.
        2. If there is such prescriptionMedicine startDate
                2.1. return schedules between dates today and closestPrescriptionMedicineStartDate.
        3. Else
                3.1. furthestPrescriptionMedicineEndDate := endDate of PrescriptionMedicine whose end day is on or furthest from today.
                3.2. If there is such prescriptionMedicine endDate
                        3.2.1. return schedules between dates today and furthestPrescriptionMedicineEndDate.
                3.3. Else
                        3.3.1. return nil [i.e. there is no upcoming schedule date]
         */

        DateWithoutTime today = new DateWithoutTime();
        DateWithoutTime closestPrescriptionMedicineStartDate = sqlQueryService.getClosestPrescriptionMedicineStartDateTo(today);

        if (closestPrescriptionMedicineStartDate != null) {
            GenericRawResults<PrescriptionMedicine> prescriptionMedicines =
                    sqlQueryService.listPrescriptionMedicinesHavingSchedulesBetweenDates(today, closestPrescriptionMedicineStartDate);
            return prepareSchedules(today, closestPrescriptionMedicineStartDate, prescriptionMedicines);
        } else {
            DateWithoutTime furthestPrescriptionMedicineEndDate = sqlQueryService.getFurthestPrescriptionMedicineEndDateFrom(today);

            if (furthestPrescriptionMedicineEndDate != null) {
                GenericRawResults<PrescriptionMedicine> prescriptionMedicines =
                        sqlQueryService.listPrescriptionMedicinesHavingSchedulesBetweenDates(today, furthestPrescriptionMedicineEndDate);
                return prepareSchedules(today, furthestPrescriptionMedicineEndDate, prescriptionMedicines);
            } else {
                return null;
            }
        }
    }

    @SuppressWarnings("deprecation")
    private Calendar getClosestUpcomingReminderScheduleTime(Map<DateWithoutTime,
            List<PrescriptionMedicine>> closestUpcomingScheduleDates) {
        List<DateWithoutTime> closestUpcomingDateSet =
                new ArrayList<DateWithoutTime>(new TreeSet<DateWithoutTime>(closestUpcomingScheduleDates.keySet()));
        for (DateWithoutTime date : closestUpcomingDateSet) {
            Date closestUpcomingReminderScheduleTime = null;

            List<PrescriptionMedicine> prescriptionMedicines = closestUpcomingScheduleDates.get(date);
            for (PrescriptionMedicine prescriptionMedicine : prescriptionMedicines) {
                if (prescriptionMedicine.getDosageReminders().size() > 0) {
                    ArrayList<Dosage> dosageList = new ArrayList<Dosage>(prescriptionMedicine.getDosageReminders());
                    Collections.sort(dosageList, new Comparator<Dosage>() {
                        @Override
                        public int compare(Dosage lhs, Dosage rhs) {
                            Date lhsTime = new Date(0, 0, 0, lhs.getTime().getHours(), lhs.getTime().getMinutes());
                            Date rhsTime = new Date(0, 0, 0, rhs.getTime().getHours(), rhs.getTime().getMinutes());

                            if (lhsTime.before(rhsTime)) {
                                return -1;
                            } else if (lhsTime.after(rhsTime)) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }
                    });

                    Dosage dosageWithClosestTime = dosageList.get(0);
                    Date currentClosestTime = new Date(0, 0, 0, dosageWithClosestTime.getTime().getHours(),
                            dosageWithClosestTime.getTime().getMinutes());

                    if (closestUpcomingReminderScheduleTime == null) {
                        closestUpcomingReminderScheduleTime = currentClosestTime;
                        continue;
                    }

                    if (currentClosestTime.before(closestUpcomingReminderScheduleTime)) {
                        closestUpcomingReminderScheduleTime = currentClosestTime;
                    }
                }
            }

            if (closestUpcomingReminderScheduleTime != null) {
                closestUpcomingReminderScheduleTime.setYear(date.getYear());
                closestUpcomingReminderScheduleTime.setMonth(date.getMonth());
                closestUpcomingReminderScheduleTime.setDate(date.getDate());

                Calendar closestUpcomingReminderScheduleTimeAsCalendarObject = Calendar.getInstance();
                closestUpcomingReminderScheduleTimeAsCalendarObject.setTime(closestUpcomingReminderScheduleTime);

                return closestUpcomingReminderScheduleTimeAsCalendarObject;
            }
        }

        return null;    //no closest upcoming reminder schedule found
    }

    public List<PrescriptionMedicine> list(Calendar scheduleTime) {
        List<PrescriptionMedicine> prescriptionMedicines = null;

        //TODO

        return prescriptionMedicines;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        DbHelper.release();
    }
}
