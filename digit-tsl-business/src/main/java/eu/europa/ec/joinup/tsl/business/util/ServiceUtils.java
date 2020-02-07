package eu.europa.ec.joinup.tsl.business.util;

import java.util.Arrays;

import org.apache.commons.collections.CollectionUtils;

import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceHistory;

public class ServiceUtils {

    /**
     * Compare service with history entry
     * 
     * @param service
     * @param history
     */
    public static boolean isServiceHistoryEquals(TLServiceDto service, TLServiceHistory history) {
        if (!service.getTypeIdentifier().equals(history.getTypeIdentifier())) {
            return false;
        } else if (!service.getServiceName().equals(history.getServiceName())) {
            return false;
        } else if (!service.getCurrentStatus().equals(history.getCurrentStatus())) {
            return false;
        } else if (!service.getCurrentStatusStartingDate().equals(history.getCurrentStatusStartingDate())) {
            return false;
        } else if (!service.getExtension().equals(history.getExtension())) {
            return false;
        } else {
            byte[] historySKI = getHistorySKI(history);
            byte[] serviceSKI = getServiceSKI(service);
            if (historySKI == null || serviceSKI == null || !Arrays.equals(historySKI, serviceSKI)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get service
     * 
     * @param service
     */
    private static byte[] getServiceSKI(TLServiceDto service) {
        if (CollectionUtils.isNotEmpty(service.getDigitalIdentification())) {
            if (CollectionUtils.isNotEmpty(service.getDigitalIdentification().get(0).getCertificateList())) {
                return service.getDigitalIdentification().get(0).getCertificateList().get(0).getCertSki();
            }
        }
        return new byte[] { 0 };
    }

    /**
     * Get history SKI
     * 
     * @param history
     */
    private static byte[] getHistorySKI(TLServiceHistory history) {
        if (CollectionUtils.isNotEmpty(history.getDigitalIdentification())) {
            return history.getDigitalIdentification().get(0).getX509ski();
        }
        return new byte[] { 0 };
    }
}
