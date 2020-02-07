package eu.europa.ec.joinup.tsl.model.enums;

public enum SignatureStatus {

    VALID,

    NOT_BASELINE_B,

    INDETERMINATE,

    INVALID,

    MORE_THAN_ONE_SIGNATURE,

    NOT_SIGNED,

    FILE_NOT_FOUND,

    CANNOT_BE_VALIDATED,

    // Signature doesn't cover the whole document
    NOT_FULL_SCOPE

}
