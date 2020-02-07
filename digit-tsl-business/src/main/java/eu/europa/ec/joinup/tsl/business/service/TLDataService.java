package eu.europa.ec.joinup.tsl.business.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.data.stats.TLDataTypeCriteriaDTO;
import eu.europa.ec.joinup.tsl.business.dto.data.tl.ServiceDataDTO;
import eu.europa.ec.joinup.tsl.business.dto.data.tl.TSPDataDTO;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceExtension;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceHistory;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.business.repository.HistoryRepository;
import eu.europa.ec.joinup.tsl.business.repository.ServiceRepository;
import eu.europa.ec.joinup.tsl.business.repository.TrustServiceProviderRepository;
import eu.europa.ec.joinup.tsl.business.util.TLQServiceTypeUtils;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.ec.joinup.tsl.model.DBHistory;
import eu.europa.ec.joinup.tsl.model.DBService;
import eu.europa.ec.joinup.tsl.model.DBTrustServiceProvider;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

/**
 * Service data information management (trust service/service/certificate)
 */
@Service
@Transactional(value = TxType.REQUIRED)
public class TLDataService {

    @Value("${lotl.territory}")
    private String lotlTerritory;

    @Autowired
    private TrustServiceProviderRepository trustServiceRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private TLCertificateService certificateService;

    /**
     * Get all the trust service providers
     */
    public Set<TSPDataDTO> findAllTSPs() {
        Set<TSPDataDTO> result = new TreeSet<>(new Comparator<TSPDataDTO>() {
            @Override
            public int compare(TSPDataDTO o1, TSPDataDTO o2) {
                if (o1.getCountryCode().equals(o2.getCountryCode())) {
                    return o1.getMName().compareTo(o2.getMName());
                } else {
                    return o1.getCountryCode().compareTo(o2.getCountryCode());
                }
            }
        });
        for (DBTrustServiceProvider tsp : trustServiceRepository.findAll()) {
            result.add(new TSPDataDTO(tsp, true));
        }
        return result;
    }

    /**
     * Search trust service data by criteria (type/country)
     *
     * @param criteriaDTO
     */
    public List<TSPDataDTO> searchTrustServiceProvider(TLDataTypeCriteriaDTO criteriaDTO) {
        List<TSPDataDTO> result = new ArrayList<>();

        List<DBTrustServiceProvider> dbTrustServices = trustServiceRepository.findDistinctByCountryCodeInAndServicesQServiceTypesInOrderByCountryCode(criteriaDTO.getCountries(),
                criteriaDTO.getSetServiceTypes());
        for (DBTrustServiceProvider dbService : dbTrustServices) {
            result.add(new TSPDataDTO(dbService, true));
        }
        return result;
    }

    /**
     * Search services data by criteria (type/country)
     *
     * @param criteriaDTO
     */
    public List<ServiceDataDTO> searchServiceByType(TLDataTypeCriteriaDTO criteriaDTO) {
        List<ServiceDataDTO> result = new ArrayList<>();

        List<DBService> dbServices = serviceRepository.findByCountryCodeInAndQServiceTypesInOrderByCountryCode(criteriaDTO.getCountries(), criteriaDTO.getSetServiceTypes());
        for (DBService dbService : dbServices) {
            result.add(new ServiceDataDTO(dbService, true));
        }
        return result;
    }

    /**
     * Find all TSP by country code and initialize services and history list
     *
     * @param countryCode
     */
    public List<TSPDataDTO> findAllByCountry(String countryCode) {
        List<TSPDataDTO> result = new ArrayList<>();
        List<DBTrustServiceProvider> dbTSPs = trustServiceRepository.findAllByCountryCode(countryCode);
        for (DBTrustServiceProvider dbTrustServiceProvider : dbTSPs) {
            result.add(new TSPDataDTO(dbTrustServiceProvider, true, true));
        }
        return result;
    }

    // ----- ----- Trust service provider ----- ----- //

