package ru.semyonmi.extjsdemo.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity(name = "sys_access")
public class Access implements Serializable {

    @Transient
    private static final int NON_ACCESS_MASK=0;
    @Transient
    private static final int READ_MASK=1;
    @Transient
    private static final int WRITE_MASK=2;

    @Id
    @Column(name="user_id")
    Long userId;

    @MapsId
    @OneToOne(mappedBy = "access")
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private int mask;


    public Access() {
    }

    public Access(int mask) {
        this.mask = mask;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

    public boolean canRead() {
        return (mask & READ_MASK) == READ_MASK;
    }

    public boolean canWrite() {
        return (mask & WRITE_MASK) == WRITE_MASK;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Access access = (Access) o;

        if (mask != access.mask) {
            return false;
        }
        if (!user.equals(access.user)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result + mask;
        return result;
    }

    @Override
    public String toString() {
        return "Access{" +
               ", mask=" + mask +
               '}';
    }
}
