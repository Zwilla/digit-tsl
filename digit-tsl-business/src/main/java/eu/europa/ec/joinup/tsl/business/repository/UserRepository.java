package eu.europa.ec.joinup.tsl.business.repository;

import java.util.List;

import eu.europa.ec.joinup.tsl.model.DBRole;
import org.springframework.data.repository.CrudRepository;

import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBUser;

public interface UserRepository extends CrudRepository<DBUser, Integer> {

    List<DBUser> findAllByOrderByTerritoryCodeTerritoryAscNameAsc();

    DBUser findByEcasId(String name);

    List<DBUser> findByTerritoryAndRoleCode(DBCountries territory, DBRole role_code);

    DBUser findByEcasIdAndRoleCodeIn(String ecasId, List<String> roles);

    DBUser findByEcasIdAndRoleNotNull(String ecasId);

}
