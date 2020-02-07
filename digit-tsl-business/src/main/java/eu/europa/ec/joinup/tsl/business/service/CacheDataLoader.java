package eu.europa.ec.joinup.tsl.business.service;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import eu.europa.esig.dss.client.http.DataLoader;

/**
 * Cache data loader service
 */
@Service
public class CacheDataLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheDataLoader.class);

    @Autowired
    private DataLoader dataLoader;

    @Cacheable(value = "externalResourcesCache", key = "{#root.methodName, #uri}")
    public boolean isAccessibleUri(String uri) {
        boolean result = false;
        if (StringUtils.isNotEmpty(uri)) {
            try {
                byte[] bs = dataLoader.get(uri);
                result = ArrayUtils.isNotEmpty(bs);
            } catch (Exception e) {
                LOGGER.warn("Unable to access to '" + uri + "' : " + e.getMessage());
            }
        }
        return result;
    }

}
