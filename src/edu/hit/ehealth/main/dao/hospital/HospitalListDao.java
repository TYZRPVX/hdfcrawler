package edu.hit.ehealth.main.dao.hospital;

import edu.hit.ehealth.main.vo.hospital.HospitalList;
import org.springframework.data.repository.CrudRepository;

public interface HospitalListDao extends CrudRepository<HospitalList, String> {
}
