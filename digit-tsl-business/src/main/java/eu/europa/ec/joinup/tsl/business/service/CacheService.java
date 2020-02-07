package eu.europa.ec.joinup.tsl.business.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

/**
 * Cache cleaner service
 */
@Service
public class CacheService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheService.class);

    @CacheEvict(value = "countryCache", allEntries = true)
    public void evictCountryCache() {
        LOGGER.info("Evict cache 'countryCache'");
    }

    @CacheEvict(value = "propertiesCache", allEntries = true)
    public void evictPropertiesCache() {
        LOGGER.info("Evict cache 'propertiesCache'");
    }

    @CacheEvict(value = "checkCache", allEntries = true)
    public void evictCheckCache() {
        LOGGER.info("Evict cache 'checkCache'");
    }

}
