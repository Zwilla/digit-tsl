package eu.europa.ec.joinup.tsl.business.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.business.dto.TransitionEntry;
import eu.europa.ec.joinup.tsl.business.dto.TransitionStatus;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceHistory;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.business.util.DateUtils;
import eu.europa.ec.joinup.tsl.business.util.LocationUtils;
import eu.europa.ec.joinup.tsl.business.util.ServiceUtils;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.ec.joinup.tsl.model.DBCheck;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.CheckName;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

@Service
public class TLTransitionCheckService {

    Logger LOGGER = LoggerFactory.getLogger(TLTransitionCheckService.class);

    @Autowired
    private TLService tlService;

    @Autowired
    private CheckService checkService;

    @Transactional
    public List<CheckResultDTO> getTransitionCheck(int tlId) {
        List<CheckResultDTO> results = new ArrayList<>();
        // Init current TL and compared TL
        DBTrustedLists dbTL = tlService.getDbTL(tlId);
        TL currentTL = tlService.getDtoTL(dbTL);
        TL comparedTL = null;
        if (dbTL.getStatus().equals(TLStatus.PROD)) {
            comparedTL = tlService.getPreviousProductionByCountry(dbTL.getTerritory());
        } else {
            comparedTL = tlService.getPublishedTLByCountry(dbTL.getTerritory());
        }
        // Perform checks
        results.addAll(perfomTransitionChecks(tlId, dbTL, currentTL, comparedTL));
        // Init HR Location
        for (CheckResultDTO result : results) {
            result.setLocation(LocationUtils.idUserReadable(currentTL, result.getId()));
        }
        return results;
    }

    /**
     * Perform transition checks on TL
     * 
     * @param tlId
     * @param dbTL
     * @param currentTL
     * @param comparedTL
     * @return
     */
    private List<CheckResultDTO> perfomTransitionChecks(int tlId, DBTrustedLists dbTL, TL currentTL, TL comparedTL) {
        List<CheckResultDTO> results = new ArrayList<>();
        if (comparedTL != null) {
            Map<String, DBCheck> transitionChecks = checkService.getCheckMapByType(Tag.TRANSITION_CHECK);
            // Init publication date
            Date publicationDate = null;
            if (dbTL.getStatus().equals(TLStatus.PROD) && dbTL.getXmlFile() != null) {
                publicationDate = dbTL.getXmlFile().getFirstScanDate();
            } else {
                publicationDate = new Date();
            }
            // Init signing date
            Date signingDate = null;
            if (dbTL.getXmlFile() != null && dbTL.getXmlFile().getSignatureInformation() != null && dbTL.getXmlFile().getSignatureInformation().getSigningDate() != null) {
                signingDate = dbTL.getXmlFile().getSignatureInformation().getSigningDate();
            }

            // Checks
            if (isSigningDateUlterior(signingDate, publicationDate)) {
                results.add(new CheckResultDTO(tlId + "_" + Tag.SIGNATURE, transitionChecks.get(getCheckID(CheckName.SIGNING_DATE_ULTERIOR))));
            }
            if (isIssueDateUlterior(currentTL, publicationDate)) {
                results.add(new CheckResultDTO(tlId + "_" + Tag.SIGNATURE, transitionChecks.get(getCheckID(CheckName.ISSUE_DATE_ULTERIOR))));
            }
            if (isIssueDateBackDated(currentTL, comparedTL)) {
                results.add(new CheckResultDTO(tlId + "_" + Tag.SCHEME_INFORMATION + "_" + Tag.ISSUE_DATE, transitionChecks.get(getCheckID(CheckName.BACK_DATING_ISSUE_DATE))));
            }

            // Check TSPs
            results.addAll(performTSPChecks(currentTL, comparedTL, transitionChecks, publicationDate));

        }
        return results;
    }

    /**
     * Perform transition checks on TSPs
     * 
     * @param currentTL
     * @param comparedTL
     * @param results
     * @param transitionChecks
     * @param publicationDate
     * @return
     */
    public List<CheckResultDTO> performTSPChecks(TL currentTL, TL comparedTL, Map<String, DBCheck> transitionChecks, Date publicationDate) {
        List<CheckResultDTO> results = new ArrayList<>();
        // Init TSP temporary lists
        List<TLServiceProvider> tmpPreviousTSPs = (CollectionUtils.isEmpty(comparedTL.getServiceProviders()) ? new ArrayList<TLServiceProvider>()
                : new ArrayList<TLServiceProvider>(comparedTL.getServiceProviders()));
        for (TLServiceProvider currentTSP : currentTL.getServiceProviders()) {
            TLServiceProvider tspFound = null;
            for (TLServiceProvider previousTSP : tmpPreviousTSPs) {
                if (TLUtils.isTSPMatch(currentTSP, previousTSP)) {
                    tspFound = previousTSP;
                    results.addAll(getServicesCheck(currentTSP, previousTSP, publicationDate, transitionChecks));
                    break;
                }
            }
            if (tspFound != null) {
                tmpPreviousTSPs.remove(tspFound);
            }
        }
        if (CollectionUtils.isNotEmpty(tmpPreviousTSPs)) {
            for (TLServiceProvider tsp : tmpPreviousTSPs) {
                results.add(new CheckResultDTO(tsp.getId() + "_" + Tag.TSP_SERVICE, transitionChecks.get(getCheckID(CheckName.TSP_DELETE))));
            }
        }
        return results;
    }

