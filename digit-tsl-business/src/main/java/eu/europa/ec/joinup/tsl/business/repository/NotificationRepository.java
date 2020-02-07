package eu.europa.ec.joinup.tsl.business.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import eu.europa.ec.joinup.tsl.model.DBNotification;

public interface NotificationRepository extends CrudRepository<DBNotification, Integer> {

    List<DBNotification> findByArchiveFalseOrderByInsertDateDesc();

}
