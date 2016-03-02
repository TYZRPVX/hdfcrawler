package edu.hit.ehealth.main.vo.doctor;

import edu.hit.ehealth.main.vo.AbstractVOMD5;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "H_LW")
public class DoctorGift extends AbstractVOMD5 {

    private String personInfoID;
    private String date;
    private String content;

    @Column(name = "grxxid")
    public String getPersonInfoID() {
        return personInfoID;
    }

    public void setPersonInfoID(String personInfoID) {
        this.personInfoID = personInfoID;
    }

    @Column(name = "sj")
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Column(name = "content", columnDefinition = "TEXT")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
