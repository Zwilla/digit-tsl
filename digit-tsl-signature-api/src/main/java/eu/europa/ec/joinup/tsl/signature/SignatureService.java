package eu.europa.ec.joinup.tsl.signature;

import java.util.List;

import eu.europa.ec.joinup.tsl.exceptions.TLSignatureException;

public interface SignatureService {

    List<String> getSealDisplayNames();

    byte[] sign(byte[] file, String sealDisplayName);

}
