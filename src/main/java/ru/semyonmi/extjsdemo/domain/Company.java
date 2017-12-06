package ru.semyonmi.extjsdemo.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity(name="ed_company")
public class Company implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SeqGen")
    @SequenceGenerator(sequenceName = "sys_main_sq", allocationSize = 1, name = "SeqGen")
    private Long id;
    @Column
    private String name;
    @Column
    private Integer employees;
    @Column(name="country_id")
    private Long countryId;
    @Column(name="parent_id")
    private Long parentId;

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

        Company company = (Company) o;

        if (countryId != null ? !countryId.equals(company.countryId) : company.countryId != null) {
            return false;
        }
        if (employees != null ? !employees.equals(company.employees) : company.employees != null) {
            return false;
        }
        if (id != null ? !id.equals(company.id) : company.id != null) {
            return false;
        }
        if (!name.equals(company.name)) {
            return false;
        }
        if (parentId != null ? !parentId.equals(company.parentId) : company.parentId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + (employees != null ? employees.hashCode() : 0);
        result = 31 * result + (countryId != null ? countryId.hashCode() : 0);
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Company{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", employees=" + employees +
               ", countryId=" + countryId +
               ", parentId=" + parentId +
               '}';
    }
}
