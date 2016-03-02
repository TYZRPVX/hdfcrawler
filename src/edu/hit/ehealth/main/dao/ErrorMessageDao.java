package edu.hit.ehealth.main.dao;

import edu.hit.ehealth.main.vo.ErrorMessage;
import org.springframework.data.repository.CrudRepository;

public interface ErrorMessageDao extends CrudRepository<ErrorMessage, String> {
}
