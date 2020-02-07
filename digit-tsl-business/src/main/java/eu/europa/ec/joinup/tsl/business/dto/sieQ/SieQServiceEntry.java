package eu.europa.ec.joinup.tsl.business.dto.sieQ;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;

public class SieQServiceEntry {

    private String countryCode;
    private TLServiceDto service;
    private List<String> qualifiers;

    public SieQServiceEntry() {
        super();
    }

    public SieQServiceEntry(String cc, TLServiceDto tlServiceDto, List<String> list) {
        countryCode = cc;
        service = tlServiceDto;
        this.qualifiers = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (String qualifier : list) {
                this.qualifiers.add(qualifier.replace("https://uri.etsi.org/TrstSvc/TrustedList/SvcInfoExt/", ""));
            }
            this.qualifiers.sort(Comparator.naturalOrder());
        }
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public TLServiceDto getService() {
        return service;
    }

    public void setService(TLServiceDto service) {
        this.service = service;
    }

    public List<String> getQualifiers() {
        return qualifiers;
    }

    public void setQualifiers(List<String> qualifiers) {
        this.qualifiers = qualifiers;
    }

}
