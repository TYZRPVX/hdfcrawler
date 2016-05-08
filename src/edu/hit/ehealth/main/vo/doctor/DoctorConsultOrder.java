package edu.hit.ehealth.main.vo.doctor;

import edu.hit.ehealth.main.vo.AbstractVOMD5;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table()
public class DoctorConsultOrder extends AbstractVOMD5 {

    private String homepageID;
    private String orderNumber;
    private String orderState;
    private String content;
    private String location;

    @Column(name = "grzyid")
    public String getHomepageID() {
        return homepageID;
    }

    public void setHomepageID(String homepageID) {
        this.homepageID = homepageID;
    }


    @Column(name = "ddh")
    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Column(name = "state")
    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    @Column(name = "content", columnDefinition = "TEXT")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "dq")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
