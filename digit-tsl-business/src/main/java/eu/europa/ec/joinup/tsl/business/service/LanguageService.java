package eu.europa.ec.joinup.tsl.business.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.Properties;

@Service
public class LanguageService {

    public static final String ENGLISH_CODE = "en";

    @Autowired
    private PropertiesService propertyService;

    @Cacheable(value = "propertiesCache", key = "#root.methodName")
    public List<String> getAllowedLanguages() {
        List<String> result = new ArrayList<>();
        List<Properties> languages = propertyService.getLanguages();
        if (CollectionUtils.isNotEmpty(languages)) {
            for (Properties property : languages) {
                result.add(property.getLabel());
            }
        }
        return result;
    }

}
