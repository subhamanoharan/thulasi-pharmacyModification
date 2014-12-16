package com.cryptic.imed.controller;

import com.cryptic.imed.app.DbHelper;
import com.cryptic.imed.domain.Pharmacy;
import com.google.inject.Singleton;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

/**
 * @author sharafat
 */
@Singleton
public class PharmacyController {
    private final RuntimeExceptionDao<Pharmacy, Integer> pharmacyDao;

    public PharmacyController() {
        pharmacyDao = DbHelper.getHelper().getRuntimeExceptionDao(Pharmacy.class);
    }

    public Dao.CreateOrUpdateStatus save(Pharmacy pharmacy) {
        return pharmacyDao.createOrUpdate(pharmacy);
    }

    public int delete(Pharmacy pharmacy) {
        return pharmacyDao.delete(pharmacy);
    }

    public List<Pharmacy> findByDeleted(boolean deleted) {
        return pharmacyDao.queryForEq("deleted", deleted);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        DbHelper.release();
    }
}
