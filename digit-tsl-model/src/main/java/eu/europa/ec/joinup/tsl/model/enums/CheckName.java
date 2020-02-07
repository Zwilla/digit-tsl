package eu.europa.ec.joinup.tsl.model.enums;

public enum CheckName {

    // GENERICS
    IS_PRESENT, IS_LIST_NOT_EMPTY,
    IS_VALUES_NOT_EMPTY, IS_ATTRIBUTE_LANG_PRESENT,
    IS_ATTRIBUTE_LANG_LOWERCASE,
    IS_ATTRIBUTE_LANG_ALLOWED,
    IS_LIST_CONTAIN_LANG_EN,

    //DIGITAL IDENTITY
    IS_VALID_CERTIFICATE,
    IS_SUBJECT_NAME_MATCH,
    IS_X509SKI_MATCH,
    IS_CERTIFICATE_EXPIRED,
    IS_SIGNING_CERTIFICATE_PRESENT,
    AT_LEAST_TWO_CERT_NOT_EXPIRED,
    NEW_OLD_CERT_TIME_GAP,
    ALL_X509_CERTS_EXPIRED_GRANT_OR_REC_NAT_LEVEL,
    ONE_X509_CERT_EXPIRED_GRANT_OR_REC_NAT_LEVEL,

    // POINTER
    IS_MIME_TYPE_PRESENT,
    IS_LOCATION_PRESENT,
    IS_LOCATION_VALID,
    IS_LOCATION_ACCESSIBLE,
    IS_LOCATION_PUBLISHED_LOTL,
    IS_LOCATION_PUBLISHED_TL,
    IS_LOCATION_CORRECT_VALUES,
    IS_SCHEME_TERRITORY_PRESENT,
    IS_SCHEME_TERRITORY_CORRECT_VALUE,
    IS_TLS_TYPE_PRESENT,
    IS_TLS_TYPE_CORRECT_VALUE,
    IS_SCHEME_TYPE_COMMUNITY_RULES_CORRECT_VALUES,
    IS_SERVICE_DIGITAL_IDENTITIES_PRESENT,
    IS_DIGITAL_IDS_ALLOWED,
    IS_DIGITAL_IDS_FROM_OJ_PRESENT,
    IS_X509CERTIFICATE_CONTAINS_CORRECT_KEY_USAGES,
    IS_X509CERTIFICATE_CONTAINS_BASIC_CONSTRAINT_CA_FALSE,
    IS_X509CERTIFICATE_CONTAINS_TSLSIGNING_EXT_KEY_USAGE,
    IS_X509CERTIFICATE_CONTAINS_SUBJECT_KEY_IDENTIFIER,
    IS_X509CERTIFICATE_COUNTRY_CODE_MATCH,
    IS_X509CERTIFICATE_ORGANIZATION_MATCH,
    IS_SERVICE_DIGITAL_IDENTITIES_CORRECT,

    // COMPARISON CHECK
    IS_SEQUENCE_NUMBER_INCREMENTED,
    IS_ALL_PREVIOUS_TSP_SERVICE_PROVIDERS_PRESENT,
    IS_ALL_PREVIOUS_TSP_SERVICES_PRESENT,
    IS_ALL_PREVIOUS_TSP_SERVICE_HISTORIES_PRESENT,
    IS_CRITERIA_ASSERTS_NOT_NULL,
    IS_QUALIFICATION_ELEMENT_PRESENT,
    IS_QUALIFIERS_PRESENT,

    // TRANSITION CHECK
    SERVICE_UPDATE_STARTING_TIME_ONLY,
    SERVICE_UPDATE_NO_HISTORY,
    HISTORY_CHANGE,
    SERVICE_PUBLICATION_DATE,
    TSP_DELETE,
    SERVICE_DELETE,
    BACK_DATING_ISSUE_DATE,
    SIGNING_DATE_ULTERIOR,
    ISSUE_DATE_ULTERIOR,
    NEW_HISTORY_CHANGE,

