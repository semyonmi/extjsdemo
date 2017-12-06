package ru.semyonmi.extjsdemo.dto;

import java.io.Serializable;

public class RoleDto implements Serializable {

    private Long id;
    private String ident;
    private String name;

    public RoleDto() {
    }

    public RoleDto(Long id, String ident, String name) {
        this.id = id;
        this.ident = ident;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
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

        RoleDto roleDto = (RoleDto) o;

        if (id != null ? !id.equals(roleDto.id) : roleDto.id != null) {
            return false;
        }
        if (!ident.equals(roleDto.ident)) {
            return false;
        }
        if (!name.equals(roleDto.name)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + ident.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RoleDto{" +
               "id=" + id +
               ", ident='" + ident + '\'' +
               ", name='" + name + '\'' +
               '}';
    }
}
