package edu.hit.ehealth.main.vo.doctor;

import edu.hit.ehealth.main.vo.AbstractVO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "H_YSGRWZRTJ")
public class DoctorHomepageDaily extends AbstractVO {

    private String personInfoID;
    private String lastOnlineDate;
    private Integer yesterVisitCount;
    private Integer yesterReportPatientNum;
    private String homepageID;

    @Column(name = "grxxid")
    public String getPersonInfoID() {
        return personInfoID;
    }

    public void setPersonInfoID(String personInfoID) {
        this.personInfoID = personInfoID;
    }

    @Column(name = "lasttime")
    public String getLastOnlineDate() {
        return lastOnlineDate;
    }

    public void setLastOnlineDate(String lastOnlineDate) {
        this.lastOnlineDate = lastOnlineDate;
    }

    @Column(name = "zrfwl")
    public Integer getYesterVisitCount() {
        return yesterVisitCount;
    }

    public void setYesterVisitCount(Integer yesterVisitCount) {
        this.yesterVisitCount = yesterVisitCount;
    }

    @Column(name = "zrzhbd")
    public Integer getYesterReportPatientNum() {
        return yesterReportPatientNum;
    }

    public void setYesterReportPatientNum(Integer yesterReportPatientNum) {
        this.yesterReportPatientNum = yesterReportPatientNum;
    }

    @Column()
    public String getHomepageID() {
        return homepageID;
    }

    public void setHomepageID(String homepageID) {
        this.homepageID = homepageID;
    }
}
