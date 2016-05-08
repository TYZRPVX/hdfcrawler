package edu.hit.ehealth.main.vo.doctor;

import edu.hit.ehealth.main.vo.AbstractVOMD5;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table()
public class DoctorExperience extends AbstractVOMD5 {

    private String personInfoID;
    private String fromCity;
    private String disease;
    private String recordDate;
    private String effect;
    private String attitude;
    private String cureMethod;
    private String cureProcess;
    private String recommandNum;
    private String replyNum;

    @Column(name = "grxxid")
    public String getPersonInfoID() {
        return personInfoID;
    }

    public void setPersonInfoID(String personInfoID) {
        this.personInfoID = personInfoID;
    }


    @Column(name = "hzlz")
    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    @Column(name = "jb")
    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    @Column(name = "jlsj")
    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    @Column(name = "lx")
    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    @Column(name = "td")
    public String getAttitude() {
        return attitude;
    }

    public void setAttitude(String attitude) {
        this.attitude = attitude;
    }

    @Column(name = "zlfs")
    public String getCureMethod() {
        return cureMethod;
    }

    public void setCureMethod(String cureMethod) {
        this.cureMethod = cureMethod;
    }

    @Column(name = "kbgc", columnDefinition = "TEXT")
    public String getCureProcess() {
        return cureProcess;
    }

    public void setCureProcess(String cureProcess) {
        this.cureProcess = cureProcess;
    }

    @Column(name = "ckjzs")
    public String getRecommandNum() {
        return recommandNum;
    }

    public void setRecommandNum(String recommandNum) {
        this.recommandNum = recommandNum;
    }

    @Column(name = "hys")
    public String getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(String replyNum) {
        this.replyNum = replyNum;
    }
}

