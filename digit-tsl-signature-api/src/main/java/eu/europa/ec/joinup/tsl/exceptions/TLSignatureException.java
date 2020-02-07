package eu.europa.ec.joinup.tsl.exceptions;

public class TLSignatureException extends Exception {

    private static final long serialVersionUID = -2500987961184715539L;

    public TLSignatureException() {
        super();
    }

    public TLSignatureException(String message) {
        super(message);
    }

    public TLSignatureException(Throwable cause) {
        super(cause);
    }

    public TLSignatureException(String message, Throwable cause) {
        super(message, cause);
    }

}
