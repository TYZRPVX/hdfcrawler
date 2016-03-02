package edu.hit.ehealth.main.dao.doctor;

import edu.hit.ehealth.main.vo.doctor.DoctorConsultOrder;
import org.springframework.data.repository.CrudRepository;

public interface DoctorConsultOrderDao extends CrudRepository<DoctorConsultOrder, String> {
}
