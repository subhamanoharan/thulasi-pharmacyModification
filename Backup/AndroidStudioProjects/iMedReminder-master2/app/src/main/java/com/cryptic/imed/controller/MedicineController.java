package com.cryptic.imed.controller;

import com.cryptic.imed.app.DbHelper;
import com.cryptic.imed.domain.Medicine;
import com.google.inject.Singleton;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

/**
 * @author sharafat
 */
@Singleton
public class MedicineController {
    private final RuntimeExceptionDao<Medicine, Integer> medicineDao;

    public MedicineController() {
        medicineDao = DbHelper.getHelper().getRuntimeExceptionDao(Medicine.class);
    }

    public Dao.CreateOrUpdateStatus save(Medicine medicine) {
        return medicineDao.createOrUpdate(medicine);
    }

    public int delete(Medicine medicine) {
        return medicineDao.delete(medicine);
    }

    public List<Medicine> findByDeleted(boolean deleted) {
        return medicineDao.queryForEq("deleted", deleted);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        DbHelper.release();
    }
}
