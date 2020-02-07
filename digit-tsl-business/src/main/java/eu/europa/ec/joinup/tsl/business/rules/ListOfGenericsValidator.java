package eu.europa.ec.joinup.tsl.business.rules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;
import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLGeneric;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemeInformation;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceExtension;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceHistory;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLTakenOverBy;
import eu.europa.ec.joinup.tsl.model.enums.CheckName;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

@Service
public class ListOfGenericsValidator extends AbstractCheckValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListOfGenericsValidator.class);

    private static final Set<Tag> SUPPORTED_CHECK_TARGETS;
    private static final Set<CheckName> SUPPORTED_CHECK_NAMES;

    static {
        SUPPORTED_CHECK_TARGETS = new HashSet<>();
        SUPPORTED_CHECK_TARGETS.add(Tag.SCHEME_OPERATOR_NAME);

        SUPPORTED_CHECK_NAMES = new HashSet<>();
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_LIST_NOT_EMPTY);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_VALUES_NOT_EMPTY);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_ATTRIBUTE_LANG_PRESENT);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_ATTRIBUTE_LANG_LOWERCASE);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_ATTRIBUTE_LANG_ALLOWED);
        SUPPORTED_CHECK_NAMES.add(CheckName.IS_LIST_CONTAIN_LANG_EN);
    }

    @Autowired
    private GenericValidator genericValidator;

    @Autowired
    private LanguageValidator languageValidator;

    @Override
    public boolean isSupported(CheckDTO check) {
        return SUPPORTED_CHECK_TARGETS.contains(check.getTarget()) && SUPPORTED_CHECK_NAMES.contains(check.getName());
    }

    @Override
    public List<CheckResultDTO> validate(CheckDTO check, TL tl) {
        List<CheckResultDTO> results = new ArrayList<>();
        checkAllSchemeOperatorNames(check, results, tl);
        return results;
    }

    private void checkAllSchemeOperatorNames(CheckDTO check, List<CheckResultDTO> results, TL tl) {

        TLSchemeInformation schemeInformation = tl.getSchemeInformation();
        List<TLPointersToOtherTSL> pointers = tl.getPointers();
        List<TLServiceProvider> serviceProviders = tl.getServiceProviders();

        // schemeInformation/SchemeOperatorName
        runCheckOnGenerics(schemeInformation.getId() + "_" + Tag.SCHEME_OPERATOR_NAME, check, schemeInformation.getSchemeOpeName(), results);

        if (CollectionUtils.isNotEmpty(pointers)) {
            for (TLPointersToOtherTSL pointer : pointers) {
                // schemeInformation/PointersToOtherTSL/OtherTSLPointer/OtherInformation/SchemeOperatorName
                runCheckOnGenerics(pointer.getId() + "_" + Tag.SCHEME_OPERATOR_NAME, check, pointer.getSchemeOpeName(), results);
            }
        }

        if (CollectionUtils.isNotEmpty(serviceProviders)) {
            for (TLServiceProvider serviceProvider : serviceProviders) {
                List<TLServiceDto> services = serviceProvider.getTSPServices();
                if (CollectionUtils.isNotEmpty(services)) {
                    for (TLServiceDto service : services) {

                        List<TLServiceExtension> extensions = service.getExtension();
                        if (CollectionUtils.isNotEmpty(extensions)) {
                            for (TLServiceExtension extension : extensions) {
                                TLTakenOverBy takenOverBy = extension.getTakenOverBy();
                                if (takenOverBy != null) {
                                    // schemeInformation/TrustServiceProvider/TSPServices/TSPService/ServiceInformation/ServiceInformationExtensions/Extension/TakenOverBy
                                    runCheckOnGenerics(takenOverBy.getId() + "_" + Tag.SCHEME_OPERATOR_NAME, check, takenOverBy.getOperatorName(), results);
                                }
                            }
                        }

                        List<TLServiceHistory> serviceHistories = service.getHistory();
                        if (CollectionUtils.isNotEmpty(serviceHistories)) {
                            for (TLServiceHistory serviceHistory : serviceHistories) {
                                List<TLServiceExtension> extensionsHistory = serviceHistory.getExtension();
                                if (CollectionUtils.isNotEmpty(extensionsHistory)) {
                                    for (TLServiceExtension extension : extensionsHistory) {
                                        TLTakenOverBy takenOverBy = extension.getTakenOverBy();
                                        if (takenOverBy != null) {
                                            // schemeInformation/TrustServiceProvider/TSPServices/TSPService/ServiceHistory/ServiceHistoryInstance/ServiceInformationExtensions/Extension/TakenOverBy
                                            runCheckOnGenerics(takenOverBy.getId() + "_" + Tag.SCHEME_OPERATOR_NAME, check, takenOverBy.getOperatorName(), results);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void runCheckOnGenerics(String location, CheckDTO check, List<? extends TLGeneric> listOfUris, List<CheckResultDTO> results) {
        switch (check.getName()) {
        case IS_PRESENT:
            addResult(check, location, genericValidator.isPresent(listOfUris), results);
            break;
        case IS_LIST_NOT_EMPTY:
            addResult(check, location, genericValidator.isCollectionNotEmpty(listOfUris), results);
            break;
        case IS_LIST_CONTAIN_LANG_EN:
            addResult(check, location, languageValidator.isLanguagesContainsEnglish(listOfUris), results);
            break;
        case IS_VALUES_NOT_EMPTY:
            isValuesNotEmpty(check, results, listOfUris);
            break;
        case IS_ATTRIBUTE_LANG_PRESENT:
            isLanguageAttributePresent(check, results, listOfUris);
            break;
        case IS_ATTRIBUTE_LANG_LOWERCASE:
            isLanguageAttributeLowercase(check, results, listOfUris);
            break;
        case IS_ATTRIBUTE_LANG_ALLOWED:
            isLanguageAttributeAllowed(check, results, listOfUris);
            break;
        default:
            LOGGER.warn("Unsupported " + check);
        }
    }

    public void runCheckOnGenerics(String location, List<CheckDTO> checkList, List<? extends TLGeneric> listOfUris, List<CheckResultDTO> results) {
        if (CollectionUtils.isNotEmpty(checkList)) {
            for (CheckDTO check : checkList) {
                runCheckOnGenerics(location, check, listOfUris, results);
            }
        }
    }

    private void isLanguageAttributeAllowed(CheckDTO check, List<CheckResultDTO> results, List<? extends TLGeneric> generics) {
        if (CollectionUtils.isNotEmpty(generics)) {
            for (TLGeneric generic : generics) {
                addResult(check, generic.getId(), languageValidator.isAllowedLanguage(generic), results);
            }
        }
    }

    private void isLanguageAttributeLowercase(CheckDTO check, List<CheckResultDTO> results, List<? extends TLGeneric> generics) {
        if (CollectionUtils.isNotEmpty(generics)) {
            for (TLGeneric generic : generics) {
                addResult(check, generic.getId(), languageValidator.isLanguageLowerCase(generic), results);
            }
        }
    }

    private void isLanguageAttributePresent(CheckDTO check, List<CheckResultDTO> results, List<? extends TLGeneric> generics) {
        if (CollectionUtils.isNotEmpty(generics)) {
            for (TLGeneric generic : generics) {
                addResult(check, generic.getId(), languageValidator.isLanguagePresent(generic), results);
            }
        }
    }

    private void isValuesNotEmpty(CheckDTO check, List<CheckResultDTO> results, List<? extends TLGeneric> generics) {
        if (CollectionUtils.isNotEmpty(generics)) {
            for (TLGeneric generic : generics) {
                addResult(check, generic.getId(), genericValidator.isNotEmpty(generic.getValue()), results);
            }
        }
    }

}
