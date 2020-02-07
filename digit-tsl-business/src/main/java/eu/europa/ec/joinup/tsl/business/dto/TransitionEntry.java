package eu.europa.ec.joinup.tsl.business.dto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceExtension;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceHistory;
import eu.europa.esig.dss.utils.Utils;

public class TransitionEntry {

    private String x509SKI;
    private List<String> asieList;

    public TransitionEntry(TLServiceDto service) {
        super();
        if (CollectionUtils.isNotEmpty(service.getDigitalIdentification())) {
            if (CollectionUtils.isNotEmpty(service.getDigitalIdentification().get(0).getCertificateList())) {
                this.x509SKI = service.getDigitalIdentification().get(0).getCertificateList().get(0).getCertSkiB64();
            }
        }
        initASIEList(service.getExtension());
    }

    public TransitionEntry(TLServiceHistory history) {
        super();
        if (CollectionUtils.isNotEmpty(history.getDigitalIdentification())) {
            this.x509SKI = Utils.toBase64(history.getDigitalIdentification().get(0).getX509ski());
        }
        initASIEList(history.getExtension());
    }

    public TransitionEntry(String x509ski, Date startingDate, List<TLServiceExtension> asiE) {
        super();
        x509SKI = x509ski;
        initASIEList(asiE);
    }

    private void initASIEList(List<TLServiceExtension> asiE) {
        this.asieList = new ArrayList<>();
        for (TLServiceExtension tlServiceExtension : asiE) {
            if (tlServiceExtension.getAdditionnalServiceInfo() != null) {
                this.asieList.add(tlServiceExtension.getAdditionnalServiceInfo().getValue());
            }
        }
        this.asieList.sort(Comparator.naturalOrder());
    }

    public String getX509SKI() {
        return x509SKI;
    }

    public void setX509SKI(String x509ski) {
        x509SKI = x509ski;
    }

    public List<String> getAsiE() {
        return asieList;
    }

    public void setAsiE(List<String> asiE) {
        this.asieList = asiE;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((asieList == null) ? 0 : asieList.hashCode());
        result = prime * result + ((x509SKI == null) ? 0 : x509SKI.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TransitionEntry other = (TransitionEntry) obj;
        if (asieList == null) {
            if (other.asieList != null)
                return false;
        } else if (!asieList.equals(other.asieList))
            return false;
        if (x509SKI == null) {
            return other.x509SKI == null;
        } else return x509SKI.equals(other.x509SKI);
    }

    @Override
    public String toString() {
        return "TransitionEntry [x509SKI=" + x509SKI + ", asieList=" + asieList + "]";
    }

}
