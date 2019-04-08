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
package eu.europa.ec.joinup.tsl.business.dto.notification;

import java.util.List;
import java.util.Set;

import eu.europa.ec.joinup.tsl.business.dto.User;
import eu.europa.ec.joinup.tsl.model.DBUser;

public class UserChangeDTO {

    private String territory;
    private List<DBUser> dbUsers;
    private List<User> users;
    private Set<String> userRemoved;
    private Set<String> userAdded;

    public UserChangeDTO(String territory, List<DBUser> dbUsers, List<User> users, Set<String> userRemoved, Set<String> userAdded) {
        super();
        this.territory = territory;
        this.dbUsers = dbUsers;
        this.users = users;
        this.userRemoved = userRemoved;
        this.userAdded = userAdded;
    }

    public String getTerritory() {
        return territory;
    }

    public void setTerritory(String territory) {
        this.territory = territory;
    }

    public List<DBUser> getDbUsers() {
        return dbUsers;
    }

    public void setDbUsers(List<DBUser> dbUsers) {
        this.dbUsers = dbUsers;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Set<String> getUserRemoved() {
        return userRemoved;
    }

    public void setUserRemoved(Set<String> userRemoved) {
        this.userRemoved = userRemoved;
    }

    public Set<String> getUserAdded() {
        return userAdded;
    }

    public void setUserAdded(Set<String> userAdded) {
        this.userAdded = userAdded;
    }

}
