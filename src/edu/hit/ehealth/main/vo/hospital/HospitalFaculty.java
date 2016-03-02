package edu.hit.ehealth.main.vo.hospital;

import edu.hit.ehealth.main.vo.AbstractVO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "H_YYKS")
public class HospitalFaculty extends AbstractVO {

    private String hospitalID;
    private String specID;
    private String facultyName;
    private Integer consultDocNum;
    private Integer addedPatNum;
    private Integer addedDocNum;
    private String facultyIntro;

    @Column(name = "yyid")
    public String getHospitalID() {
        return hospitalID;
    }

    public void setHospitalID(String hospitalID) {
        this.hospitalID = hospitalID;
    }

    @Column(name = "zkid")
    public String getSpecID() {
        return specID;
    }

    public void setSpecID(String specID) {
        this.specID = specID;
    }

    @Column(name = "name")
    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    @Column(name = "DHZXDF")
    public Integer getConsultDocNum() {
        return consultDocNum;
    }

    public void setConsultDocNum(Integer consultDocNum) {
        this.consultDocNum = consultDocNum;
    }

    @Column(name = "JHHZ")
    public Integer getAddedPatNum() {
        return addedPatNum;
    }

    public void setAddedPatNum(Integer addedPatNum) {
        this.addedPatNum = addedPatNum;
    }

    @Column(name = "JHDF")
    public Integer getAddedDocNum() {
        return addedDocNum;
    }

    public void setAddedDocNum(Integer addedDocNum) {
        this.addedDocNum = addedDocNum;
    }

    @Column(name = "KSJS", columnDefinition = "TEXT")
    public String getFacultyIntro() {
        return facultyIntro;
    }

    public void setFacultyIntro(String facultyIntro) {
        this.facultyIntro = facultyIntro;
    }
}
