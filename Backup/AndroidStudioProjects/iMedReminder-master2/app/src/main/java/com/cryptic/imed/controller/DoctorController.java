package com.cryptic.imed.controller;

import com.cryptic.imed.app.DbHelper;
import com.cryptic.imed.domain.Doctor;
import com.google.inject.Singleton;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

/**
 * @author sharafat
 */
@Singleton
public class DoctorController {
    private final RuntimeExceptionDao<Doctor, Integer> doctorDao;

    public DoctorController() {
        doctorDao = DbHelper.getHelper().getRuntimeExceptionDao(Doctor.class);
    }

    public Dao.CreateOrUpdateStatus save(Doctor doctor) {
        return doctorDao.createOrUpdate(doctor);
    }

    public int delete(Doctor doctor) {
        return doctorDao.delete(doctor);
    }

    public List<Doctor> findByDeleted(boolean deleted) {
        return doctorDao.queryForEq("deleted", deleted);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        DbHelper.release();
    }
}
