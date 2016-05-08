package edu.hit.ehealth.main.vo.hospital;

import edu.hit.ehealth.main.vo.AbstractVO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table()
public class SpecialityList extends AbstractVO {

    private String specName;

    @Column(name = "name")
    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }
}
