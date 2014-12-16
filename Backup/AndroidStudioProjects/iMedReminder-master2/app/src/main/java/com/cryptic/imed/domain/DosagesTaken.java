package com.cryptic.imed.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sharafat
 */
@DatabaseTable
public class DosagesTaken implements Serializable {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, foreign = true, index = true,
            columnDefinition = "integer references dosage(id) on delete cascade")
    private Dosage dosage;

    @DatabaseField
    private Date takenDateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Dosage getDosage() {
        return dosage;
    }

    public void setDosage(Dosage dosage) {
        this.dosage = dosage;
    }

    public Date getTakenDateTime() {
        return takenDateTime;
    }

    public void setTakenDateTime(Date takenDateTime) {
        this.takenDateTime = takenDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DosagesTaken that = (DosagesTaken) o;

        if (id != that.id) return false;
        if (!dosage.equals(that.dosage)) return false;
        if (takenDateTime != null ? !takenDateTime.equals(that.takenDateTime) : that.takenDateTime != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + dosage.hashCode();
        result = 31 * result + (takenDateTime != null ? takenDateTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DosagesTaken{" +
                "id=" + id +
                ", dosage=" + dosage +
                ", takenDateTime=" + takenDateTime +
                '}';
    }
}
