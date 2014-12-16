package com.cryptic.imed.domain;

import com.cryptic.imed.util.adapter.Filterable;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * @author sharafat
 */
@DatabaseTable
public class Prescription implements Filterable, Serializable {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String title;

    @DatabaseField
    private String details;

    @DatabaseField(canBeNull = false)
    private Date issueDate;

    @DatabaseField(foreign = true, foreignAutoCreate = true)
    private Doctor prescribedBy;

    @ForeignCollectionField
    private Collection<PrescriptionMedicine> medicines;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Doctor getPrescribedBy() {
        return prescribedBy;
    }

    public void setPrescribedBy(Doctor prescribedBy) {
        this.prescribedBy = prescribedBy;
    }

    public Collection<PrescriptionMedicine> getMedicines() {
        return medicines;
    }

    public void setMedicines(Collection<PrescriptionMedicine> medicines) {
        this.medicines = medicines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Prescription that = (Prescription) o;

        if (id != that.id) return false;
        if (details != null ? !details.equals(that.details) : that.details != null) return false;
        if (!issueDate.equals(that.issueDate)) return false;
        if (medicines != null ? !medicines.equals(that.medicines) : that.medicines != null) return false;
        if (prescribedBy != null ? !prescribedBy.equals(that.prescribedBy) : that.prescribedBy != null) return false;
        if (!title.equals(that.title)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + title.hashCode();
        result = 31 * result + (details != null ? details.hashCode() : 0);
        result = 31 * result + issueDate.hashCode();
        result = 31 * result + (prescribedBy != null ? prescribedBy.hashCode() : 0);
        result = 31 * result + (medicines != null ? medicines.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", details='" + details + '\'' +
                ", issueDate=" + issueDate +
                ", prescribedBy=" + prescribedBy +
                ", medicines=" + medicines +
                '}';
    }

    @Override
    public String getFilterableText() {
        return title;
    }
}
