package edu.hit.ehealth.main.dao.hospital;

import edu.hit.ehealth.main.vo.hospital.HospitalInfo;
import org.springframework.data.repository.CrudRepository;

public interface HospitalInfoDao extends CrudRepository<HospitalInfo, String> {
}
