package edu.hit.ehealth.main.vo.patient;

import edu.hit.ehealth.main.vo.AbstractVOMD5;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table()
public class PatientClub extends AbstractVOMD5 {

    public static final long serialVersionUID = 1L;
    private String homePageID;
    private String lastReplyDate;
    private Integer replyNum;
    private String authorName;
    private String groupName;
    private String title;

    @Column(name = "grzyid")
    public String getHomePageID() {
        return homePageID;
    }

    public void setHomePageID(String homePageID) {
        this.homePageID = homePageID;
    }

    @Column(name = "time", columnDefinition = "TEXT")
    public String getLastReplyDate() {
        return lastReplyDate;
    }

    public void setLastReplyDate(String lastReplyDate) {
        this.lastReplyDate = lastReplyDate;
    }

    @Column(name = "hy")
    public Integer getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(Integer replyNum) {
        this.replyNum = replyNum;
    }

    @Column(name = "writer")
    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @Column(name = "xz")
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
