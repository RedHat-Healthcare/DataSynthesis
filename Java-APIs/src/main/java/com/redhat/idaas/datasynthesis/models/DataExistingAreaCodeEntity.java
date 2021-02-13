package com.redhat.idaas.datasynthesis.models;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "dataexisting_areacode", schema = "datasynthesis", catalog = "")
public class DataExistingAreaCodeEntity extends io.quarkus.hibernate.orm.panache.PanacheEntityBase {
    private long areaCodeId;
    private String areaCodeValue;
    private Timestamp createdDate;
    private String createdUser;
    private RefDataStatusEntity status;
    private RefDataApplicationEntity registeredApp;
    private RefDataTimeZonesEntity timeZone;
    private RefDataUsStatesEntity state;

    @Id
    @GeneratedValue
    @Column(name = "AreaCodeID", nullable = false)
    public long getAreaCodeId() {
        return areaCodeId;
    }

    public void setAreaCodeId(long areaCodeId) {
        this.areaCodeId = areaCodeId;
    }

    @Basic
    @Column(name = "AreaCodeValue", nullable = false, length = 3)
    public String getAreaCodeValue() {
        return areaCodeValue;
    }

    public void setAreaCodeValue(String areaCodeValue) {
        this.areaCodeValue = areaCodeValue;
    }

    @Basic
    @Column(name = "CreatedDate", nullable = true)
    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    @Basic
    @Column(name = "CreatedUser", nullable = true, length = 20)
    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataExistingAreaCodeEntity that = (DataExistingAreaCodeEntity) o;

        if (areaCodeId != that.areaCodeId) return false;
        if (areaCodeValue != null ? !areaCodeValue.equals(that.areaCodeValue) : that.areaCodeValue != null)
            return false;
        if (createdDate != null ? !createdDate.equals(that.createdDate) : that.createdDate != null) return false;
        if (createdUser != null ? !createdUser.equals(that.createdUser) : that.createdUser != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (timeZone != null ? !timeZone.equals(that.timeZone) : that.timeZone != null) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        if (registeredApp != null ? !registeredApp.equals(that.registeredApp) : that.registeredApp != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (areaCodeId ^ (areaCodeId >>> 32));
        result = 31 * result + (areaCodeValue != null ? areaCodeValue.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (createdUser != null ? createdUser.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (timeZone != null ? timeZone.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (registeredApp != null ? registeredApp.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "TimeZone", referencedColumnName = "TimeZoneValue")
    public RefDataTimeZonesEntity getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(RefDataTimeZonesEntity timeZone) {
        this.timeZone = timeZone;
    }

    @ManyToOne
    @JoinColumn(name = "StateCode", referencedColumnName = "StateID")
    public RefDataUsStatesEntity getState() {
        return state;
    }

    public void setState(RefDataUsStatesEntity state) {
        this.state = state;
    }

    @ManyToOne
    @JoinColumn(name = "StatusID", referencedColumnName = "StatusID")
    public RefDataStatusEntity getStatus() {
        return status;
    }

    public void setStatus(RefDataStatusEntity status) {
        this.status = status;
    }

    @ManyToOne
    @JoinColumn(name = "RegisteredApp", referencedColumnName = "AppGUID")
    public RefDataApplicationEntity getRegisteredApp() {
        return registeredApp;
    }

    public void setRegisteredApp(RefDataApplicationEntity registeredApp) {
        this.registeredApp = registeredApp;
    }

    public static List<DataExistingAreaCodeEntity> findByStatusId(Short statusId) {
        return find("status", new RefDataStatusEntity(statusId)).list();
    }
}
