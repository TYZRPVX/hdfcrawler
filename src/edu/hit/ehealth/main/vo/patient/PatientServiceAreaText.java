package edu.hit.ehealth.main.vo.patient;

import edu.hit.ehealth.main.vo.AbstractVOMD5;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "H_HZFWQWB")
public class PatientServiceAreaText extends AbstractVOMD5 {

    private String queryID;
    private String homepageID;
    private String postDateStr;
    private String content;
    private String orderState;

    @Column(name = "zxid")
    public String getQueryID() {
        return queryID;
    }

    public void setQueryID(String queryID) {
        this.queryID = queryID;
    }

    @Column(name = "grzxid")
    public String getHomepageID() {
        return homepageID;
    }

    public void setHomepageID(String homepageID) {
        this.homepageID = homepageID;
    }

    @Column(name = "time")
    public String getPostDateStr() {
        return postDateStr;
    }

    public void setPostDateStr(String postDateStr) {
        this.postDateStr = postDateStr;
    }

    @Column(name = "content", columnDefinition = "TEXT")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "state")
    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }


}
