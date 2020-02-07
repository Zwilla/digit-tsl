package eu.europa.ec.joinup.tsl.business.rules;

import java.util.concurrent.Future;

import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.service.CacheDataLoader;

@Service
public class URIValidator extends GenericValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(URIValidator.class);

    @Autowired
    private CacheDataLoader cacheDataLoader;

    @Cacheable(value = "externalResourcesCache", key = "#url")
    public boolean isCorrectUrl(String url) {
        String[] schemes = { "http", "https" }; // DEFAULT schemes = "http", "https", "ftp"
        UrlValidator urlValidator = new UrlValidator(schemes);
        boolean valid = urlValidator.isValid(url);
        if (!valid) {
            LOGGER.debug("Not valid URL detected '" + url + "'");
        }
        return valid;
    }

    @Async
    public Future<boolean> isAccessibleUri(String uri) {
        return new AsyncResult<>(cacheDataLoader.isAccessibleUri(uri));
    }

    public boolean isSecureURI(String uri) {
        if (uri.toLowerCase().startsWith("https")) {
            return true;
        } else {
            return false;
        }
    }

}
