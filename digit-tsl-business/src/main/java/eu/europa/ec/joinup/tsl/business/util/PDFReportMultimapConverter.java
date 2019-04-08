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
package eu.europa.ec.joinup.tsl.business.util;

import java.util.Map.Entry;

import com.google.common.collect.Multimap;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.collections.MapConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * Convert Map<String,String> schemeOperatorName to Multimap entries for PDF Report
 */
public class PDFReportMultimapConverter extends MapConverter {

    public PDFReportMultimapConverter(Mapper mapper) {
        super(mapper);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean canConvert(Class clazz) {
        return Multimap.class.isAssignableFrom(clazz);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        Multimap<String, String> map = (Multimap<String, String>) source;
        for (Entry<String, String> entry : map.entries()) {
            writer.startNode("schemeOperatorName");
            writer.addAttribute("language", entry.getKey());
            writer.setValue(entry.getValue());
            writer.endNode();
        }
    }
}
