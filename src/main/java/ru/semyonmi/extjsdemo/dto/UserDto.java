package ru.semyonmi.extjsdemo.dto;

import java.io.Serializable;

public class UserDto implements Serializable {

    private Long id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private boolean isLocked;
    private RoleDto role;
    private int access;

    public UserDto() {
    }

    public UserDto(Long id, String login, String password, String firstName, String lastName, boolean isLocked, RoleDto role, int access) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isLocked = isLocked;
        this.role = role;
        this.access = access;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public RoleDto getRole() {
        return role;
    }

    public void setRole(RoleDto role) {
        this.role = role;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserDto userDto = (UserDto) o;

        if (access != userDto.access) {
            return false;
        }
        if (isLocked != userDto.isLocked) {
            return false;
        }
        if (firstName != null ? !firstName.equals(userDto.firstName) : userDto.firstName != null) {
            return false;
        }
        if (id != null ? !id.equals(userDto.id) : userDto.id != null) {
            return false;
        }
        if (lastName != null ? !lastName.equals(userDto.lastName) : userDto.lastName != null) {
            return false;
        }
        if (!login.equals(userDto.login)) {
            return false;
        }
        if (password != null ? !password.equals(userDto.password) : userDto.password != null) {
            return false;
        }
        if (role != null ? !role.equals(userDto.role) : userDto.role != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + login.hashCode();
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (isLocked ? 1 : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + access;
        return result;
    }

    @Override
    public String toString() {
        return "UserDto{" +
               "id=" + id +
               ", login='" + login + '\'' +
               ", password='" + password + '\'' +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", isLocked=" + isLocked +
               ", role=" + role +
               ", access=" + access +
               '}';
    }
}
