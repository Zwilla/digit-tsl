/*******************************************************************************
 * DIGIT-TSL - Trusted List Manager
 * Copyright (C) 2018 European Commission, provided under the CEF E-Signature programme
 * 
 * This file is part of the "DIGIT-TSL - Trusted List Manager" project.
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package eu.europa.ec.joinup.tsl.business.util;

import java.util.ArrayList;
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
                for (TLCertificate certificate : digit.getCertificateList()) {
                    certificateList.add(certificate);
                }
            }
        }
        return certificateList;
    }

    public static void sortByExpirationDate(List<TLCertificate> certificates) {
        certificates.sort(new Comparator<TLCertificate>() {

            @Override
            public int compare(TLCertificate c1, TLCertificate c2) {
                return c1.getCertAfter().compareTo(c2.getCertAfter());
            }
        });
    }

    public static void sortByStartDate(List<TLCertificate> certificates) {
        certificates.sort(new Comparator<TLCertificate>() {

            @Override
            public int compare(TLCertificate c1, TLCertificate c2) {
                return c1.getCertNotBefore().compareTo(c2.getCertNotBefore());
            }
        });

    }

    /**
     * Compare TLDigitalIdentification (certificate list, subject name, X509Ski, Other)
     *
     * @param tmp1
     * @param tmp2
     * @return
     */
    public static Boolean matchTLDigitalIdentification(TLDigitalIdentification tmp1, TLDigitalIdentification tmp2) {
        if ((tmp1 != null) && (tmp2 != null)) {
            if ((tmp1.getCertificateList() != null) && (tmp2.getCertificateList() != null) && tmp1.getCertificateList().equals(tmp2.getCertificateList())) {
                return true;
            } else if ((tmp1.getSubjectName() != null) && (tmp2.getSubjectName() != null) && tmp1.getSubjectName().equals(tmp2.getSubjectName())) {
                return true;
            } else if ((tmp1.getX509ski() != null) && ((tmp2.getX509ski() != null) && tmp1.getX509ski().equals(tmp2.getX509ski()))) {
                return true;
            } else if (equalsBetweenOther(tmp1.getOther(), tmp2.getOther())) {
                return true;
            }

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
    public static Boolean equalsBetweenOther(List<Object> tmp, List<Object> tmpBis) {
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
