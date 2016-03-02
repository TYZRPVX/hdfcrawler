package edu.hit.ehealth.main.vo.patient;

import edu.hit.ehealth.main.vo.AbstractVOMD5;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "H_HYHWB")
public class ClubThread extends AbstractVOMD5 {

    private String topicID;
    private String homepageID;
    private String postDate;
    private String content;
    private String publisher;
    private String topicTitle;


    @Column(name = "htid")
    public String getTopicID() {
        return topicID;
    }

    public void setTopicID(String topicID) {
        this.topicID = topicID;
    }


    @Column(name = "grzyid")
    public String getHomepageID() {
        return homepageID;
    }

    public void setHomepageID(String homepageID) {
        this.homepageID = homepageID;
    }


    @Column(name = "sj")
    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    @Column(name = "content", columnDefinition = "TEXT")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "name")
    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Column(name = "title")
    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }
}
