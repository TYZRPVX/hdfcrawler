package edu.hit.ehealth.main.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table()
public class ErrorMessage extends AbstractVO {

    private String errorReason;

    private String crawlerName;

    private String exceptionMsg;


    @Column()
    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }

    @Column(columnDefinition = "TEXT")
    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public void setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }

    @Column()
    public String getCrawlerName() {
        return crawlerName;
    }

    public void setCrawlerName(String crawlerName) {
        this.crawlerName = crawlerName;
    }
}
