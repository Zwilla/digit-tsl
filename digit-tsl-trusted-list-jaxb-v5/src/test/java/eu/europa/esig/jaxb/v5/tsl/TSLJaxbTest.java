package eu.europa.esig.jaxb.v5.tsl;

import javax.xml.bind.JAXBContext;

import org.junit.Test;

public class TSLJaxbTest {

    @Test
    public void test() throws Exception {

        JAXBContext ctx = JAXBContext.newInstance(TrustStatusListTypeV5.class);

        TrustStatusListTypeV5 t = new TrustStatusListTypeV5();

        ctx.createMarshaller().marshal(new ObjectFactory().createTrustServiceStatusList(t), System.out);

    }

}
