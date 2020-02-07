package eu.europa.ec.joinup.tsl.web.config;

import org.jasig.cas.client.validation.Cas20ProxyTicketValidator;

public class Cas20ProxyTicketValidatorCustom extends Cas20ProxyTicketValidator {

    private final String casProxyPropertie;

    public Cas20ProxyTicketValidatorCustom(String casServerUrlPrefix, String casProxyPropertie) {
        super(casServerUrlPrefix);
        this.casProxyPropertie = casProxyPropertie;
    }

    @Override
    protected String getUrlSuffix() {
        return casProxyPropertie;
    }

}
