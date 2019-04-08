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
package eu.europa.ec.joinup.tsl.business.service;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.dto.TLSignature;
import eu.europa.ec.joinup.tsl.model.enums.SignatureStatus;

public class SignatureChangeServiceTest extends AbstractSpringTest {

    @Autowired
    private SignatureChangeService signatureChange;

    @SuppressWarnings("deprecation")
    @Test
    public void testDiff() {
        Date d = new Date();

        TLSignature s1 = new TLSignature();
        s1.setDigestAlgo("D1");
        s1.setEncryptionAlgo("E1");
        s1.setIndication(SignatureStatus.VALID);
        s1.setKeyLength(2048);
        s1.setSignatureFormat("F1");
        s1.setSignedBy("TOTO");
        s1.setSignedByNotAfter(d);
        s1.setSignedByNotBefore(d);
        s1.setSigningDate(new Date());

        TLSignature s2 = new TLSignature();
        s2.setDigestAlgo("D2");
        s2.setEncryptionAlgo("E2");
        s2.setIndication(SignatureStatus.INDETERMINATE);
        s2.setKeyLength(2048);
        s2.setSignatureFormat("F2");
        s2.setSignedBy("TATA");
        s2.setSignedByNotAfter(d);
        s2.setSignedByNotBefore(d);
        s2.setSigningDate(new Date(10, 10, 10));

        List<TLDifference> diff = signatureChange.getChanges("ID", s1, s2);
        Assert.assertNotNull(diff);
        Assert.assertEquals(6, diff.size());
    }

    @Test
    public void testEquals() {
        TLSignature s1 = new TLSignature();
        s1.setDigestAlgo("D1");
        s1.setEncryptionAlgo("E1");
        s1.setIndication(SignatureStatus.VALID);
        s1.setKeyLength(2048);
        s1.setSignatureFormat("F1");
        s1.setSignedBy("TOTO");
        s1.setSignedByNotAfter(new Date());
        s1.setSignedByNotBefore(new Date());
        s1.setSigningDate(new Date());

        TLSignature s2 = s1;

        List<TLDifference> diff = signatureChange.getChanges("ID", s1, s2);
        Assert.assertNotNull(diff);
        Assert.assertEquals(0, diff.size());
    }

}
