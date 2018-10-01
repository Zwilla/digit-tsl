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
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.Column;
import eu.europa.ec.joinup.tsl.business.repository.ColumnsRepository;
import eu.europa.ec.joinup.tsl.model.DBColumnAvailable;

/**
 * Get attribute to show/hide on trust backbone.
 * When hide DTO attribute(s) are not filled and column(s) are not displayed
 */
@Service
@Transactional(value = TxType.REQUIRED)
public class BackboneColumnsService {

    @Autowired
    private ColumnsRepository columnsRepository;

    /**
     * Get column informations
     */
    public List<Column> getColumns() {
        List<Column> list = new ArrayList<>();
        List<DBColumnAvailable> colList = (List<DBColumnAvailable>) columnsRepository.findAll();
        for (DBColumnAvailable dbCol : colList) {
            Column usr = new Column(dbCol);
            list.add(usr);
        }
        return list;
    }

    /**
     * Check if column @name is set to visible in database
     *
     * @param name
     */
    public Boolean getColumnVisible(String name) {
        DBColumnAvailable col = columnsRepository.findByCode(name);
        if (col != null) {
            return col.getVisible();
        }
        return false;
    }
}
