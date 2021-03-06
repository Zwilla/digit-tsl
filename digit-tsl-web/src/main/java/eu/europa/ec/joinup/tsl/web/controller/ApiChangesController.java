package eu.europa.ec.joinup.tsl.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.europa.ec.joinup.tsl.business.dto.NotificationPointers;
import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.service.ContactService;
import eu.europa.ec.joinup.tsl.business.service.NotificationService;
import eu.europa.ec.joinup.tsl.business.service.SignatureChangeService;
import eu.europa.ec.joinup.tsl.business.service.TLService;
import eu.europa.ec.joinup.tsl.business.service.UserService;
import eu.europa.ec.joinup.tsl.business.util.LocationUtils;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.Tag;
import eu.europa.ec.joinup.tsl.web.form.ServiceResponse;
import eu.europa.ec.joinup.tsl.web.form.TLCookie;

@Controller
@RequestMapping(value = "/api/changes")
public class ApiChangesController {

    @Autowired
    private TLService tlService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private SignatureChangeService signatureChangeService;

    @RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ServiceResponse<List<List<TLDifference>>> getChanges(@RequestBody TLCookie cookie) {
        ServiceResponse<List<List<TLDifference>>> response = new ServiceResponse<>();
        if (tlService.inStoreOrProd(cookie.getTlId(), cookie.getCookie())) {
            List<List<TLDifference>> diffList = new ArrayList<>();
            List<TLDifference> diffSchemeInfo = new ArrayList<>();
            List<TLDifference> diffSignature = new ArrayList<>();
            TL tl = tlService.getTL(cookie.getTlId());
            if ((tl != null) && TLStatus.DRAFT.equals(tl.getDbStatus())) {
                TL publishedTl = tlService.getPublishedTLByCountryCode(tl.getSchemeInformation().getTerritory());
                if (publishedTl != null) {
                    diffSchemeInfo.addAll(tl.asPublishedDiff(publishedTl));
                    List<TLDifference> diffPointers = new ArrayList<TLDifference>(tl.getPointersDiff(publishedTl.getPointers(), tl.getId() + "_" + Tag.POINTERS_TO_OTHER_TSL));

                    diffList.add(diffSchemeInfo);
                    diffList.add(diffPointers);
                    if ((tl.getServiceProviders() != null) && (publishedTl.getServiceProviders() != null)) {
                        List<TLDifference> diffProviders = new ArrayList<TLDifference>(tl.getTrustServiceProvidersDiff(publishedTl.getServiceProviders(), tl.getId() + "_" + Tag.TSP_SERVICE_PROVIDER));
                        diffList.add(diffProviders);
                    }

                    diffSignature = signatureChangeService.getSignatureChanges(tl, publishedTl);
                    diffList.add(diffSignature);
                }
            }
            response.setContent(diffList);
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/pointers", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ServiceResponse<List<TLDifference>> getPointerChanges(@RequestBody TLCookie cookie) {
        ServiceResponse<List<TLDifference>> response = new ServiceResponse<>();
        if (tlService.inStoreOrProd(cookie.getTlId(), cookie.getCookie())) {
            List<TLDifference> diffList = new ArrayList<>();
            TL tl = tlService.getTL(cookie.getTlId());
            if ((tl != null) && TLStatus.DRAFT.equals(tl.getDbStatus())) {
                TL publishedTl = tlService.getPublishedTLByCountryCode(tl.getSchemeInformation().getTerritory());
                if (publishedTl != null) {
                    diffList.addAll(tl.getPointersDiff(publishedTl.getPointers(), tl.getId() + "_" + Tag.POINTERS_TO_OTHER_TSL));
                }
            }
            response.setContent(diffList);
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/notification/{checkHr}", method = RequestMethod.PUT)
    public @ResponseBody ServiceResponse<List<List<TLDifference>>> getDifList(@RequestBody NotificationPointers notification, @PathVariable("checkHr") int checkHr) {
        ServiceResponse<List<List<TLDifference>>> response = new ServiceResponse<>();
        List<List<TLDifference>> responseContent = new ArrayList<>();
        List<TLDifference> diffListMP = new ArrayList<>();
        List<TLDifference> diffListHR = new ArrayList<>();
        NotificationPointers lotl = null;
        if (notification.getMpPointer() != null) {
            lotl = notificationService.getNotification(notification.getMpPointer().getSchemeTerritory());

            if ((notification.getMpPointer() != null) && (lotl.getMpPointer() != null)) {
                diffListMP.addAll(notification.getMpPointer().asPublishedDiff(lotl.getMpPointer()));
                diffListMP.addAll(contactService.getContactChanges(notification.getTlsoContact(), notification.getMpPointer().getSchemeTerritory(), Tag.NOTIFICATION.toString()));

                for (TLDifference diff : diffListMP) {
                    diff.setHrLocation(LocationUtils.formatNotificationPointerId(notification.getMpPointer(), diff.getId()));
                }

            }
        }
        if (notification.getHrPointer() != null) {
            if ((notification.getHrPointer().getId() != null) && !notification.getHrPointer().getId().contains(Tag.POINTERS_TO_OTHER_TSL.toString())) {
                notification.getHrPointer().setId(notification.getHrPointer().getId() + "_" + Tag.POINTERS_TO_OTHER_TSL + "_0");
            }
            if (lotl == null) {
                lotl = notificationService.getNotification(notification.getHrPointer().getSchemeTerritory());
            }
            if (checkHr == 1) {
                // NotificationDraft
                if (lotl.getHrPointer() == null) {
                    diffListHR.add(new TLDifference(notification.getHrPointer().getId() + "_" + Tag.POINTER_LOCATION, "", notification.getHrPointer().getTlLocation()));
                } else if (StringUtils.isEmpty(notification.getHrPointer().getTlLocation())) {
                    diffListHR.add(new TLDifference(notification.getHrPointer().getId() + "_" + Tag.POINTER_LOCATION, lotl.getHrPointer().getTlLocation(), ""));
                } else {
                    diffListHR.addAll(notification.getHrPointer().asPublishedDiff(lotl.getHrPointer()));
                }
            } else {
                // NotificationForm
                if ((notification.getHrPointer() != null) && (lotl.getHrPointer() != null)) {
                    if (!notification.getHrPointer().getTlLocation().equals(lotl.getHrPointer().getTlLocation())) {
                        TLDifference diff = new TLDifference(notification.getHrPointer().getId() + "_" + Tag.POINTER_LOCATION, lotl.getHrPointer().getTlLocation(),
                                notification.getHrPointer().getTlLocation());
                        diffListHR.add(diff);
                    }
                } else {
                    if (((notification.getHrPointer() != null) && (!notification.getHrPointer().getTlLocation().equals(""))) && (lotl.getHrPointer() == null)) {
                        TLDifference diff = new TLDifference("newHR", "", notification.getHrPointer().getTlLocation());
                        diffListHR.add(diff);
                    }
                }
            }

            for (TLDifference diff : diffListHR) {
                diff.setHrLocation(LocationUtils.formatNotificationPointerId(notification.getHrPointer(), diff.getId()));
            }
        }

        responseContent.add(diffListMP);
        responseContent.add(diffListHR);
        // Users
        responseContent.add(userService.getUserDifference(Tag.NOTIFICATION.toString(), notification.getUsers(), notification.getTlsoContact().getTerritory()));
        response.setContent(responseContent);
        response.setResponseStatus(HttpStatus.OK.toString());
        return response;
    }

    @RequestMapping(value = "/schemeInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ServiceResponse<List<TLDifference>> getSchemeInfoChanges(@RequestBody TLCookie cookie) {
        ServiceResponse<List<TLDifference>> response = new ServiceResponse<>();
        if (tlService.inStoreOrProd(cookie.getTlId(), cookie.getCookie())) {
            List<TLDifference> diffList = new ArrayList<>();
            TL tl = tlService.getTL(cookie.getTlId());
            if ((tl != null) && TLStatus.DRAFT.equals(tl.getDbStatus())) {
                TL publishedTl = tlService.getPublishedTLByCountryCode(tl.getSchemeInformation().getTerritory());
                if (publishedTl != null) {
                    diffList.addAll(tl.asPublishedDiff(publishedTl));
                }
            }
            response.setContent(diffList);
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/serviceProvider", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ServiceResponse<List<TLDifference>> getProvidersChanges(@RequestBody TLCookie cookie) {
        ServiceResponse<List<TLDifference>> response = new ServiceResponse<>();
        if (tlService.inStoreOrProd(cookie.getTlId(), cookie.getCookie())) {
            List<TLDifference> diffList = new ArrayList<>();
            TL tl = tlService.getTL(cookie.getTlId());
            if ((tl != null) && TLStatus.DRAFT.equals(tl.getDbStatus())) {
                TL publishedTl = tlService.getPublishedTLByCountryCode(tl.getSchemeInformation().getTerritory());
                if ((publishedTl != null) && (tl.getServiceProviders() != null) && (publishedTl.getServiceProviders() != null)) {
                    diffList.addAll(tl.getTrustServiceProvidersDiff(publishedTl.getServiceProviders(), tl.getId() + "_" + Tag.TSP_SERVICE_PROVIDER));
                }
            }
            response.setContent(diffList);
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/signature", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ServiceResponse<List<TLDifference>> getSignature(@RequestBody TLCookie cookie) {
        ServiceResponse<List<TLDifference>> response = new ServiceResponse<>();
        if (tlService.inStoreOrProd(cookie.getTlId(), cookie.getCookie())) {
            List<TLDifference> diffList = new ArrayList<>();
            TL tl = tlService.getTL(cookie.getTlId());
            if ((tl != null) && TLStatus.DRAFT.equals(tl.getDbStatus())) {
                TL publishedTl = tlService.getPublishedTLByCountryCode(tl.getSchemeInformation().getTerritory());
                diffList = signatureChangeService.getSignatureChanges(tl, publishedTl);
            }
            response.setContent(diffList);
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/production/{prodId}/{archiveId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ServiceResponse<List<List<TLDifference>>> getChangesProd(@PathVariable("prodId") int prodId, @PathVariable("archiveId") int archiveId) {
        TL prod = tlService.getTL(prodId);
        TL archived = tlService.getTL(archiveId);
        ServiceResponse<List<List<TLDifference>>> response = new ServiceResponse<>();
        if (prod.getDbStatus().equals(TLStatus.PROD) && archived.getDbStatus().equals(TLStatus.PROD)) {
            List<List<TLDifference>> diffList = new ArrayList<>();
            List<TLDifference> diffSignature = new ArrayList<>();

            List<TLDifference> diffSchemeInfo = new ArrayList<TLDifference>(prod.asPublishedDiff(archived));
            List<TLDifference> diffPointers = new ArrayList<TLDifference>(prod.getPointersDiff(archived.getPointers(), archived.getId() + "_" + Tag.POINTERS_TO_OTHER_TSL));

            diffList.add(diffSchemeInfo);
            diffList.add(diffPointers);
            if ((prod.getServiceProviders() != null) && (archived.getServiceProviders() != null)) {
                List<TLDifference> diffProviders = new ArrayList<TLDifference>(prod.getTrustServiceProvidersDiff(archived.getServiceProviders(), prod.getId() + "_" + Tag.TSP_SERVICE_PROVIDER));
                diffList.add(diffProviders);
            }

            diffSignature = signatureChangeService.getSignatureChanges(prod, archived);
            diffList.add(diffSignature);

            response.setContent(diffList);
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

}