    /**
     * Compare the day of signing and the day of publication. Return true if day of signing is after the publication
     * 
     * @param signingDate
     * @param publicationDate
     */
    public boolean isSigningDateUlterior(Date signingDate, Date publicationDate) {
        return DateUtils.compareDateByDay(signingDate, publicationDate);
    }

    /**
     * Compare the issue date and the day of publication. Return true if issue day is after the publication
     * 
     * @param currentTL
     * @param publicationDate
     */
    public boolean isIssueDateUlterior(TL currentTL, Date publicationDate) {
        if (currentTL.getSchemeInformation().getIssueDate() == null) {
            return true;
        }
        return DateUtils.compareDateByDay(currentTL.getSchemeInformation().getIssueDate(), publicationDate);
    }

    /**
     * Compare the current issue date with the compared one. Return true if the current issue date is before
     * 
     * @param currentTL
     * @param comparedTL
     */
    public boolean isIssueDateBackDated(TL currentTL, TL comparedTL) {
        if (currentTL.getSchemeInformation() != null && currentTL.getSchemeInformation().getIssueDate() != null && comparedTL.getSchemeInformation() != null
                && comparedTL.getSchemeInformation().getIssueDate() != null) {
            return currentTL.getSchemeInformation().getIssueDate().before(comparedTL.getSchemeInformation().getIssueDate());
        }
        return false;
    }

