package edu.hit.ehealth.main.vo.doctor;


import edu.hit.ehealth.main.vo.AbstractVO;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table()
public class DoctorInfoCenter extends AbstractVO {

    private String visitCount;
    private String visitCountDate; // 妥协这不规律的日期
    private String effect;
    private String attitude;
    private String canConsult;
    private String canPhoneCall;
    private String canAdd;
    private String specID;
    private String officeID;
    private String hospitalID;
    private String allPatients;
    private String sfPatients;
    private String star;

    public String getAllPatients() {
        return allPatients;
    }

    public void setAllPatients(String allPatients) {
        this.allPatients = allPatients;
    }

    public String getSfPatients() {
        return sfPatients;
    }

    public void setSfPatients(String sfPatients) {
        this.sfPatients = sfPatients;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(String visitCount) {
        this.visitCount = visitCount;
    }

    public String getVisitCountDate() {
        return visitCountDate;
    }

    public void setVisitCountDate(String visitCountDate) {
        this.visitCountDate = visitCountDate;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getAttitude() {
        return attitude;
    }

    public void setAttitude(String attitude) {
        this.attitude = attitude;
    }

    public String getCanConsult() {
        return canConsult;
    }

    public void setCanConsult(String canConsult) {
        this.canConsult = canConsult;
    }

    public String getCanPhoneCall() {
        return canPhoneCall;
    }

    public void setCanPhoneCall(String canPhoneCall) {
        this.canPhoneCall = canPhoneCall;
    }

    public String getCanAdd() {
        return canAdd;
    }

    public void setCanAdd(String canAdd) {
        this.canAdd = canAdd;
    }

    public String getSpecID() {
        return specID;
    }

    public void setSpecID(String specID) {
        this.specID = specID;
    }

    public String getOfficeID() {
        return officeID;
    }

    public void setOfficeID(String officeID) {
        this.officeID = officeID;
    }

    public String getHospitalID() {
        return hospitalID;
    }

    public void setHospitalID(String hospitalID) {
        this.hospitalID = hospitalID;
    }
}
