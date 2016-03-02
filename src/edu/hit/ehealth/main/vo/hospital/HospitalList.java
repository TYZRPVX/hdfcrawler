package edu.hit.ehealth.main.vo.hospital;

import edu.hit.ehealth.main.vo.AbstractVO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class HospitalList extends AbstractVO {

    private String hospitalName;

    private String province;

    private String sp;

    @Column
    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    @Column
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Column
    public String getSp() {
        return sp;
    }

    public void setSp(String sp) {
        this.sp = sp;
    }
}