    /**
     * Get service transition checks
     * 
     * @param currentTSP
     * @param previousTSP
     * @param publicationDate
     */
    public List<CheckResultDTO> getServicesCheck(TLServiceProvider currentTSP, TLServiceProvider previousTSP, Date publicationDate, Map<String, DBCheck> transitionChecks) {
        List<CheckResultDTO> checkResults = new ArrayList<>();
        // Init services list
        List<TLServiceDto> tmpCurrentServices = (CollectionUtils.isEmpty(currentTSP.getTSPServices()) ? new ArrayList<TLServiceDto>() : new ArrayList<TLServiceDto>(currentTSP.getTSPServices()));
        List<TLServiceDto> tmpPreviousServices = (CollectionUtils.isEmpty(previousTSP.getTSPServices()) ? new ArrayList<TLServiceDto>() : new ArrayList<TLServiceDto>(previousTSP.getTSPServices()));
        if (!CollectionUtils.isEmpty(currentTSP.getTSPServices())) {
            for (TLServiceDto currentService : currentTSP.getTSPServices()) {
                TLServiceDto serviceFound = null;
                TransitionStatus transitionStatus = null;
                for (TLServiceDto previousService : tmpPreviousServices) {
                    transitionStatus = serviceMatch(currentService, previousService);
                    switch (transitionStatus) {
                    case EQUALS:
                        serviceFound = previousService;
                        break;
                    case SERVICE_MATCH:
                    case SERVICE_UPDATED:
                        // Compare service
                        final boolean equalsTransition = currentService.equalsTransition(previousService);
                        if (equalsTransition && !currentService.compareStartingDate(previousService)) {
                            // Change on starting date only
                            checkResults.add(new CheckResultDTO(currentService.getId(), transitionChecks.get(getCheckID(CheckName.SERVICE_UPDATE_STARTING_TIME_ONLY))));
                        } else if (!equalsTransition) {
                            // Change in service
                            checkResults.add(new CheckResultDTO(currentService.getId(), transitionChecks.get(getCheckID(CheckName.SERVICE_UPDATE_NO_HISTORY))));
                        }
                        // Compare history
                        if ((CollectionUtils.isEmpty(currentService.getHistory()) && CollectionUtils.isNotEmpty(previousService.getHistory()))
                                || (CollectionUtils.isNotEmpty(currentService.getHistory()) && CollectionUtils.isNotEmpty(previousService.getHistory())
                                        && (!currentService.getHistory().equals(previousService.getHistory())))) {
                            checkResults.add(new CheckResultDTO(currentService.getId(), transitionChecks.get(getCheckID(CheckName.HISTORY_CHANGE))));
                        }
                        serviceFound = previousService;
                        break;
                    case HISTORY_MATCH:
                    case HISTORY_UPDATED:
                        serviceFound = previousService;
                        List<TLServiceHistory> tmpCurrentHistory = (CollectionUtils.isEmpty(currentService.getHistory()) ? new ArrayList<TLServiceHistory>()
                                : new ArrayList<TLServiceHistory>(currentService.getHistory()));
                        if (CollectionUtils.isNotEmpty(tmpCurrentHistory)) {
                            if (!ServiceUtils.isServiceHistoryEquals(previousService, tmpCurrentHistory.get(0))) {
                                // The new history entry has changed from the previous service
                                checkResults.add(new CheckResultDTO(tmpCurrentHistory.get(0).getId(), transitionChecks.get(getCheckID(CheckName.NEW_HISTORY_CHANGE))));
                            }
                            // Remove first history entry which is the previous service entry
                            tmpCurrentHistory.remove(0);
                        }
                        List<TLServiceHistory> tmpPreviousHistory = (CollectionUtils.isEmpty(previousService.getHistory()) ? new ArrayList<TLServiceHistory>()
                                : new ArrayList<TLServiceHistory>(previousService.getHistory()));
                        if (!tmpCurrentHistory.equals(tmpPreviousHistory)) {
                            checkResults.add(new CheckResultDTO(currentService.getId(), transitionChecks.get(getCheckID(CheckName.HISTORY_CHANGE))));
                        }
                        break;
                    default:
                        break;
                    }

                    if (serviceFound != null) {
                        break;
                    }
                }

                if (serviceFound != null) {
                    if (transitionStatus != null && (transitionStatus.equals(TransitionStatus.EQUALS) || transitionStatus.equals(TransitionStatus.SERVICE_MATCH)
                            || transitionStatus.equals(TransitionStatus.SERVICE_UPDATED))) {
                        tmpCurrentServices.remove(currentService);
                    }
                    tmpPreviousServices.remove(serviceFound);
                }
            }
        }
        // New service(s) added
        if (CollectionUtils.isNotEmpty(tmpCurrentServices)) {
            for (TLServiceDto service : tmpCurrentServices) {
                if (DateUtils.compareDateByDay(publicationDate, service.getCurrentStatusStartingDate())) {
                    checkResults.add(new CheckResultDTO(service.getId(), transitionChecks.get(getCheckID(CheckName.SERVICE_PUBLICATION_DATE))));
                }
            }
        }
        // Previous service(s) removed
        if (CollectionUtils.isNotEmpty(tmpPreviousServices)) {
            for (int i = 0; i < tmpPreviousServices.size(); i++) {
                checkResults.add(new CheckResultDTO(currentTSP.getId(), transitionChecks.get(getCheckID(CheckName.SERVICE_DELETE))));
            }
        }
        return checkResults;
    }

    /**
     * Return the transition status between two services
     * 
     * @param currentService
     * @param previousService
     */
    private TransitionStatus serviceMatch(TLServiceDto currentService, TLServiceDto previousService) {
        if (currentService.equals(previousService)) {
            return TransitionStatus.EQUALS;
        } else {
            TransitionEntry currentEntry = new TransitionEntry(currentService);
            TransitionEntry previousEntry = new TransitionEntry(previousService);

            if (currentEntry.equals(previousEntry)) {
                if (DateUtils.compareDateNotNull(currentService.getCurrentStatusStartingDate(), previousService.getCurrentStatusStartingDate())) {
                    return TransitionStatus.SERVICE_MATCH;
                } else if (CollectionUtils.isNotEmpty(currentService.getHistory())
                        && DateUtils.compareDateNotNull(currentService.getHistory().get(0).getCurrentStatusStartingDate(), previousService.getCurrentStatusStartingDate())) {
                    return TransitionStatus.HISTORY_MATCH;
                } else {
                    // Service key match but no history and date has changed
                    return TransitionStatus.SERVICE_UPDATED;
                }
            } else if (CollectionUtils.isNotEmpty(currentService.getHistory())) {
                currentEntry = new TransitionEntry(currentService.getHistory().get(0));
                if (currentEntry.equals(previousEntry)) {
                    if (DateUtils.compareDateNotNull(currentService.getHistory().get(0).getCurrentStatusStartingDate(), previousService.getCurrentStatusStartingDate())) {
                        return TransitionStatus.HISTORY_MATCH;
                    } else {
                        // History key match but date has changed
                        return TransitionStatus.HISTORY_UPDATED;
                    }
                }
            }
            return TransitionStatus.NO_MATCH;
        }
    }

    private String getCheckID(CheckName name) {
        return Tag.TRANSITION_CHECK + "." + name;
    }

}