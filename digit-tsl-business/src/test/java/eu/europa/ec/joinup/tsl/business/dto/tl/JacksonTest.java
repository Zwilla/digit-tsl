package eu.europa.ec.joinup.tsl.business.dto.tl;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonTest {

    @Test
    public void test() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        TLInformationExtension ext = new TLInformationExtension();
        ext.setCritical(true);

        mapper.writeValue(System.out, ext);
    }

    @Test
    public void test2() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        TLServiceExtension ext = new TLServiceExtension();
        ext.setCritical(true);
        ext.setAdditionnalServiceInfo(new TLAdditionnalServiceInfo());
        ext.getAdditionnalServiceInfo().setLanguage("EN");
        ext.getAdditionnalServiceInfo().setValue("Bla bla bla");

        mapper.writeValue(System.out, ext);

        TLServiceExtension ext2 = mapper.readValue(
                "{\"tlId\":0,\"id\":null,\"critical\":true,\"takenOverBy\":null,\"additionnalServiceInfo\":{\"tlId\":0,\"id\":null,\"language\":\"EN\",\"value\":\"Bla bla bla\"},\"qualificationsExtension\":null,\"expiredCertsRevocationDate\":null}",
                TLServiceExtension.class);
        Assert.assertEquals("EN", ext2.getAdditionnalServiceInfo().getLanguage());
        System.out.println("*** " + ext2.getAdditionnalServiceInfo().getValue());

    }

    @Test
    public void test3() throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        TLServiceExtension ext = new TLServiceExtension();
        ext.setCritical(true);

        ext.setTakenOverBy(new TLTakenOverBy());

        ext.setAdditionnalServiceInfo(new TLAdditionnalServiceInfo());
        ext.getAdditionnalServiceInfo().setLanguage("EN");
        ext.getAdditionnalServiceInfo().setValue("Bla bla bla");

        mapper.writeValue(System.out, ext);

        TLServiceExtension ext2 = mapper.readValue(
                "{\"tlId\":0,\"id\":null,\"critical\":true,\"takenOverBy\":{\"tlId\":0,\"id\":null,\"url\":null,\"tspName\":null,\"operatorName\":null,\"territory\":null,\"otherQualifier\":null},\"additionnalServiceInfo\":{\"tlId\":0,\"id\":null,\"language\":\"EN\",\"value\":\"Bla bla bla\"},\"qualificationsExtension\":null,\"expiredCertsRevocationDate\":null}",
                TLServiceExtension.class);
        Assert.assertEquals("EN", ext2.getAdditionnalServiceInfo().getLanguage());
        System.out.println("*** " + ext2.getAdditionnalServiceInfo().getValue());

    }

}
