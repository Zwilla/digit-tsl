package eu.europa.ec.joinup.tsl.business.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.UserRole;
import eu.europa.ec.joinup.tsl.business.repository.RoleRepository;
import eu.europa.ec.joinup.tsl.model.DBRole;

/**
 * Get list of user roles
 */
@Service
@Transactional(value = TxType.REQUIRED)
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Get user roles stored in database
     */
    public List<UserRole> getRoles() {
        List<UserRole> roleList = new ArrayList<>();
        List<DBRole> dbList = (List<DBRole>) roleRepository.findAll();
        for (DBRole dbRole : dbList) {
            roleList.add(new UserRole(dbRole));
        }
        return roleList;

    }

}
