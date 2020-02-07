package eu.europa.ec.joinup.tsl.checker.dto;

import java.util.List;

public class TLCCFileRequestDTO {

    private byte[] lotlFile;
    private byte[] tlFile;
    private List<String> keyStore;

    public TLCCFileRequestDTO() {
        super();
    }

    public TLCCFileRequestDTO(byte[] lotlFile, byte[] file) {
        this.lotlFile = lotlFile;
        this.tlFile = file;
    }

    public byte[] getLotlFile() {
        return lotlFile;
    }

    public void setLotlFile(byte[] lotlFile) {
        this.lotlFile = lotlFile;
    }

    public byte[] getTlFile() {
        return tlFile;
    }

    public void setTlFile(byte[] tlFile) {
        this.tlFile = tlFile;
    }

    public List<String> getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(List<String> keyStore) {
        this.keyStore = keyStore;
    }
}
