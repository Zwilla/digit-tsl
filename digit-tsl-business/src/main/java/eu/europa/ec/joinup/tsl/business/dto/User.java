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
package eu.europa.ec.joinup.tsl.business.dto;

import java.util.ArrayList;
import java.util.List;

import eu.europa.ec.joinup.tsl.model.DBRole;
import eu.europa.ec.joinup.tsl.model.DBUser;

public class User {

    private int id;
    private String name;
    private String ecasId;
    private List<UserRole> role;
    private List<TrustBackbonePref> pref;
    private String codeTerritory;

    public User() {
    }

    public User(DBUser dbUser) {
        if (dbUser != null) {
            this.setId(dbUser.getId());
            this.setEcasId(dbUser.getEcasId());
            this.setName(dbUser.getName());

            List<UserRole> roleList = new ArrayList<>();
            for (DBRole role : dbUser.getRole()) {
                roleList.add(new UserRole(role));
            }
            this.setRole(roleList);
            if (dbUser.getTerritory() != null) {
                this.codeTerritory = dbUser.getTerritory().getCodeTerritory();
            } else {
                this.codeTerritory = "";
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEcasId() {
        return ecasId;
    }

    public void setEcasId(String ecasId) {
        this.ecasId = ecasId;
    }

    public List<UserRole> getRole() {
        return role;
    }

    public void setRole(List<UserRole> role) {
        this.role = role;
    }

    public List<TrustBackbonePref> getPref() {
        return pref;
    }

    public void setPref(List<TrustBackbonePref> pref) {
        this.pref = pref;
    }

    public String getCodeTerritory() {
        return codeTerritory;
    }

    public void setCodeTerritory(String codeTerritory) {
        this.codeTerritory = codeTerritory;
    }
}
