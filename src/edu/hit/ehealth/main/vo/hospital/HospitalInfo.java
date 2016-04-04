package edu.hit.ehealth.main.vo.hospital;

import edu.hit.ehealth.main.vo.AbstractVOMD5;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "H_YYXX")
public class HospitalInfo extends AbstractVOMD5 {

    private String address;

    private String hospitalIntro;

    private String hospitalName;

    private String hospitalClass;

    private Integer consultDoctorNumber;

    private Integer addedPatientNumber;

    private Integer addedDoctorNumber;

    private Integer officeNumber;

    private Integer doctorNumber;

    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "yyjs", columnDefinition = "TEXT")
    public String getHospitalIntro() {
        return hospitalIntro;
    }

    public void setHospitalIntro(String hospitalIntro) {
        this.hospitalIntro = hospitalIntro;
    }

    @Column(name = "class")
    public String getHospitalClass() {
        return hospitalClass;
    }

    public void setHospitalClass(String hospitalClass) {
        this.hospitalClass = hospitalClass;
    }

    @Column(name = "name")
    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    @Column(name = "dhzxdf")
    public Integer getConsultDoctorNumber() {
        return consultDoctorNumber;
    }

    public void setConsultDoctorNumber(Integer consultDoctorNumber) {
        this.consultDoctorNumber = consultDoctorNumber;
    }

    @Column(name = "jhhz")
    public Integer getAddedPatientNumber() {
        return addedPatientNumber;
    }

    public void setAddedPatientNumber(Integer addedPatientNumber) {
        this.addedPatientNumber = addedPatientNumber;
    }

    @Column(name = "jhdf")
    public Integer getAddedDoctorNumber() {
        return addedDoctorNumber;
    }

    public void setAddedDoctorNumber(Integer addedDoctorNumber) {
        this.addedDoctorNumber = addedDoctorNumber;
    }

    @Column(name = "kss")
    public Integer getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(Integer officeNumber) {
        this.officeNumber = officeNumber;
    }

    @Column(name = "dfs")
    public Integer getDoctorNumber() {
        return doctorNumber;
    }

    public void setDoctorNumber(Integer doctorNumber) {
        this.doctorNumber = doctorNumber;
    }
}
