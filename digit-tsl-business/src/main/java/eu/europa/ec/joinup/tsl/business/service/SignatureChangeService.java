package eu.europa.ec.joinup.tsl.business.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.dto.TLSignature;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.util.LocationUtils;
import eu.europa.ec.joinup.tsl.model.enums.SignatureStatus;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

/**
 * Calcul TLDifferences of different trusted list signature
 */
@Service
public class SignatureChangeService {

    @Autowired
    private TLService tlService;

    /**
     * Get signature changes between current trusted list & compared one
     *
     * @param currentTl
     * @param comparedTl
     * @return
     */
    public List<TLDifference> getSignatureChanges(TL currentTl, TL comparedTl) {

        TLSignature signature = tlService.getSignatureInfo(currentTl.getTlId());
        TLSignature comparedSignature = tlService.getSignatureInfo(comparedTl.getTlId());

        List<TLDifference> differences = new ArrayList<>(getChanges(currentTl.getId(), signature, comparedSignature));
        for (TLDifference diff : differences) {
            diff.setHrLocation(LocationUtils.idUserReadable(currentTl, diff.getId()));
        }
        return differences;
    }

    public List<TLDifference> getChanges(String id, TLSignature signature, TLSignature comparedSignature) {
        List<TLDifference> differences = new ArrayList<>();

        if ((signature == null) || signature.getIndication().equals(SignatureStatus.NOT_SIGNED)) {
            if ((comparedSignature != null) && !comparedSignature.getIndication().equals(SignatureStatus.NOT_SIGNED)) {
                differences.add(new TLDifference(id + "_" + Tag.SIGNATURE, comparedSignature.getSignedBy() + " - " + comparedSignature.getSigningDate().toString(), ""));
            }
        } else {
            differences.addAll(signature.asPublishedDiff(comparedSignature, id + "_" + Tag.SIGNATURE));
        }
        return differences;
    }

}