    /**
     * Add a new trust service entry
     *
     * @param tsp
     * @param countryCode
     */
    public DBTrustServiceProvider addTSPEntry(TLServiceProvider tsp, String countryCode, int sequenceNumber) {
        DBTrustServiceProvider dbTSP = new DBTrustServiceProvider();
        dbTSP.setCountryCode(countryCode);
        dbTSP.setSequenceNumber(sequenceNumber);
        dbTSP.setTspId(tsp.getId());
        dbTSP.setTspNames(TLUtils.extractEnglishValues(tsp.getTSPName()));
        dbTSP.setTspTradeNames(TLUtils.extractVATorNTR(tsp.getTSPTradeName()));
        return trustServiceRepository.save(dbTSP);
    }

    // ----- ----- Trust services ----- ----- //

    /**
     * Add a new service entry
     *
     * @param service
     * @param dbTSP
     * @param countryCode
     */
    public DBService addServiceEntry(TLServiceDto service, DBTrustServiceProvider dbTSP, String countryCode) {
        DBService dbService = new DBService();
        dbService.setCountryCode(countryCode);
        dbService.setServiceId(service.getId());
        dbService.setServiceNames(TLUtils.extractEnglishValues(service.getServiceName()));
        dbService.setStatus(service.getCurrentStatus());
        dbService.setType(service.getTypeIdentifier());
        dbService.setStartingDate(service.getCurrentStatusStartingDate());
        dbService.setQServiceTypes(TLQServiceTypeUtils.getServiceTypes(service));
        if (!CollectionUtils.isEmpty(service.getExtension())) {
            for (TLServiceExtension extension : service.getExtension()) {
                if (extension.getTakenOverBy() != null) {
                    dbService.setTakenOverBy(extension.getTakenOverBy().mainOperatorName());
                }
            }
        }
        dbService.setTrustServiceProvider(dbTSP);
        return serviceRepository.save(dbService);
    }

    // ----- ----- History ----- ----- //

    public void addHistoryEntry(TLServiceHistory history, DBService dbService, String countryCode) {
        DBHistory dbHistory = new DBHistory();
        dbHistory.setCountryCode(countryCode);
        dbHistory.setHistoryId(history.getId());
        dbHistory.setHistoryNames(TLUtils.extractEnglishValues(history.getServiceName()));
        dbHistory.setStatus(history.getCurrentStatus());
        dbHistory.setType(history.getTypeIdentifier());
        dbHistory.setStartingDate(history.getCurrentStatusStartingDate());
        dbHistory.setQHistoryTypes(TLQServiceTypeUtils.getHistoryTypes(history));
        if (!CollectionUtils.isEmpty(history.getExtension())) {
            for (TLServiceExtension extension : history.getExtension()) {
                if (extension.getTakenOverBy() != null) {
                    dbHistory.setTakenOverBy(extension.getTakenOverBy().mainOperatorName());
                }
            }
        }
        dbHistory.setTrustService(dbService);
        historyRepository.save(dbHistory);
    }

    // ----- ----- Delete ----- ----- //

    /**
     * Delete trust services/services and certificate entries by country
     *
     * @param countryCode
     */
    @Transactional(value = TxType.REQUIRES_NEW)
    public void deleteDataByCountry(String countryCode) {
        certificateService.deleteByCountryCode(countryCode, TLType.TL);
        historyRepository.deleteAllByCountryCode(countryCode);
        serviceRepository.deleteAllByCountryCode(countryCode);
        trustServiceRepository.deleteAllByCountryCode(countryCode);
    }

    // ----- ----- Information ----- ----- //

    /**
     * Find all TSP names
     * 
     * @param country
     */
    public Set<String> findCountryTSPNames(String country) {
        Set<String> tspNames = new HashSet<>();
        List<DBTrustServiceProvider> tspList = trustServiceRepository.findAllByCountryCode(country);
        for (DBTrustServiceProvider tsp : tspList) {
            tspNames.addAll(tsp.getTspNames());
        }
        return tspNames;
    }
}
