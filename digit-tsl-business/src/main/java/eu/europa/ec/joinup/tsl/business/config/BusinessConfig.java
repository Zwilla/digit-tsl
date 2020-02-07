package eu.europa.ec.joinup.tsl.business.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import eu.europa.esig.dss.x509.CertificateToken;
import eu.europa.esig.dss.x509.KeyStoreCertificateSource;
import eu.europa.esig.jaxb.v5.utils.JaxbGregorianCalendarZulu;

@EnableCaching
@Configuration
public class BusinessConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessConfig.class);

    @Value("${lotl.keystore.file}")
    private String lotlKeyStoreFilename;

    @Value("${lotl.keystore.type}")
    private String lotlKeyStoreType;

    @Value("${lotl.keystore.password}")
    private String lotlKeyStorePassword;

    @Bean
    @Qualifier(value = "lotlKeyStore")
    public KeyStoreCertificateSource lotlKeyStore() throws Exception {
        File filesystem = new File(lotlKeyStoreFilename);

        if (!filesystem.exists()) {
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(filesystem);
                KeyStore ks = KeyStore.getInstance(lotlKeyStoreType);
                ks.load(null, null);
                ks.store(out, lotlKeyStorePassword.toCharArray());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                assert out != null;
                out.close();
            }
        }

        KeyStoreCertificateSource keyStoreCertificateSource = new KeyStoreCertificateSource(filesystem, lotlKeyStoreType, lotlKeyStorePassword);
        LOGGER.info("All certificates in the keystore : ");
        for (CertificateToken certificate : keyStoreCertificateSource.getCertificates()) {
            LOGGER.info(certificate.toString());
        }
        return keyStoreCertificateSource;
    }

    @Bean
    @Qualifier(value = "tslMarshaller")
    public Jaxb2Marshaller tslJaxb2Marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(eu.europa.esig.jaxb.tsl.ObjectFactory.class, eu.europa.esig.jaxb.tslx.ObjectFactory.class, eu.europa.esig.jaxb.ecc.ObjectFactory.class,
                eu.europa.esig.jaxb.xades.ObjectFactory.class, eu.europa.esig.jaxb.xades141.ObjectFactory.class, eu.europa.esig.jaxb.xmldsig.ObjectFactory.class);
        return marshaller;
    }

    @Bean
    @Qualifier(value = "tslMarshallerV5")
    public Jaxb2Marshaller tslJaxb2MarshallerV5() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setAdapters(new JaxbGregorianCalendarZulu());
        Map<String, Object> map = new HashMap<>();
        map.put(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
        map.put("com.sun.xml.bind.xmlDeclaration", false);
        map.put("com.sun.xml.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        marshaller.setMarshallerProperties(map);
        marshaller.setClassesToBeBound(eu.europa.esig.jaxb.v5.tsl.ObjectFactory.class, eu.europa.esig.jaxb.v5.tslx.ObjectFactory.class, eu.europa.esig.jaxb.v5.ecc.ObjectFactory.class,
                eu.europa.esig.jaxb.v5.xades.ObjectFactory.class, eu.europa.esig.jaxb.v5.xades141.ObjectFactory.class, eu.europa.esig.jaxb.v5.xmldsig.ObjectFactory.class);
        return marshaller;
    }

    @Bean
    public CacheManager getEhCacheManager() {
        return new EhCacheCacheManager(getEhCacheFactory().getObject());
    }

    @Bean
    public EhCacheManagerFactoryBean getEhCacheFactory() {
        EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
        factoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        factoryBean.setShared(true);
        return factoryBean;
    }

}
