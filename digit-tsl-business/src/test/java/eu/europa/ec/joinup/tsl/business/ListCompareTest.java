package eu.europa.ec.joinup.tsl.business;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import eu.europa.ec.joinup.tsl.business.dto.tl.TLCertificate;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDigitalIdentification;

public class ListCompareTest {

    @Test
    public void test1() {

        List<TLDigitalIdentification> list = new ArrayList<>();

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

        List<TLDigitalIdentification> list2 = new ArrayList<>();
        list2.add(id2);
        list2.add(id);

        Assert.assertTrue(list.contains(list2.get(0)));
        Assert.assertTrue(list.contains(list2.get(1)));

        TLCertificate cert3 = new TLCertificate();
        id2.getCertificateList().add(cert3);

        Assert.assertFalse(list.contains(id2));

    }

    @Test
    public void test2() {

        List<TLDigitalIdentification> list = new ArrayList<>();

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
