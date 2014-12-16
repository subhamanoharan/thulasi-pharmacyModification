package com.cryptic.imed.domain;

import com.cryptic.imed.util.adapter.Filterable;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * @author sharafat
 */
@DatabaseTable
public class Pharmacy implements Filterable, Serializable {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField
    private String phone;

    @DatabaseField
    private String address;

    @DatabaseField
    private String email;

    @DatabaseField
    private String website;

    @DatabaseField
    private String notes;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

        Pharmacy pharmacy = (Pharmacy) o;

        if (deleted != pharmacy.deleted) return false;
        if (id != pharmacy.id) return false;
        if (address != null ? !address.equals(pharmacy.address) : pharmacy.address != null) return false;
        if (email != null ? !email.equals(pharmacy.email) : pharmacy.email != null) return false;
        if (!name.equals(pharmacy.name)) return false;
        if (notes != null ? !notes.equals(pharmacy.notes) : pharmacy.notes != null) return false;
        if (phone != null ? !phone.equals(pharmacy.phone) : pharmacy.phone != null) return false;
        if (website != null ? !website.equals(pharmacy.website) : pharmacy.website != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (website != null ? website.hashCode() : 0);
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        result = 31 * result + (deleted ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Pharmacy{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", website='" + website + '\'' +
                ", notes='" + notes + '\'' +
                ", deleted=" + deleted +
                '}';
    }

    @Override
    public String getFilterableText() {
        return name;
    }
}
