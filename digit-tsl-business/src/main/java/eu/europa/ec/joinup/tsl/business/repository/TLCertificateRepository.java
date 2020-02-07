package eu.europa.ec.joinup.tsl.business.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import eu.europa.ec.joinup.tsl.model.DBCertificate;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

public interface TLCertificateRepository extends CrudRepository<DBCertificate, Integer> {

    @Override
    List<DBCertificate> findAll();

    List<DBCertificate> findByTlType(TLType type);

    List<DBCertificate> getAllByCountryCodeAndTlType(String countryCode, TLType type);

    List<DBCertificate> getAllByCountryCodeAndNotAfterBetweenAndTlType(String countryCode, Date expirationDateStart, Date expirationDateEnd, TLType type);

    List<DBCertificate> getAllByCountryCodeAndNotBeforeLessThanAndTlType(String countryCode, Date today, TLType type);

    void deleteByCountryCodeAndTlType(String countryCode, TLType type);

    void deleteByTlType(TLType type);

    List<DBCertificate> findBySki(byte[] ski);
}
