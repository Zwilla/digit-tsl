package eu.europa.ec.joinup.tsl.business.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.UserRole;
import eu.europa.ec.joinup.tsl.business.repository.RoleRepository;
import eu.europa.ec.joinup.tsl.model.DBRole;

public class RoleServiceTest extends AbstractSpringTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @Test
    public void testChangeAvailability() {
        DBRole role = new DBRole();
        roleRepository.save(role);

        long count = roleRepository.count();

        List<UserRole> list = roleService.getRoles();
        assertEquals(count, list.size());
    }
}
