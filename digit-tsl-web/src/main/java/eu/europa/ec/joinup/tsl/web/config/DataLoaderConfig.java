package eu.europa.ec.joinup.tsl.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import eu.europa.esig.dss.client.crl.OnlineCRLSource;
import eu.europa.esig.dss.client.http.DataLoader;
import eu.europa.esig.dss.client.http.commons.OCSPDataLoader;
import eu.europa.esig.dss.client.http.proxy.ProxyConfig;
import eu.europa.esig.dss.client.ocsp.OnlineOCSPSource;
import eu.europa.esig.dss.x509.crl.CRLSource;
import eu.europa.esig.dss.x509.ocsp.OCSPSource;

@Configuration
public class DataLoaderConfig {

    @Value("${connection.timeout}")
    private int connectionTimeout;

    @Value("${connection.socket}")
    private int connectionSocket;

    @Autowired
    private ProxyConfig proxyConfig;

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public DataLoader dataLoader() {
        CommonDataLoaderOverride dataLoader = new CommonDataLoaderOverride();
        dataLoader.setTimeoutConnection(connectionTimeout);
        dataLoader.setTimeoutSocket(connectionSocket);
        dataLoader.setProxyConfig(proxyConfig);
        return dataLoader;
    }

    @Bean
    public OCSPDataLoader ocspDataLoader() {
        OCSPDataLoader ocspDataLoader = new OCSPDataLoader();
        ocspDataLoader.setTimeoutConnection(connectionTimeout);
        ocspDataLoader.setTimeoutSocket(connectionSocket);
        ocspDataLoader.setProxyConfig(proxyConfig);
        return ocspDataLoader;
    }

    @Bean
    public OCSPSource ocspSource() {
        OnlineOCSPSource ocspSource = new OnlineOCSPSource();
        ocspSource.setDataLoader(ocspDataLoader());
        return ocspSource;
    }

    @Bean
    public CRLSource crlSource() {
        OnlineCRLSource crlSource = new OnlineCRLSource();
        crlSource.setDataLoader(dataLoader());
        return crlSource;
    }

}
