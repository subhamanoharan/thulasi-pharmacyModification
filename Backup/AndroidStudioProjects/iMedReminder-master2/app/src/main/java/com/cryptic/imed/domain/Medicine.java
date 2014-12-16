package com.cryptic.imed.domain;

import com.cryptic.imed.domain.enums.MedicationUnit;
import com.cryptic.imed.util.adapter.Filterable;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author sharafat
 */
@DatabaseTable
public class Medicine implements Filterable, Serializable {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField
    private String details;

    @DatabaseField(canBeNull = false)
    private MedicationUnit medicationUnit;

    @DatabaseField(canBeNull = false)
    private float currentStock;

    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    private byte[] photo;

    @DatabaseField(canBeNull = false)
    private boolean deleted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public MedicationUnit getMedicationUnit() {
        return medicationUnit;
    }

    public void setMedicationUnit(MedicationUnit medicationUnit) {
        this.medicationUnit = medicationUnit;
    }

    public float getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(float currentStock) {
        this.currentStock = currentStock;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Medicine medicine = (Medicine) o;

        if (Float.compare(medicine.currentStock, currentStock) != 0) return false;
        if (deleted != medicine.deleted) return false;
        if (id != medicine.id) return false;
        if (details != null ? !details.equals(medicine.details) : medicine.details != null) return false;
        if (medicationUnit != medicine.medicationUnit) return false;
        if (!name.equals(medicine.name)) return false;
        if (!Arrays.equals(photo, medicine.photo)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + (details != null ? details.hashCode() : 0);
        result = 31 * result + medicationUnit.hashCode();
        result = 31 * result + (currentStock != +0.0f ? Float.floatToIntBits(currentStock) : 0);
        result = 31 * result + (photo != null ? Arrays.hashCode(photo) : 0);
        result = 31 * result + (deleted ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", details='" + details + '\'' +
                ", medicationUnit=" + medicationUnit +
                ", currentStock=" + currentStock +
                ", photo=" + (photo != null ? photo.length : 0) + "bytes" +
                ", deleted=" + deleted +
                '}';
    }

    @Override
    public String getFilterableText() {
        return name;
    }
}
