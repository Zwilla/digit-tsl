/*******************************************************************************
 * DIGIT-TSL - Trusted List Manager
 * Copyright (C) 2018 European Commission, provided under the CEF E-Signature programme
 *  
 * This file is part of the "DIGIT-TSL - Trusted List Manager" project.
 *  
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 *  
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package eu.europa.ec.joinup.tsl.business.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.Load;
import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.dto.User;
import eu.europa.ec.joinup.tsl.model.DBUser;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceTest extends AbstractSpringTest {

    @Autowired
    private UserService userService;

    @Autowired
    private TLLoader tlLoader;

    @Before
    public void init() {
        Load load = new Load();
        tlLoader.loadTL("EU", "", TLType.LOTL, TLStatus.PROD, load);
        tlLoader.loadTL("BE", "", TLType.TL, TLStatus.PROD, load);
        tlLoader.loadTL("DE", "", TLType.TL, TLStatus.PROD, load);
        tlLoader.loadTL("UK", "", TLType.TL, TLStatus.PROD, load);
    }

    @Test
    public void AisManagement() {
        Assert.assertTrue(userService.isSuperAdmin("test"));
        Assert.assertFalse(userService.isSuperAdmin("adm"));
    }

    @Test
    public void BisAdmin() {
        Assert.assertTrue(userService.isManagement("adm"));
        Assert.assertFalse(userService.isManagement("psc"));
    }

    @Test
    public void CisLotlSigner() {
        Assert.assertTrue(userService.islotlSealer("testSigner"));
        Assert.assertFalse(userService.islotlSealer("psc"));
    }

    @Test
    public void DisUnsignedNot() {
        Assert.assertTrue(userService.isUnsignedNotifier("not"));
        Assert.assertFalse(userService.isUnsignedNotifier("testAth"));
    }

    @Test
    public void EisLotlManager() {
        Assert.assertTrue(userService.isLotlManager("psc"));
        Assert.assertFalse(userService.isLotlManager("not"));
    }

    @Test
    public void FisAuthenticated() {
        Assert.assertTrue(userService.isAuthenticated("test"));
        Assert.assertTrue(userService.isAuthenticated("adm"));
        Assert.assertTrue(userService.isAuthenticated("testSigner"));
        Assert.assertTrue(userService.isAuthenticated("not"));
        Assert.assertTrue(userService.isAuthenticated("psc"));
        Assert.assertTrue(userService.isAuthenticated("testAth"));
        Assert.assertFalse(userService.isAuthenticated("toto"));
    }

    @Test
    public void GgetUsers() {
        List<User> dbu = userService.getUsersOrderByCountryAndName();
        Assert.assertEquals(11, dbu.size());
    }

    @Test
    public void HgetUser() {
        User user = userService.getUser("not");
        Assert.assertNotNull(user);
        Assert.assertEquals("unsignedNotifier", user.getName());
        Assert.assertNull(userService.getUser("note"));
    }

    @Test
    public void IgetDbUser() {
        DBUser dbUser = userService.getDBUser("adm");
        Assert.assertNotNull(dbUser);
        Assert.assertEquals("Admin", dbUser.getName());
        Assert.assertNull(userService.getDBUser("note"));
    }

    @Test
    public void JaddRole() {
        User user = userService.getUser("test");
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getRole());
        int nbRoles = user.getRole().size();
        userService.addRole(user.getId(), 2);

        User userBis = userService.getUser("test");
        Assert.assertEquals(nbRoles + 1, userBis.getRole().size());
    }

    @Test
    public void KremoveRole() {
        User user = userService.getUser("test");
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getRole());
        int nbRoles = user.getRole().size();
        userService.removeRole(user.getId(), user.getRole().get(0).getId());

        User userBis = userService.getUser("test");
        Assert.assertEquals(nbRoles - 1, userBis.getRole().size());
    }

    @Test
    public void LupdateTerritory() {
        DBUser test = userService.getDBUser("test");
        Assert.assertTrue(userService.updateTerritory("test", "EU"));
        Assert.assertEquals("EU", test.getTerritory().getCodeTerritory());
        Assert.assertFalse(userService.updateTerritory("test", "BBBBB"));
    }

    @Test
    public void MaddUser() {
        Assert.assertEquals(11, userService.getUsersOrderByCountryAndName().size());
        User newUser = userService.addUser("newUser", "LU");
        Assert.assertNotNull(newUser);
        Assert.assertEquals("LU", newUser.getCodeTerritory());
        Assert.assertEquals(12, userService.getUsersOrderByCountryAndName().size());
    }

    @Test
    public void NremoveUser() {
        Assert.assertEquals(12, userService.getUsersOrderByCountryAndName().size());
        Assert.assertTrue(userService.deleteUser(1));
        Assert.assertFalse(userService.deleteUser(9999));
        Assert.assertEquals(11, userService.getUsersOrderByCountryAndName().size());
    }

    @Test
    public void OgetCodeRole() {
        String role = userService.getCodeRole(1);
        Assert.assertEquals("SUP", role);
        role = userService.getCodeRole(9999);
        Assert.assertNull(role);
    }

    @Test
    public void PgetUserATHByTerritory() {
        List<DBUser> users = userService.findAuthenticatedUserByTerritory("BE");
        Assert.assertNotNull(users);
        Assert.assertEquals(1, users.size());
    }

    @Test
    public void QgetUserDifference() {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setEcasId("ath2");
        users.add(user);

        user = new User();
        user.setEcasId("ath3");
        users.add(user);

        user = new User();
        user.setEcasId("ath5");
        users.add(user);
        List<TLDifference> differences = userService.getUserDifference("1", users, "EU");
        Assert.assertNotNull(differences);
        Assert.assertEquals(2, differences.size());
        Assert.assertEquals("ath1", differences.get(0).getPublishedValue());
        Assert.assertEquals("ath5", differences.get(1).getCurrentValue());
    }

    @Test
    @Transactional
    public void RupdateAuthenticatedUser() {
        List<DBUser> dbUsers = userService.findAuthenticatedUserByTerritory("EU");
        Assert.assertEquals(3, dbUsers.size());

        List<User> users = new ArrayList<>();
        for (DBUser dbUser : dbUsers) {
            if (dbUser.getEcasId().equals("ath2")) {
                users.add(new User(dbUser));
            }
        }
        User newUser = new User();
        newUser.setEcasId("ath6");
        users.add(newUser);

        Assert.assertTrue(userService.updateAuthenticatedUser(userService.initUserChange("EU", users)));
        dbUsers = userService.findAuthenticatedUserByTerritory("EU");
        Assert.assertEquals(2, dbUsers.size());
        Assert.assertEquals("ath2", dbUsers.get(0).getEcasId());
        Assert.assertEquals("ath6", dbUsers.get(1).getEcasId());

    }
}
