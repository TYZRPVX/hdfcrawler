package edu.hit.ehealth.main.vo.doctor;

import edu.hit.ehealth.main.vo.AbstractVOMD5;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table()
public class DoctorConsultValuation extends AbstractVOMD5 {

    private String homepageID;
    private String chargeStandard;
    private String userFeedback;
    private String callDate;
    private String callDuration;
    private String valuation;

    @Column(name = "grzyid")
    public String getHomepageID() {
        return homepageID;
    }

    public void setHomepageID(String homepageID) {
        this.homepageID = homepageID;
    }

    @Column(name = "sfbz")
    public String getChargeStandard() {
        return chargeStandard;
    }

    public void setChargeStandard(String chargeStandard) {
        this.chargeStandard = chargeStandard;
    }

    @Column(name = "yhfk")
    public String getUserFeedback() {
        return userFeedback;
    }

    public void setUserFeedback(String userFeedback) {
        this.userFeedback = userFeedback;
    }

    @Column(name = "thsj")
    public String getCallDate() {
        return callDate;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }

    @Column(name = "thsc")
    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    @Column(name = "ztpj")
    public String getValuation() {
        return valuation;
    }

    public void setValuation(String valuation) {
        this.valuation = valuation;
    }
}
