package edu.hit.ehealth.main.vo.doctor;

import javax.persistence.Column;

public class DoctorDisease {

    private String diseaseID;
    private String doctorID;

    //    @EmbeddedId
    @Column(name = "jbid")
    public String getDiseaseID() {
        return diseaseID;
    }

    public void setDiseaseID(String diseaseID) {
        this.diseaseID = diseaseID;
    }

    @Column(name = "ysid")
    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }
}
