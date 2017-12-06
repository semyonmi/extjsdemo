package ru.semyonmi.extjsdemo.dto;

import java.io.Serializable;

public class CompanyDto implements Serializable {

    private Long id;
    private String name;
    private Integer employees;
    private Long countryId;
    private Long parentId;

    public CompanyDto() {
    }

    public CompanyDto(Long id, String name, Integer employees, Long countryId, Long parentId) {
        this.id = id;
        this.name = name;
        this.employees = employees;
        this.countryId = countryId;
        this.parentId = parentId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEmployees() {
        return employees;
    }

    public void setEmployees(Integer employees) {
        this.employees = employees;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CompanyDto that = (CompanyDto) o;

        if (countryId != null ? !countryId.equals(that.countryId) : that.countryId != null) {
            return false;
        }
        if (employees != null ? !employees.equals(that.employees) : that.employees != null) {
            return false;
        }
        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (name != null ? !name.equals(that.name) : that.name != null) {
            return false;
        }
        if (parentId != null ? !parentId.equals(that.parentId) : that.parentId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (employees != null ? employees.hashCode() : 0);
        result = 31 * result + (countryId != null ? countryId.hashCode() : 0);
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CompanyDto{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", employees=" + employees +
               ", countryId=" + countryId +
               ", parentId=" + parentId +
               '}';
    }
}
