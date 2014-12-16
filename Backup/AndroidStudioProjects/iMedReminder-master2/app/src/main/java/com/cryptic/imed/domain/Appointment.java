package com.cryptic.imed.domain;

import android.text.format.DateFormat;
import com.cryptic.imed.common.Constants;
import com.cryptic.imed.domain.enums.AppointmentType;
import com.cryptic.imed.domain.enums.TimeIntervalUnit;
import com.cryptic.imed.util.adapter.Filterable;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sharafat
 */
@DatabaseTable
public class Appointment implements Filterable, Serializable {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String title;

    @DatabaseField
    private String details;

    @DatabaseField(useGetSet = true, foreign = true, foreignAutoCreate = true)
    private Doctor doctor;

    @DatabaseField(useGetSet = true, foreign = true, foreignAutoCreate = true)
    private Pharmacy pharmacy;

    @DatabaseField(canBeNull = false)
    private Date time;

    @DatabaseField(canBeNull = false)
    private float remindBefore;

    @DatabaseField(canBeNull = false)
    private TimeIntervalUnit timeIntervalUnit;

    @DatabaseField(canBeNull = false)
    private AppointmentType appointmentType;

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

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
        this.appointmentType = doctor != null ? AppointmentType.DOCTOR : AppointmentType.OTHER;
    }

    public Pharmacy getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
        this.appointmentType = pharmacy != null ? AppointmentType.PHARMACY : AppointmentType.OTHER;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public float getRemindBefore() {
        return remindBefore;
    }

    public void setRemindBefore(float remindBefore) {
        this.remindBefore = remindBefore;
    }

    public TimeIntervalUnit getTimeIntervalUnit() {
        return timeIntervalUnit;
    }

    public void setTimeIntervalUnit(TimeIntervalUnit timeIntervalUnit) {
        this.timeIntervalUnit = timeIntervalUnit;
    }

    public AppointmentType getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(AppointmentType appointmentType) {
        this.appointmentType = appointmentType;
    }

    public String getFormattedAppointmentTime() {
        return time != null ? DateFormat.format(Constants.GENERAL_DATE_TIME_FORMAT, time).toString() : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Appointment that = (Appointment) o;

        if (id != that.id) return false;
        if (Float.compare(that.remindBefore, remindBefore) != 0) return false;
        if (appointmentType != that.appointmentType) return false;
        if (details != null ? !details.equals(that.details) : that.details != null) return false;
        if (doctor != null ? !doctor.equals(that.doctor) : that.doctor != null) return false;
        if (pharmacy != null ? !pharmacy.equals(that.pharmacy) : that.pharmacy != null) return false;
        if (!time.equals(that.time)) return false;
        if (timeIntervalUnit != that.timeIntervalUnit) return false;
        if (!title.equals(that.title)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + title.hashCode();
        result = 31 * result + (details != null ? details.hashCode() : 0);
        result = 31 * result + (doctor != null ? doctor.hashCode() : 0);
        result = 31 * result + (pharmacy != null ? pharmacy.hashCode() : 0);
        result = 31 * result + time.hashCode();
        result = 31 * result + (remindBefore != +0.0f ? Float.floatToIntBits(remindBefore) : 0);
        result = 31 * result + timeIntervalUnit.hashCode();
        result = 31 * result + appointmentType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", details='" + details + '\'' +
                ", doctor=" + doctor +
                ", pharmacy=" + pharmacy +
                ", time=" + time +
                ", remindBefore=" + remindBefore +
                ", timeIntervalUnit=" + timeIntervalUnit +
                ", appointmentType=" + appointmentType +
                '}';
    }

    @Override
    public String getFilterableText() {
        return title;
    }
}
