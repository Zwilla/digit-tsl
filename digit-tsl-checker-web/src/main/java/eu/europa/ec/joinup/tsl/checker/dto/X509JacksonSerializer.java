package eu.europa.ec.joinup.tsl.checker.dto;

import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class X509JacksonSerializer extends JsonSerializer<X509Certificate> {

    @Override
    public void serialize(X509Certificate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        try {
            gen.writeBinary(value.getEncoded());
        } catch (CertificateEncodingException e) {
            throw new IOException(e);
        }
    }

    @Override
    public Class<X509Certificate> handledType() {
        return X509Certificate.class;
    }

}
