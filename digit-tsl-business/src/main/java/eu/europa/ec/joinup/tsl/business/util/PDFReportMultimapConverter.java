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
