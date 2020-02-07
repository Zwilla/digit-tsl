package eu.europa.esig.jaxb.v5.utils;

import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Assert;
import org.junit.Test;

public class JaxbCalendarZuluTest {

    @Test
    public void test1() throws Exception {

        JaxbGregorianCalendarZulu z = new JaxbGregorianCalendarZulu();
        String v = "2009-05-05T14:00:00Z";
        XMLGregorianCalendar cal = z.unmarshal(v);

        System.out.println(cal);

        System.out.println(z.marshal(cal));

        Assert.assertEquals(v, z.marshal(z.unmarshal(v)));

    }

}
