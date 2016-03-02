package edu.hit.ehealth.main.vo.patient;

import edu.hit.ehealth.main.vo.AbstractVO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ThankLetterPatcher")
public class ThankLetterPatcher extends AbstractVO {

    private String area;
    private String content;

    @Column
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Column
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
