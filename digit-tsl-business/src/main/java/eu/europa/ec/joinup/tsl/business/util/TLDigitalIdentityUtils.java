package eu.europa.ec.joinup.tsl.business.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import eu.europa.ec.joinup.tsl.business.dto.tl.TLCertificate;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDigitalIdentification;

/**
 * Digital Identity toolbox (comparison & others..)
 */
public class TLDigitalIdentityUtils {

    /**
     * Retrieve certificate from list of TLDigitalIdentity
     */
    public static List<TLCertificate> retrieveCertificates(List<TLDigitalIdentification> digitalIds) {
        List<TLCertificate> certificateList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(digitalIds)) {
            for (TLDigitalIdentification digit : digitalIds) {
                certificateList.addAll(digit.getCertificateList());
            }
        }
        return certificateList;
    }

    public static void sortByExpirationDate(List<TLCertificate> certificates) {
        certificates.sort(Comparator.comparing(TLCertificate::getCertAfter));
    }

    public static void sortByStartDate(List<TLCertificate> certificates) {
        certificates.sort(Comparator.comparing(TLCertificate::getCertNotBefore));

    }

    /**
     * Compare TLDigitalIdentification (certificate list, subject name, X509Ski, Other)
     *
     * @param tmp1
     * @param tmp2
     * @return
     */
    public static boolean matchTLDigitalIdentification(TLDigitalIdentification tmp1, TLDigitalIdentification tmp2) {
        if ((tmp1 != null) && (tmp2 != null)) {
            if ((tmp1.getCertificateList() != null) && (tmp2.getCertificateList() != null) && tmp1.getCertificateList().equals(tmp2.getCertificateList())) {
                return true;
            } else if ((tmp1.getSubjectName() != null) && (tmp2.getSubjectName() != null) && tmp1.getSubjectName().equals(tmp2.getSubjectName())) {
                return true;
            } else if ((tmp1.getX509ski() != null) && ((tmp2.getX509ski() != null) && Arrays.equals(tmp1.getX509ski(), tmp2.getX509ski()))) {
                return true;
            } else return equalsBetweenOther(tmp1.getOther(), tmp2.getOther());

        }
        return false;
    }

    /**
     * Compare 'Other' tag of TLDigitalIdentity
     *
     * @param tmp
     * @param tmpBis
     * @return
     */
    public static boolean equalsBetweenOther(List<Object> tmp, List<Object> tmpBis) {
        List<String> tmpOther = new ArrayList<>();
        List<String> tmpOtherBis = new ArrayList<>();
        if ((tmp == null) && (tmpBis == null)) {
            return false;
        }
        if (tmp != null) {
            for (Object o : tmp) {
                String str = AnyTypeUtils.convertOtherTag(o);
                if (str != null) {
                    tmpOther.add(str);
                }
            }
        }
        if (tmpBis != null) {
            for (Object o : tmpBis) {
                String str = AnyTypeUtils.convertOtherTag(o);
                if (str != null) {
                    tmpOtherBis.add(str);
                }
            }
        }
        return tmpOther.equals(tmpOtherBis);
    }

}
