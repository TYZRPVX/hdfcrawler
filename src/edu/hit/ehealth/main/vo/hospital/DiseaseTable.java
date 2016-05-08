package edu.hit.ehealth.main.vo.hospital;

import edu.hit.ehealth.main.vo.AbstractVO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table()
public class DiseaseTable extends AbstractVO {

    private String disName;

    private String specID;

    @Column(name = "name")
    public String getDisName() {
        return disName;
    }

    public void setDisName(String disName) {
        this.disName = disName;
    }

    @Column(name = "zkid")
    public String getSpecID() {
        return specID;
    }

    public void setSpecID(String specID) {
        this.specID = specID;
    }
}
