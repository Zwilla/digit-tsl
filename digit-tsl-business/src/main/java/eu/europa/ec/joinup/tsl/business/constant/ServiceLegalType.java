package eu.europa.ec.joinup.tsl.business.constant;

/** Legal Service Types **/
public enum ServiceLegalType {

    /* Q Trust Services (9) */
    Q_CERT_ESIG("QCertESig", true), Q_CERT_ESEAL("QCertESeal", true), Q_WAC("QWAC", true), Q_VAL_ESIG("QValQESig", true), Q_PRES_ESIG("QPresQESig", true), Q_VAL_ESEAL("QValQESeal",
            true), Q_PRES_ESEAL("QPresQESeal", true), Q_TIMESTAMP("QTimestamp", true), Q_ERDS("QeRDS", true),
    /* nonQ Trust Services (11) */
    CERT_ESIG("CertESig", false), CERT_ESEAL("CertESeal", false), WAC("WAC", false), VAL_ESIG("ValESig", false), GEN_ESIG("GenESig", false), PRES_ESIG("PresESig", false), VAL_ESEAL("ValESeal",
            false), GEN_ESEAL("GenESeal", false), PRES_ESEAL("PresESeal", false), TIMESTAMP("Timestamp", false), ERDS("eRDS", false),
    /* non Reg, nationally defined TS (1) */
    NON_REGULATORY("NonRegulatory", false),
    /* Undefined (1) */
    UNDEFINED("CertUndefined", false);

    private String code;
    private boolean qualified;

    ServiceLegalType(String code, boolean qualified) {
        this.code = code;
        this.qualified = qualified;
    }

    /**
     * Return ENUM value from enum code
     *
     * @param code
     */
    public static ServiceLegalType getFromCode(String code) {
        for (ServiceLegalType type : values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        return UNDEFINED;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isQualified() {
        return qualified;
    }

    public void setQualified(boolean qualified) {
        this.qualified = qualified;
    }

}
