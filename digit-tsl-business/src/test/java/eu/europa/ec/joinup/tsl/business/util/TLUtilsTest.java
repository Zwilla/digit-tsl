package eu.europa.ec.joinup.tsl.business.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.junit.Test;

import eu.europa.esig.dss.DSSUtils;
import eu.europa.esig.dss.DigestAlgorithm;

import static org.junit.Assert.*;

public class TLUtilsTest {

    @Test
    public void isHex() {
        assertFalse(TLUtils.isHex(null));
        assertFalse(TLUtils.isHex("G".getBytes()));
        assertFalse(TLUtils.isHex("g".getBytes()));

        assertTrue(TLUtils.isHex("123465467890abcef".getBytes()));
        assertTrue(TLUtils.isHex("123465467890ABCDEF".getBytes()));
    }

    @Test
    public void isPdf() throws Exception {
        assertFalse(TLUtils.isPdf(null));
        assertFalse(TLUtils.isPdf(IOUtils.toByteArray(new FileInputStream("src/test/resources/lotl.xml"))));
        assertFalse(TLUtils.isPdf(IOUtils.toByteArray(new FileInputStream("src/test/resources/fake_pdf.pdf"))));

        assertTrue(TLUtils.isPdf(IOUtils.toByteArray(new FileInputStream("src/test/resources/sample.pdf"))));
    }

    @Test
    public void getSHA() {
        String sha2 = TLUtils.getSHA2("Hello world!".getBytes());
        assertEquals(sha2, TLUtils.getSHA2("Hello world!".getBytes()));
        assertNotEquals(sha2, TLUtils.getSHA2("Hello World!".getBytes()));
    }

    @Test
    public void getSHA2Url() {
        assertEquals("http://dns.org/tsl.sha2", TLUtils.getSHA2Url("http://dns.org/tsl.xml"));
        assertEquals("http://dns.org/tsl.sha2", TLUtils.getSHA2Url("http://dns.org/tsl.XML"));
        assertEquals("http://dns.org/tsl.sha2", TLUtils.getSHA2Url("http://dns.org/tsl"));
        assertEquals("http://dns.org/tsl.sha2", TLUtils.getSHA2Url("http://dns.org/tsl.xtsl"));
        assertEquals("http://dns.org/tsl.sha2", TLUtils.getSHA2Url("http://dns.org/tsl.XTSL"));
        assertEquals("http://dns.org/TSL.sha2", TLUtils.getSHA2Url("http://dns.org/TSL.XTSL"));
    }

    @Test
    public void compareDigestOfDifferentEncoding() throws IOException {
        ByteArrayInputStream contentUtf8 = new ByteArrayInputStream(FileUtils.readFileToByteArray(new File("src/test/resources/encoding/lotl_utf-8.xml")));
        ByteArrayInputStream contentUtf8WithoutBom = new ByteArrayInputStream(FileUtils.readFileToByteArray(new File("src/test/resources/encoding/lotl_utf-8-sansbom.xml")));
        System.out.println(contentUtf8);
        System.out.println(contentUtf8WithoutBom);

        BOMInputStream bomUtf8 = new BOMInputStream(contentUtf8);
        BOMInputStream bomUtf8WithoutBom = new BOMInputStream(contentUtf8WithoutBom);
        assertNotEquals(contentUtf8, contentUtf8WithoutBom);

        byte[] digestUtf8 = DSSUtils.digest(DigestAlgorithm.SHA256, IOUtils.toByteArray(bomUtf8));
        byte[] digestUtf8WithoutBom = DSSUtils.digest(DigestAlgorithm.SHA256, IOUtils.toByteArray(bomUtf8WithoutBom));
        assertArrayEquals(digestUtf8, digestUtf8WithoutBom);

    }

    @Test
    public void compareSha2() throws IOException {
        String shaUtf8 = TLUtils.getSHA2(FileUtils.readFileToByteArray(new File("src/test/resources/encoding/lotl_utf-8.xml")));
        String shaUtf8WithoutBom = TLUtils.getSHA2(FileUtils.readFileToByteArray(new File("src/test/resources/encoding/lotl_utf-8-sansbom.xml")));

        assertEquals(shaUtf8, shaUtf8WithoutBom);
    }

}
