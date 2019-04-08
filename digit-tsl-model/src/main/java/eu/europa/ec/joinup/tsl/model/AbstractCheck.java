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
package eu.europa.ec.joinup.tsl.model;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import eu.europa.ec.joinup.tsl.model.enums.CheckImpact;
import eu.europa.ec.joinup.tsl.model.enums.CheckName;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;
import eu.europa.ec.joinup.tsl.model.enums.CheckType;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

@MappedSuperclass
public abstract class AbstractCheck {

    @Id
    @GeneratedValue
    @Column(name = "CHECK_ID", nullable = false, updatable = false)
    private int id;

    @Column(name = "DESCRIPTION", length = 4096, nullable = false, updatable = false)
    private String description;

    @Column(name = "TARGET", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private Tag target;

    @Column(name = "NAME", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private CheckName name;

    @Column(name = "PRIORITY", nullable = false)
    @Enumerated(EnumType.STRING)
    private CheckStatus priority;

    @Column(name = "IMPACT")
    @Enumerated(EnumType.STRING)
    private CheckImpact impact;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private CheckType type;

    @Column(name = "VALUE", updatable = false)
    private String value;

    public AbstractCheck() {
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractCheck other = (AbstractCheck) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Tag getTarget() {
        return target;
    }

    public void setTarget(Tag target) {
        this.target = target;
    }

    public CheckName getName() {
        return name;
    }

    public void setName(CheckName name) {
        this.name = name;
    }

    public CheckStatus getPriority() {
        return priority;
    }

    public void setPriority(CheckStatus priority) {
        this.priority = priority;
    }

    public CheckImpact getImpact() {
        return impact;
    }

    public void setImpact(CheckImpact impact) {
        this.impact = impact;
    }

    public CheckType getType() {
        return type;
    }

    public void setType(CheckType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "DBCheck [id=" + id + ", target=" + target + ", name=" + name + "]";
    }

}
