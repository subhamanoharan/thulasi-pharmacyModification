package com.cryptic.imed.controller;

import com.cryptic.imed.app.DbHelper;
import com.cryptic.imed.domain.DosagesTaken;
import com.google.inject.Singleton;
import com.j256.ormlite.dao.RuntimeExceptionDao;

/**
 * @author sharafat
 */
@Singleton
public class DosagesTakenController {
    private final RuntimeExceptionDao<DosagesTaken, Integer> dosagesTakenDao;

    public DosagesTakenController() {
        dosagesTakenDao = DbHelper.getHelper().getRuntimeExceptionDao(DosagesTaken.class);
    }

    public DosagesTaken findDosagesTakenByScheduleTime() {
        //TODO
        return null;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        DbHelper.release();
    }
}
