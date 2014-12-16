package com.cryptic.imed.service;

import com.cryptic.imed.app.DbHelper;
import com.cryptic.imed.domain.PrescriptionMedicine;
import com.cryptic.imed.util.DateUtils;
import com.cryptic.imed.util.DateWithoutTime;
import com.google.inject.Singleton;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * @author sharafat
 */
@Singleton
public class SqlQueryService {
    private static final Logger log = LoggerFactory.getLogger(SqlQueryService.class);

    private final RuntimeExceptionDao<PrescriptionMedicine, Integer> prescriptionMedicineDao;

    public SqlQueryService() {
        prescriptionMedicineDao = DbHelper.getHelper().getRuntimeExceptionDao(PrescriptionMedicine.class);
    }

    public GenericRawResults<PrescriptionMedicine> listPrescriptionMedicinesHavingSchedulesBetweenDates(
            DateWithoutTime startDate, DateWithoutTime endDate) {
        return listPrescriptionMedicinesHavingSchedulesBetweenDates(
                DateUtils.getSqliteFormattedDate(startDate), DateUtils.getSqliteFormattedDate(endDate));
    }

    public GenericRawResults<PrescriptionMedicine> listPrescriptionMedicinesHavingSchedulesBetweenDates(
            String sqliteFormattedStartDate, String sqliteFormattedEndDate) {
        return prescriptionMedicineDao.queryRaw(
                "select id, " + getEndDateColumn() + " " +
                        "from prescriptionmedicine " +
                        "where date(startDate) <= ? and date(endDate) >= ?",
                new RawRowMapper<PrescriptionMedicine>() {
                    @Override
                    public PrescriptionMedicine mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
                        log.debug("prescriptionMedicines id: {} endDate: {}", resultColumns[0], resultColumns[1]);
                        return prescriptionMedicineDao.queryForId(Integer.parseInt(resultColumns[0]));
                    }
                },
                sqliteFormattedEndDate,
                sqliteFormattedStartDate
        );
    }

    private String getEndDateColumn() {
        return getEndDateSelectionQueryPart() + " as endDate";
    }

    private String getEndDateSelectionQueryPart() {
        return "date(startDate, '+' || ((totalDaysToTake - 1) * (dayInterval + 1)) || 'day')";
    }

    public DateWithoutTime getClosestPrescriptionMedicineStartDateTo(DateWithoutTime date) {
        return getClosestPrescriptionMedicineStartDateTo(DateUtils.getSqliteFormattedDate(date));
    }

    public DateWithoutTime getClosestPrescriptionMedicineStartDateTo(String sqliteFormattedDate) {
        try {
            String startDateColumnName = "startDate";
            List<PrescriptionMedicine> query = prescriptionMedicineDao.queryBuilder()
                    .orderBy(startDateColumnName, true)
                    .limit(1L)
                    .where().raw("date(" + startDateColumnName + ") >= " + sqliteFormattedDate)//.ge(startDateColumnName, date.asDate())
                    .query();

            return query.size() > 0 ? new DateWithoutTime(query.get(0).getStartDate()) : null;
        } catch (SQLException e) {
            log.error("Error in querying for closest PrescriptionMedicine startDate for date: " + sqliteFormattedDate, e);
            return null;
        }
    }

    public DateWithoutTime getFurthestPrescriptionMedicineEndDateFrom(DateWithoutTime date) {
        return getFurthestPrescriptionMedicineEndDateFrom(DateUtils.getSqliteFormattedDate(date));
    }

    public DateWithoutTime getFurthestPrescriptionMedicineEndDateFrom(String sqliteFormattedDate) {
        try {
            String endDateColumnName = "endDate";
            List<PrescriptionMedicine> query = prescriptionMedicineDao.queryBuilder()
                    .selectColumns(getEndDateColumn())
                    .orderBy(endDateColumnName, false)
                    .limit(1L)
                    .where().raw("date(" + endDateColumnName + ") >= " + sqliteFormattedDate)//.ge(endDateColumnName, sqliteFormattedDate)
                    .query();

            return query.size() > 0 ? new DateWithoutTime(query.get(0).getEndDate()) : null;
        } catch (SQLException e) {
            log.error("Error in querying for furthest PrescriptionMedicine endDate from date: " + sqliteFormattedDate, e);
            return null;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        DbHelper.release();
    }
}
