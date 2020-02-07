package eu.europa.ec.joinup.tsl.signature;

import java.util.ArrayList;
import java.util.List;

import eu.europa.ec.joinup.tsl.exceptions.TLSignatureException;

public class EmptySignatureService implements SignatureService {

    @Override
    public List<String> getSealDisplayNames() {
        return new ArrayList<>();
    }

    @Override
    public byte[] sign(byte[] file, String sealDisplayName) {
        throw new UnsupportedOperationException();
    }

}