    //TLCC
    ELADD_MUST_HAVE_SCHEME,
    ELADD_MAILTO_OR_HTTP,
    ELADD_ONE_MAILTO_ONE_HTTP,
    ELADD_ONE_MAILTO_LANG_EN,
    ELADD_ONE_MAILTO_LANG_EN_LOWERCASE,
    ELADD_ONE_HTTP_LANG_EN,
    ELADD_ONE_HTTP_LANG_EN_LOWERCASE,
    RUN_EXCEPTION,
    EXECUTION_ERROR,
    CHILDREN_SCHEMA,
    ATTRIBUTES_SCHEMA,
    VALUE_IS_INTEGER,
    PARTIAL_PARSING_ERROR,
    PARSING_ERROR,
    ELEMENT_SHOULD_NOT_BE_PRESENT,
    NOT_RECOGNIZED_ELEMENT,
    EXT_SET_TO_NON_CRITICAL,
    EXT_SET_TO_CRITICAL,
    DIGID_AS_EXPECTED,
    PUB_KEY_IN_X509_CERT,
    ONE_PUB_KEY_IN_DIG_ID,
    ONE_SUB_NAME_IN_DIG_ID,
    X509_CERT_SN_HAS_O,
    X509_CERT_SN_O_EQUAL_TSPNAME,
    X509_CERT_SN_O_EQUAL_TSPTRADENAME,
    X509_CERT_SN_O_EQUAL_TSPNAME_IGNORE_CASE,
    X509_CERT_SN_O_EQUAL_TSPTRADENAME_IGNORE_CASE,
    X509_CERT_SN_O_EQUAL_TSPNAME_OR_TSPTRADENAME,
    SAME_CERT_NOT_IN_OTHER_SERVICE_EQUAL_TYPE,
    SKI_IN_CERTIFICATE_GOOD,
    SKI_EQUAL_SKI_IN_CERTIFICATE,
    SKI_EQUAL_SKI_COMPUTED_FOR_CERTIFICATE,
    X509SUBJECTNAME_SPACENEXTTO_EQUAL,
    X509SUBJECTNAME_AS_IN_OTHER_X509CERTS,
    X509SUBJECTNAME_DIFFERENT_FROM_OTHER_X509CERTS,
    NO_X059CERTIFICATE_IN_HISTORICAL,
    DSKEYVALUE_HASPUBLICKEY,
    INDEX_OUT_OF_BOUNDS,
    LANG_ATTR_ONE_OF_ALLOWED,
    LANG_ATTR_SHOULD_LOWER_CASE,
    LANG_ATTR_ONE_SHALL_BE_EN,
    LANG_ATTR_SHOULD_EN_LOWER_CASE,
    TSL_TAG,
    SCHI_VI_VALUE_AS_SPEC,
    SCHI_SEQNUM_EQ_GREATER_THAN_1,
    SCHI_EN_SCHNAME_AS_SPECIFIED,
    TS_SCHI_SCHT_ONE_OF_EUMS_CCODES,
    TS_SCHI_SCHT_EU_FOR_ECLOL,
    TS_SCHI_SCHT_OTHER_FOR_NEUMS_NECLOL,
    TS_SCHI_NEXTUP_LISTISSUE,
    TS_SCHI_SCHN_TWO_FIRTST_CHARACTERS_EQUAL_TO_SCHT,
    TS_SCHI_SCHN_TWO_FIRTST_CHARACTERS_EQUAL_TO_SCHT_LOWERCASE,
    TS_SCHI_SCHN_SECOND_PART_VALUE_AS_SPECIFIED,
    TS_SCHI_SCHN_TWO_FIRTST_CHARACTERS_NOT_FROM_EU,
    TS_SCHI_SCHN_TWO_FIRTST_CHARACTERS_NOT_FROM_EU_LOWERCASE,
    TS_SCHI_SCHN_SECOND_PART_VALUE_NOT_FROM_EU,
    TS_SCHI_SCHN_SECOND_PART_VALUE_NOT_FROM_EU_WARNING,
    SCHI_STCR_EUMS_COMMON_URI_PRESENT,
    SCHI_STCR_EUMS_CC_URI_PRESENT,
    SCHI_STCR_URIS_FOR_EUMS,
    SCHI_STCR_URI_FOR_EUMS_CC,
    SCHI_STCR_URI_EUMS_VAL_EN,
    SCHI_STCR_URI_ECLOL_VAL_EN,
    SCHI_STCR_URI_EUMS_VAL_EN_LOWERCASE,
    SCHI_STCR_URI_ECLOL_VAL_EN_LOWERCASE,
    SCHI_LEGAL_NOTICE_MAND_VAL_EUMS,
    SCHI_LEGAL_NOTICE_MAND_VAL_EN_LANG,
    SCHI_LEGAL_NOTICE_MAND_VAL_EN_LANG_LOW_CASE,
    SCHI_LEGAL_NOTICE_OTHERVAL_EUMS,
    SCHI_LEGAL_NOTICE_MAND_VAL_ECLOL,
    SCHI_LEGAL_NOTICE_NONEUL,
    SCHI_TSLPOLICY_EUMSL_OR_ECLOL,
    SCHI_HISTPERIOD_VAL,
    EUMS_TL_PTRONE_POINTS_TO_ECLOL_XML,
    EUMS_TL_PTR_TSLTYPE_IN_PTR_TO_ECLOL,
    ECLOL_TL_PTR_TSLTYPE_IN_PTR_TO_ECLOL,
    ECLOL_TL_PTR_TSLTYPE_IN_PTR_TO_EUMS_TL,
    PTR_SCHTERR_IN_PTR_TO_ECLOL,
    PTR_SCHTERR_IN_PTR_TO_EUMS_TL,
    PTR_SCHTERR_IN_PTR_TO_ECLOL_LOWER_CASE,
    PTR_SCHTERR_IN_PTR_TO_EUMS_TL_LOWER_CASE,
    EUMS_TL_PTR_EC_CERTIFICATE_IN_PTR_TO_ECLOL_XML,
    ECLOL_ONE_PTR_TO_ECLOL,
    ECLOL_ONE_PTR_TO_EACH_EUMS_TL,
    SVC_EXPCERT_REVINFO_SVC_TYPE,
    SVC_QUALIFS_QUALIFIER_VALUE,
    SVC_QUALIFS_KEYUSAGEBIT_NAME_VALUE,
    SVC_POLID_ID_URN_QUAL,
    SVC_TAKENOVER_URI_SCH_AUTH,
    SVC_TAKENOVER_SCHOPNAME,
    SVC_TAKENOVER_SCHOPNAME_IGNORE_CASE,
    SVC_TAKENOVER_TSPNAME,
    SVC_TAKENOVER_TSPNAME_IGNORE_CASE,
    SIG_NOAPACHE_FW,
    SIG_REFURI,
    SIG_ONE_TRANSFORMS,
    SIG_TRANSFORMS_TWO_CHILDREN,
    SIG_TRANSFORM_1,
    SIG_TRANSFORM_2,
    SIG_CANON_ALG,
    SIG_KEYINFO_ONE_X509CERT,
    SIG_KEYINFO_CONTAINS_SIGNINGCERT,
    SIG_CRYPTO_VERIFICATION_OK,
    SIG_SIGNING_CERT_PROTECTED,
    SIG_IS_XADES_BES_OR_EPES,
    SIG_SIGCERT_ISSUER,
    SIG_SIGCERT_SN_ORG_SON,
    SIG_SIGCERT_SN_CC_SCHT,
    SIG_SIGCERT_KEYUS_VALUE,
    SIG_SIGCERT_EXTKEYUS_VALUE,
    SIG_SIGCERT_BASCONST_VALUE,
    SIG_SIGCERT_SKI_VALUE,
    SIG_SIGCERT_IN_ECLOL,
    SIG_SIGCERT_NOT_EXPIRED,
    NOT_EMPTY_STRING,
    STRING_LIST_NOTNULL_NOTEMPTY,
    NOT_NULL_STRING,
    TSLTAG,
    TSP_TRN_VAT_PATTERN,
    TSP_TRN_NTR_PATTERN,
    TSP_TRN_PAS_PATTERN,
    TSP_TRN_IDC_PATTERN,
    TSP_TRN_PNO_PATTERN,
    TSP_TRN_TIN_PATTERN,
    TSP_TRN_NONE_OF_PATTERNS,
    SVC_TYPE_IN_REG_910_2014,
    SVC_TYPE_IN_DIR_1999_93_EC,
    SVC_STAT_IN_REG_910_2014,
    SVC_STAT_VALID_FOR_TYPE,
    SVC_SDEFURI_PRESENT_IF_TYPE_NATROOTCA_QC,
    SVC_START_BEFORE_NOW,
    SVC_START_AFTER_PREVIOUS,
    SVC_ASIE_COHERENT_TO_TYPE,
    STAT_TRANSITION_TYPE_KNOWN,
    EUDIR_STAT_TRANSITION_CORRECT,
    EUREG_STAT_TRANSITION_CORRECT,
    ANNEX_J_STAT_TRANSITION,
    ANNEX_J_A_II,
    ANNEX_J_A_IV_CORRECT_QUAL_MIGRATION,
    ANNEX_J_A_IV_NO_OLD_QUALS,
    IS_URI,
    WEIRD_URI,
    URI_STRING_NO_TRIMMED,
    URI_NOTNULL,
    URI_DEREFERENCING_SUCCESS,
    URI_SSL_CONTEXT_BUILDING,
    NOT_EMPTY_URI,
    URI_LIST_NOTNULL_NOTEMPTY,
    URI_VAL_EUMSLIST,
    URI_VAL_ECLOL,
    URI_VAL_NEUMS_BUT_VALID,
    URI_VAL_NECLOL_BUT_VALID,
    URI_VAL_NOT_REGISTERED,
    URI_VAL_REGISTERED,
    URI_VAL_COHERENT_WITH_TSLTYPE,
    URI_VAL_NOEUMS_NORECLOL,
    URI_VAL_COHERENT_WITH_EUMS_LIST,
    URI_VAL_COHERENT_WITH_EC_LOL,
    URI_VAL_COHERENT_WITH_NEUMS_LIST,
    URI_VAL_COHERENT_WITH_NEC_LOL,
    URI_VAL_COHERENT_WITH_UNKNOWN_TYPE,
    EUREG_STARTING_TIME,
    SAME_COMPLETE_DIGITALID_NOT_IN_OTHER_SERVICE,
    EUMS_TL_PTR_ALL_EC_CERTIFICATES_IN_PTR_TO_ECLOL_XML,
    SIG_MUST_BE_PRESENT,
    SVC_QUALIFICATION_CRITERIALIST_WITH_SOME_CRITERIA,
    
    //SieQ
    ALO_KU_BITS,
    ALO_PS_OID

}
