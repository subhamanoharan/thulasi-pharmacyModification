package com.cryptic.imed.controller;

import com.cryptic.imed.app.DbHelper;
import com.cryptic.imed.domain.Appointment;
import com.cryptic.imed.domain.Doctor;
import com.cryptic.imed.domain.Pharmacy;
import com.google.inject.Singleton;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

/**
 * @author sharafat
 */
@Singleton
public class AppointmentController {
    private final RuntimeExceptionDao<Appointment, Integer> appointmentDao;
    private final RuntimeExceptionDao<Doctor, Integer> doctorDao;
    private final RuntimeExceptionDao<Pharmacy, Integer> pharmacyDao;


    public AppointmentController() {
        OrmLiteSqliteOpenHelper dbHelper = DbHelper.getHelper();

        appointmentDao = dbHelper.getRuntimeExceptionDao(Appointment.class);
        doctorDao = dbHelper.getRuntimeExceptionDao(Doctor.class);
        pharmacyDao = dbHelper.getRuntimeExceptionDao(Pharmacy.class);
    }

    public Dao.CreateOrUpdateStatus save(Appointment appointment) {
        return appointmentDao.createOrUpdate(appointment);
    }

    public int delete(Appointment appointment) {
        return appointmentDao.delete(appointment);
    }

    public List<Appointment> list() {
        return appointmentDao.queryForAll();
    }

    public void refresh(Doctor doctor) {
        doctorDao.refresh(doctor);
    }

    public void refresh(Pharmacy pharmacy) {
        pharmacyDao.refresh(pharmacy);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        DbHelper.release();
    }
}
