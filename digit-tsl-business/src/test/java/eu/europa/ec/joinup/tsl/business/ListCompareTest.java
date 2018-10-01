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
package eu.europa.ec.joinup.tsl.business;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import eu.europa.ec.joinup.tsl.business.dto.tl.TLCertificate;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDigitalIdentification;

public class ListCompareTest {

    @Test
    public void test1() throws Exception {

        List<TLDigitalIdentification> list = new ArrayList<TLDigitalIdentification>();

        TLDigitalIdentification id = new TLDigitalIdentification();

        TLCertificate cert1 = new TLCertificate();
        id.setCertificateList(new ArrayList<TLCertificate>());
        id.getCertificateList().add(cert1);

        list.add(id);

        Assert.assertTrue(list.contains(id));

        TLDigitalIdentification id2 = new TLDigitalIdentification();

        TLCertificate cert2 = new TLCertificate();
        id2.setCertificateList(new ArrayList<TLCertificate>());
        id2.getCertificateList().add(cert2);

        Assert.assertTrue(list.contains(id2));

        List<TLDigitalIdentification> list2 = new ArrayList<TLDigitalIdentification>();
        list2.add(id2);
        list2.add(id);

        Assert.assertTrue(list.contains(list2.get(0)));
        Assert.assertTrue(list.contains(list2.get(1)));

        TLCertificate cert3 = new TLCertificate();
        id2.getCertificateList().add(cert3);

        Assert.assertFalse(list.contains(id2));

    }

    @Test
    public void test2() throws Exception {

        List<TLDigitalIdentification> list = new ArrayList<TLDigitalIdentification>();

        TLDigitalIdentification id = new TLDigitalIdentification();
        id.setCertificateList(new ArrayList<TLCertificate>());

        TLCertificate cert1 = new TLCertificate();
        id.getCertificateList().add(cert1);

        list.add(id);

        TLDigitalIdentification id2 = new TLDigitalIdentification();
        id2.setCertificateList(new ArrayList<TLCertificate>());

        TLCertificate cert2 = new TLCertificate();
        id2.getCertificateList().add(cert2);

        TLCertificate cert3 = new TLCertificate();
        id2.getCertificateList().add(cert3);

        Assert.assertFalse(list.contains(id2));

        TLCertificate cert4 = new TLCertificate();
        id.getCertificateList().add(cert4);

        Assert.assertTrue(list.contains(id2));
    }

}
