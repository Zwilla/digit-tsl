package eu.europa.ec.joinup.tsl.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ModalController {

    @RequestMapping(value = "/tl/ptot", method = RequestMethod.GET)
    public String modalPtot() {
        return "modal/modalPtot";
    }

    @RequestMapping(value = "/tl/modalTabEdit", method = RequestMethod.GET)
    public String modalTabEdit() {
        return "modal/modalTabEdit";
    }

    @RequestMapping(value = "/tl/modalPostalAddressEdit", method = RequestMethod.GET)
    public String modalPostalAddressEdit() {
        return "modal/modalPostalAddressEdit";
    }

    @RequestMapping(value = "/tl/modalDistributionListEdit", method = RequestMethod.GET)
    public String modalDistributionListEdit() {
        return "modal/modalDistributionListEdit";
    }

    @RequestMapping(value = "/tl/modalElectronicAddressEdit", method = RequestMethod.GET)
    public String modalElectronicAddressEdit() {
        return "modal/modalElectronicAddressEdit";
    }

    @RequestMapping(value = { "modalSign" }, method = RequestMethod.GET)
    public String modalSign() {
        return "modal/modalSign";
    }

    @RequestMapping(value = "/tl/modalCommunityRuleEdit", method = RequestMethod.GET)
    public String modalCommunityRuleEdit() {
        return "modal/modalCommunityRuleEdit";
    }

    @RequestMapping(value = "serviceExtension", method = RequestMethod.GET)
    public String serviceExtension() {
        return "directive/serviceExtension";
    }

    @RequestMapping(value = "modalAdditonnalExtension", method = RequestMethod.GET)
    public String modalAdditonnalExtension() {
        return "modal/service/modalAdditonnalExtension";
    }

    @RequestMapping(value = "modalTakenOverBy", method = RequestMethod.GET)
    public String modalTakenOverBy() {
        return "modal/service/modalTakenOverBy";
    }

    @RequestMapping(value = "modalExpiredCertRevocationDate", method = RequestMethod.GET)
    public String modalExpiredCertRevocationDate() {
        return "modal/service/modalExpiredCertRevocationDate";
    }

    @RequestMapping(value = "modalQualificationExtension", method = RequestMethod.GET)
    public String modalQualificationExtension() {
        return "modal/service/modalQualificationExtension";
    }

    @RequestMapping(value = "/tl/modalService", method = RequestMethod.GET)
    public String modalService() {
        return "modal/service/modalService";
    }

    @RequestMapping(value = "/tl/modalHistory", method = RequestMethod.GET)
    public String modalHistory() {
        return "modal/service/modalHistory";
    }

    @RequestMapping(value = "/tl/modalServiceProvider", method = RequestMethod.GET)
    public String modalServiceProvider() {
        return "modal/service/modalServiceProvider";
    }

    @RequestMapping(value = "/tl/modalCriteriaList", method = RequestMethod.GET)
    public String modalCriteriaList() {
        return "modal/modalCriteriaList";
    }

    @RequestMapping(value = { "modalCheck" }, method = RequestMethod.GET)
    public String modalCheck() {
        return "modal/modalCheck";
    }

    @RequestMapping(value = { "modalChange" }, method = RequestMethod.GET)
    public String modalChange() {
        return "modal/modalChange";
    }

    @RequestMapping(value = { "modalB64" }, method = RequestMethod.GET)
    public String modalB64() {
        return "modal/modalB64";
    }

    @RequestMapping(value = { "pagination" }, method = RequestMethod.GET)
    public String pagination() {
        return "directive/pagination";
    }

    @RequestMapping(value = { "/modalContact" }, method = RequestMethod.GET)
    public String modalContact() {
        return "modal/modalContact";
    }

    @RequestMapping(value = { "digitalIdentity" }, method = RequestMethod.GET)
    public String digitalIdentitie() {
        return "directive/digitalIdentity";
    }

    @RequestMapping(value = { "modalDigitalIdentity" }, method = RequestMethod.GET)
    public String modalDigitalIdentitie() {
        return "modal/modalDigitalIdentity";
    }

    @RequestMapping(value = { "modalCloneDisclaimer" }, method = RequestMethod.GET)
    public String modalCloneDisclaimer() {
        return "modal/modalCloneDisclaimer";
    }

    @RequestMapping(value = { "management/modalCheckEdit" }, method = RequestMethod.GET)
    public String modalCheckEdit() {
        return "modal/modalCheckEdit";
    }

    @RequestMapping(value = { "modalTlsoContact" }, method = RequestMethod.GET)
    public String modalTlsoContact() {
        return "modal/modalTlsoContact";
    }

    @RequestMapping(value = { "modalAvailability" }, method = RequestMethod.GET)
    public String modalAvailability() {
        return "modal/modalAvailability";
    }

    @RequestMapping(value = { "modalChangesProd" }, method = RequestMethod.GET)
    public String modalChangesProd() {
        return "modal/modalChangesProd";
    }

    @RequestMapping(value = { "modalAddUser" }, method = RequestMethod.GET)
    public String modalAddUser() {
        return "modal/modalAddUser";
    }

    @RequestMapping(value = { "modalRetentionTlDetails" }, method = RequestMethod.GET)
    public String modalRetentionTlDetails() {
        return "modal/modalRetentionTlDetails";
    }

    @RequestMapping(value = { "modalMergeDraft" }, method = RequestMethod.GET)
    public String modalMergeDraft() {
        return "modal/modalMergeDraft";
    }

}
