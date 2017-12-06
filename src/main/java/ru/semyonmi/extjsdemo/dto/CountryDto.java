package ru.semyonmi.extjsdemo.dto;

import java.io.Serializable;

public class CountryDto implements Serializable {

    private Long id;
    private String name;

    public CountryDto() {
    }

    public CountryDto(Long id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CountryDto that = (CountryDto) o;

        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (!name.equals(that.name)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CountryDto{" +
               "id=" + id +
               ", name='" + name + '\'' +
               '}';
    }
}
