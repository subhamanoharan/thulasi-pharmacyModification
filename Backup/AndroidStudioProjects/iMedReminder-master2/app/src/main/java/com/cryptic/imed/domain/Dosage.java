package com.cryptic.imed.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sharafat
 */
@DatabaseTable
public class Dosage implements Serializable {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, foreign = true, index = true,
            columnDefinition = "integer references prescriptionmedicine(id) on delete cascade")
    private PrescriptionMedicine prescriptionMedicine;

    @DatabaseField(canBeNull = false)
    private int doseNo;

    @DatabaseField(canBeNull = false)
    private float quantity = 1;

    @DatabaseField(canBeNull = false)
    private Date time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PrescriptionMedicine getPrescriptionMedicine() {
        return prescriptionMedicine;
    }

    public void setPrescriptionMedicine(PrescriptionMedicine prescriptionMedicine) {
        this.prescriptionMedicine = prescriptionMedicine;
    }

    public int getDoseNo() {
        return doseNo;
    }

    public void setDoseNo(int doseNo) {
        this.doseNo = doseNo;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dosage dosage = (Dosage) o;

        if (doseNo != dosage.doseNo) return false;
        if (id != dosage.id) return false;
        if (Float.compare(dosage.quantity, quantity) != 0) return false;
        if (!prescriptionMedicine.equals(dosage.prescriptionMedicine)) return false;
        if (!time.equals(dosage.time)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + prescriptionMedicine.hashCode();
        result = 31 * result + doseNo;
        result = 31 * result + (quantity != +0.0f ? Float.floatToIntBits(quantity) : 0);
        result = 31 * result + time.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Dosage{" +
                "id=" + id +
                "prescriptionMedicineId=" + (prescriptionMedicine != null ? prescriptionMedicine.getId() : null) +
                ", doseNo=" + doseNo +
                ", quantity=" + quantity +
                ", time=" + time +
                '}';
    }
}
