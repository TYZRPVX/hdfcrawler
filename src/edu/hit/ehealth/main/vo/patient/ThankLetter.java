package edu.hit.ehealth.main.vo.patient;

import edu.hit.ehealth.main.vo.AbstractVOMD5;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "H_GXX")
public class ThankLetter extends AbstractVOMD5 {

    private String personInfoID;
    private String fromCity;
    private String disease;
    private String recordDate;
    private String effect;
    private String attitude;
    private String content;
    private String recommendNum;
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

    @Column(name = "content", columnDefinition = "TEXT")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "ckjzs")
    public String getRecommendNum() {
        return recommendNum;
    }

    public void setRecommendNum(String recommendNum) {
        this.recommendNum = recommendNum;
    }

    @Column(name = "hys")
    public String getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(String replyNum) {
        this.replyNum = replyNum;
    }
}
