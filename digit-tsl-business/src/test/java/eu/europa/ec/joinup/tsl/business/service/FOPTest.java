package eu.europa.ec.joinup.tsl.business.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.fop.apps.EnvironmentalProfileFactory;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopConfParser;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.apps.io.ResourceResolverFactory;
import org.apache.xmlgraphics.io.Resource;
import org.apache.xmlgraphics.io.ResourceResolver;
import org.apache.xmlgraphics.io.TempResourceResolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class FOPTest {

    private FopFactory fopFactory;
    private FOUserAgent foUserAgent;
    private Templates templateNotificationChange;

    @Before
    public void init() throws Exception {

        ResourceResolver rr = ResourceResolverFactory.createTempAwareResourceResolver(getTempResourceResolver(), getResourceResolver());
        File conf = new ClassPathResource("/fop/fop.conf.xml").getFile();
        Assert.assertTrue(conf.exists());

        FopFactoryBuilder builder = new FopConfParser(new FileInputStream(conf), EnvironmentalProfileFactory.createRestrictedIO(conf.getParentFile().toURI(), rr)).getFopFactoryBuilder();
        builder.setAccessibility(true);

        fopFactory = builder.build();

        foUserAgent = fopFactory.newFOUserAgent();
        foUserAgent.setCreator("TL-Manager");
        foUserAgent.setAccessibility(true);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        InputStream simpleIS = new ClassPathResource("/xslt/notificationChange.xslt").getInputStream();
        templateNotificationChange = transformerFactory.newTemplates(new StreamSource(simpleIS));
        IOUtils.closeQuietly(simpleIS);
    }

    @Test
    public void testWithCyrillic() throws Exception {
        FileInputStream stream = new FileInputStream("src/test/resources/cyrillic.xml");
        byte[] array = IOUtils.toByteArray(stream);
        InputStream is = new ByteArrayInputStream(array);
        Transformer transformer = templateNotificationChange.newTransformer();
        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, new FileOutputStream("target/cyrillic.pdf"));
        Result res = new SAXResult(fop.getDefaultHandler());
        transformer.transform(new StreamSource(is), res);
    }

    private ResourceResolver getResourceResolver() {
        return new ResourceResolver() {

            @Override
            public Resource getResource(URI uri) throws IOException {
                return new Resource(uri.toURL().openStream());
            }

            @Override
            public OutputStream getOutputStream(URI uri) throws IOException {
                return new FileOutputStream(new File(uri));
            }
        };
    }

    private TempResourceResolver getTempResourceResolver() {
        return new TempResourceResolver() {

            private final Map<String, ByteArrayOutputStream> tempBaos = Collections.synchronizedMap(new HashMap<String, ByteArrayOutputStream>());

            @Override
            public Resource getResource(String id) {
                if (!tempBaos.containsKey(id)) {
                    throw new IllegalArgumentException("Resource with id = " + id + " does not exist");
                }
                return new Resource(new ByteArrayInputStream(tempBaos.remove(id).toByteArray()));
            }

            @Override
            public OutputStream getOutputStream(String id) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                tempBaos.put(id, out);
                return out;
            }
        };
    }
}
