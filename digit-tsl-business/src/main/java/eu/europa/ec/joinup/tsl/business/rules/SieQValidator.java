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
import eu.europa.ec.joinup.tsl.business.dto.tl.TLCriteria;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLKeyUsage;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLKeyUsageBit;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPolicies;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLQualificationExtension;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceExtension;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.business.service.CheckService;
import eu.europa.ec.joinup.tsl.business.util.LocationUtils;
import eu.europa.ec.joinup.tsl.model.enums.CheckName;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

@Service
public class SieQValidator extends AbstractCheckValidator {

    @Autowired
    private CheckService checkService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SieQValidator.class);

    private static final Set<CheckName> SUPPORTED_CHECK_NAMES;

    static {
        SUPPORTED_CHECK_NAMES = new HashSet<>();
        SUPPORTED_CHECK_NAMES.add(CheckName.ALO_KU_BITS);
        SUPPORTED_CHECK_NAMES.add(CheckName.ALO_PS_OID);
    }

    @Override
    public boolean isSupported(CheckDTO check) {
        return Tag.SIEQ_CHECK.equals(check.getTarget()) && SUPPORTED_CHECK_NAMES.contains(check.getName());
    }

    @Override
    public List<CheckResultDTO> validate(CheckDTO check, TL tl) {
        List<CheckResultDTO> results = new ArrayList<>();

        List<TLServiceProvider> serviceProviders = tl.getServiceProviders();
        if (CollectionUtils.isNotEmpty(serviceProviders)) {
            for (TLServiceProvider serviceProvider : serviceProviders) {
                List<TLServiceDto> services = serviceProvider.getTSPServices();
                if (CollectionUtils.isNotEmpty(services)) {
                    for (TLServiceDto service : services) {
                        if (CollectionUtils.isNotEmpty(service.getExtension())) {
                            for (TLServiceExtension extension : service.getExtension()) {
                                if (extension.getQualificationsExtension() != null) {
                                    checkSieQExtensions(check, extension.getQualificationsExtension(), results);
                                }
                            }
                        }
                    }
                }
            }
        }
        // Init HR Location
        for (CheckResultDTO result : results) {
            result.setLocation(LocationUtils.idUserReadable(tl, result.getId()));
        }
        return results;
    }

    /**
     * Perform SieQ check on TL
     * 
     * @param tl
     */
    public List<CheckResultDTO> getSieQCheck(TL tl) {
        List<CheckResultDTO> results = new ArrayList<>();

        List<CheckDTO> checks = checkService.getTarget(Tag.SIEQ_CHECK);

        List<TLServiceProvider> serviceProviders = tl.getServiceProviders();
        if (CollectionUtils.isNotEmpty(serviceProviders)) {
            for (TLServiceProvider serviceProvider : serviceProviders) {
                List<TLServiceDto> services = serviceProvider.getTSPServices();
                if (CollectionUtils.isNotEmpty(services)) {
                    for (TLServiceDto service : services) {
                        if (CollectionUtils.isNotEmpty(service.getExtension())) {
                            for (TLServiceExtension extension : service.getExtension()) {
                                if (extension.getQualificationsExtension() != null) {
                                    for (CheckDTO check : checks) {
                                        checkSieQExtensions(check, extension.getQualificationsExtension(), results);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // Init HR Location
        for (CheckResultDTO result : results) {
            result.setLocation(LocationUtils.idUserReadable(tl, result.getId()));
        }
        return results;
    }

    private void checkSieQExtensions(CheckDTO check, List<TLQualificationExtension> qualificationExtensions, List<CheckResultDTO> results) {
        switch (check.getName()) {
        case ALO_KU_BITS:
            checkAllKeyUsageBits(check, qualificationExtensions, results);
            break;
        case ALO_PS_OID:
            checkAllPolicyListOids(check, qualificationExtensions, results);
            break;
        default:
            LOGGER.warn("Unsupported check " + check.getName());
            break;
        }
    }

    /**
     * Verify number of policy OID for each policy list from a qualification extensions. Raise an error if more than 2 policy OID is found for a
     * policy list
     * 
     * @param check
     * @param qualificationExtensions
     * @param results
     */
    private void checkAllPolicyListOids(CheckDTO check, List<TLQualificationExtension> qualificationExtensions, List<CheckResultDTO> results) {
        for (TLQualificationExtension qualificationExtension : qualificationExtensions) {
            final TLCriteria criteria = qualificationExtension.getCriteria();
            if (criteria != null) {
                if (checkCriteriaPolicyLists(criteria)) {
                    addResult(check, qualificationExtension.getId(), false, results);
                    return;
                }
                if (CollectionUtils.isNotEmpty(criteria.getCriteriaList())) {
                    for (TLCriteria tlCriteria : criteria.getCriteriaList()) {
                        if (checkCriteriaPolicyLists(tlCriteria)) {
                            addResult(check, qualificationExtension.getId(), false, results);
                            return;
                        }
                    }
                }
            }
        }
    }

    /**
     * Check criteria policy lists and return true if there is more than 2 policy OID for a policy list
     * 
     * @param criteria
     * @return
     */
    private boolean checkCriteriaPolicyLists(final TLCriteria criteria) {
        if (CollectionUtils.isNotEmpty(criteria.getPolicyList())) {
            for (TLPolicies tlPolicies : criteria.getPolicyList()) {
                if (CollectionUtils.isNotEmpty(tlPolicies.getPolicyBit()) && tlPolicies.getPolicyBit().size() > 2) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Verify number of key usage bits for an "atLeastOne" qualification extensions with keyUsage only
     * 
     * @param check
     * @param qualificationExtensions
     * @param results
     */
    private void checkAllKeyUsageBits(CheckDTO check, List<TLQualificationExtension> qualificationExtensions, List<CheckResultDTO> results) {
        for (TLQualificationExtension qualificationExtension : qualificationExtensions) {
            final TLCriteria criteria = qualificationExtension.getCriteria();
            if (criteria != null) {
                if (checkCriteriaKeyUsageList(criteria)) {
                    addResult(check, qualificationExtension.getId(), false, results);
                    return;
                }
                if (CollectionUtils.isNotEmpty(criteria.getCriteriaList())) {
                    for (TLCriteria tlCriteria : criteria.getCriteriaList()) {
                        if (checkCriteriaKeyUsageList(tlCriteria)) {
                            addResult(check, qualificationExtension.getId(), false, results);
                            return;
                        }
                    }
                }
            }
        }
    }

    private boolean checkCriteriaKeyUsageList(TLCriteria criteria) {
        if (CollectionUtils.isNotEmpty(criteria.getKeyUsage())) {
            for (TLKeyUsage tlKeyUsage : criteria.getKeyUsage()) {
                if (CollectionUtils.isNotEmpty(tlKeyUsage.getKeyUsageBit())) {
                    int nbTrue = 0;
                    for (TLKeyUsageBit bit : tlKeyUsage.getKeyUsageBit()) {
                        if (bit.getIsValue()) {
                            nbTrue++;
                        }
                    }
                    if (nbTrue >= 3) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
