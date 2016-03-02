package edu.hit.ehealth.main.vo.patient;

import edu.hit.ehealth.main.vo.AbstractVOMD5;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "H_HZFWQLB")
public class PatientServiceArea extends AbstractVOMD5 {

    private String homePageID;
    private String lastPostDate;
    private Integer doctorTalkNum;
    private Integer patientTalkNum;
    private String relevantDis;
    private String title;
    private String tags;

    @Column(name = "grzyid")
    public String getHomePageID() {
        return homePageID;
    }

    public void setHomePageID(String homePageID) {
        this.homePageID = homePageID;
    }


    @Column(name = "zhfb")
    public String getLastPostDate() {
        return lastPostDate;
    }

    public void setLastPostDate(String lastPostDate) {
        this.lastPostDate = lastPostDate;
    }

    @Column(name = "dhsy")
    public Integer getDoctorTalkNum() {
        return doctorTalkNum;
    }

    public void setDoctorTalkNum(Integer doctorTalkNum) {
        this.doctorTalkNum = doctorTalkNum;
    }

    @Column(name = "dhsh")
    public Integer getPatientTalkNum() {
        return patientTalkNum;
    }

    public void setPatientTalkNum(Integer patientTalkNum) {
        this.patientTalkNum = patientTalkNum;
    }

    @Column(name = "xgjb")
    public String getRelevantDis() {
        return relevantDis;
    }

    public void setRelevantDis(String relevantDis) {
        this.relevantDis = relevantDis;
    }

    @Column(name = "bt")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "bq")
    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
