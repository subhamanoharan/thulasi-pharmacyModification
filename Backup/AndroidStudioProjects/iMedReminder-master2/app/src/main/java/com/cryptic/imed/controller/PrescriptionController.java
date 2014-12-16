package com.cryptic.imed.controller;

import com.cryptic.imed.app.DbHelper;
import com.cryptic.imed.domain.Doctor;
import com.cryptic.imed.domain.Dosage;
import com.cryptic.imed.domain.Prescription;
import com.cryptic.imed.domain.PrescriptionMedicine;
import com.cryptic.imed.service.AlarmService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.Collection;
import java.util.List;

/**
 * @author sharafat
 */
@Singleton
public class PrescriptionController {
    private final RuntimeExceptionDao<Prescription, Integer> prescriptionDao;
    private final RuntimeExceptionDao<PrescriptionMedicine, Integer> prescriptionMedicineDao;
    private final RuntimeExceptionDao<Dosage, Integer> dosageDao;
    private final RuntimeExceptionDao<Doctor, Integer> doctorDao;

    @Inject
    private AlarmService alarmService;

    public PrescriptionController() {
        OrmLiteSqliteOpenHelper dbHelper = DbHelper.getHelper();

        prescriptionDao = dbHelper.getRuntimeExceptionDao(Prescription.class);
        prescriptionMedicineDao = dbHelper.getRuntimeExceptionDao(PrescriptionMedicine.class);
        dosageDao = dbHelper.getRuntimeExceptionDao(Dosage.class);
        doctorDao = dbHelper.getRuntimeExceptionDao(Doctor.class);
   }

    public void save(Prescription prescription) {
        prescriptionDao.createOrUpdate(prescription);
        delete(prescription.getMedicines());
        savePrescriptionMedicines(prescription);

        alarmService.setMedicineReminderAlarm();
    }

    private void savePrescriptionMedicines(Prescription prescription) {
        for (PrescriptionMedicine prescriptionMedicine : prescription.getMedicines()) {
            prescriptionMedicineDao.createOrUpdate(prescriptionMedicine);
            saveDosageReminders(prescriptionMedicine);
        }
    }

    private void saveDosageReminders(PrescriptionMedicine prescriptionMedicine) {
        for (Dosage dosage : prescriptionMedicine.getDosageReminders()) {
            dosageDao.createOrUpdate(dosage);
        }
    }

    public int delete(Prescription prescription) {
        return prescriptionDao.delete(prescription);
    }

    public int delete(PrescriptionMedicine prescriptionMedicine) {
        return prescriptionMedicineDao.delete(prescriptionMedicine);
    }

    public int delete(Collection<PrescriptionMedicine> prescriptionMedicines) {
        return prescriptionMedicineDao.delete(prescriptionMedicines);
    }

    public List<Prescription> list() {
        return prescriptionDao.queryForAll();
    }

    public void refresh(Prescription prescription) {
        prescriptionDao.refresh(prescription);
    }

    public void refresh(Doctor doctor) {
        doctorDao.refresh(doctor);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        DbHelper.release();
    }
}
