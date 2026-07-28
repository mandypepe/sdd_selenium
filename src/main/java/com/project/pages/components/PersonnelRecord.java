package com.project.pages.components;

import java.util.Objects;

/**
 * Immutable value object representing a personnel record.
 */
public class PersonnelRecord {
    private final String id;
    private final String fullName;
    private final String roleTitle;
    private final String department;
    private final String email;
    private final String officeLocation;

    public PersonnelRecord(String id, String fullName, String roleTitle, String department, String email, String officeLocation) {
        this.id = id;
        this.fullName = fullName;
        this.roleTitle = roleTitle;
        this.department = department;
        this.email = email;
        this.officeLocation = officeLocation;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRoleTitle() {
        return roleTitle;
    }

    public String getDepartment() {
        return department;
    }

    public String getEmail() {
        return email;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonnelRecord that = (PersonnelRecord) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PersonnelRecord{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", roleTitle='" + roleTitle + '\'' +
                ", department='" + department + '\'' +
                ", email='" + email + '\'' +
                ", officeLocation='" + officeLocation + '\'' +
                '}';
    }
}
