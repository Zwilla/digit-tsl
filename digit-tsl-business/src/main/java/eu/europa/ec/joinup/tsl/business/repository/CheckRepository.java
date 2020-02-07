package eu.europa.ec.joinup.tsl.business.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import eu.europa.ec.joinup.tsl.model.DBCheck;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

public interface CheckRepository extends CrudRepository<DBCheck, String> {

    List<DBCheck> findByTarget(Tag target);

    List<DBCheck> findAllByOrderByDescription();

    List<DBCheck> findAllByOrderByTarget();

    @Override
    List<DBCheck> findAll();
}
